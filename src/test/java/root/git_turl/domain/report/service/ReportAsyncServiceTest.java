package root.git_turl.domain.report.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import root.git_turl.domain.report.dto.*;
import root.git_turl.domain.report.dto.commit.CommitTypeCount;
import root.git_turl.domain.report.dto.commit.GitCommit;
import root.git_turl.domain.report.dto.reportDetail.ReportContent;
import root.git_turl.domain.report.dto.reportDetail.ReportWrapper;
import root.git_turl.domain.report.dto.reportDetail.Scale;
import root.git_turl.domain.report.dto.reportDetail.Stack;
import root.git_turl.domain.report.dto.stat.FileStat;
import root.git_turl.domain.report.enums.GenerationStatus;
import root.git_turl.domain.report.service.stat.FileSelector;
import root.git_turl.domain.report.service.warn.ReportWarningEvaluator;
import root.git_turl.global.util.parser.GitLogParser;
import root.git_turl.global.util.prompt.BuildJudgePrompt;
import root.git_turl.global.util.prompt.BuildProblemPrompt;
import root.git_turl.global.util.prompt.BuildPrompt;
import root.git_turl.global.util.prompt.BuildRetryPrompt;
import root.git_turl.infrastructure.github.GitCloneService;
import root.git_turl.infrastructure.judge.JudgeResult;
import root.git_turl.infrastructure.judge.JudgeService;
import root.git_turl.infrastructure.judge.Result;
import root.git_turl.infrastructure.openai.GptService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReportAsyncServiceTest {

    @Mock GitLogParser gitLogParser;
    @Mock GitAnalysisService gitAnalysisService;
    @Mock GptService gptService;
    @Mock JudgeService judgeService;
    @Mock GitCloneService gitCloneService;
    @Mock ReportUpdateService reportUpdateService;
    @Mock ReportWarningEvaluator reportWarningEvaluator;
    @InjectMocks ReportAsyncService service;
    @Mock BuildPrompt buildPrompt;
    @Mock BuildJudgePrompt buildJudgePrompt;
    @Mock BuildRetryPrompt buildRetryPrompt;
    @Mock BuildProblemPrompt buildProblemPrompt;
    private final FileSelector fileSelector = new FileSelector();

    @BeforeEach
    void setUp() {
        service = new ReportAsyncService(
                gitLogParser,
                gitAnalysisService,
                buildPrompt,
                gptService,
                null,                    // reportRepository - 이 테스트 흐름에서 직접 안 쓰이면 null 가능, 실제로 쓰이면 @Mock으로
                new ObjectMapper(),      // 실제 인스턴스
                gitCloneService,
                judgeService,
                buildJudgePrompt,
                buildRetryPrompt,
                buildProblemPrompt,
                reportUpdateService,
                reportWarningEvaluator
        );
    }
    @Test
    void 유저_변경_없는_파일은_selectUserFiles에서_제외된다() {
        FileStat notTouched = new FileStat("A.java");
        notTouched.addChange(50, false, LocalDate.now()); // 유저 아님

        FileStat touched = new FileStat("B.java");
        touched.addChange(30, true, LocalDate.now()); // 유저가 건드림

        Map<String, FileStat> stats = Map.of("A.java", notTouched, "B.java", touched);

        List<FileStat> result = fileSelector.selectUserFiles(stats, 10);

        assertThat(result).extracting(FileStat::getFilePath).containsExactly("B.java");
    }

    @Test
    void service_domain_경로는_config_test_경로보다_높은_점수를_받는다() {
        FileStat service = new FileStat("src/main/java/.../service/UserService.java");
        service.addChange(100, true, LocalDate.now());

        FileStat config = new FileStat("src/main/java/.../config/WebConfig.java");
        config.addChange(100, true, LocalDate.now());

        Map<String, FileStat> stats = Map.of("service", service, "config", config);

        List<FileStat> result = fileSelector.selectUserFiles(stats, 10);

        assertThat(result.get(0).getFilePath()).contains("service");
    }

    @Test
    void judge가_PASS면_리포트가_저장되고_warnings가_함께_전달된다() throws Exception {
        given(gitCloneService.cloneRepository(any())).willReturn("/tmp/repo");
        given(gitLogParser.getCommits(any())).willReturn(List.of(sampleCommit()));
        given(gitAnalysisService.analyze(any(), any(), any(), any())).willReturn(sampleResult());
        given(reportWarningEvaluator.evaluate(any(), anyBoolean())).willReturn(List.of("커밋 수가 적습니다"));

        given(buildProblemPrompt.buildReportProblemPrompt(any())).willReturn("dummy problem prompt");
        given(buildPrompt.buildReportPrompt(any(), any(), any())).willReturn("dummy report prompt");
        given(buildJudgePrompt.buildReportJudgePrompt(any(), any())).willReturn("dummy judge prompt");

        given(gptService.makeReportProblem(any())).willReturn(sampleProblemList());
        given(gptService.analyzeGit(any())).willReturn(sampleReportWrapper());
        given(judgeService.evaluate(any())).willReturn(
                new JudgeResult(null, 8, Result.SUCCESS, "적합한 근거 포함")
        );

        service.generateReport(sampleEvent());

        verify(reportUpdateService).updateReport(
                anyLong(), anyString(), anyString(), eq(GenerationStatus.DONE),
                eq(List.of("커밋 수가 적습니다"))
        );
    }

    private GitCommit sampleCommit() {
        return new GitCommit(
                "abc123",                       // hash
                "정규은",                        // authorName
                "user@example.com",             // authorEmail
                LocalDate.now().minusDays(1),   // date
                "feat: 로그인 기능 추가"          // message
        );
    }

    private GitAnalysisResult sampleResult() {
        return GitAnalysisResult.builder()
                .totalCommits(10)
                .userTotalCommits(3)
                .contributionRate(30.0)
                .commitTypeCount(CommitTypeCount.builder()
                        .featCount(2)
                        .fixCount(1)
                        .refactorCount(0)
                        .etcCount(0)
                        .build())
                .sampleMessages(List.of("feat: 로그인 기능 추가"))
                .contributionAnalyze(Map.of("user1", 3L, "user2", 7L))
                .readmeSummary("이 프로젝트는 커밋 기반 분석 서비스입니다.")
                .projectRepresentativeFiles(List.of(
                        new RepresentativeFile("src/main/.../UserService.java", "class UserService {}", false, 5, 120)
                ))
                .userRepresentativeFiles(List.of(
                        new RepresentativeFile("src/main/.../ReportController.java", "class ReportController {}", false, 2, 40)
                ))
                .build();
    }

    private ProblemList sampleProblemList() {
        return ProblemList.builder()
                .problems(List.of(
                        Problem.builder()
                                .file("ReportController.java")
                                .issue("입력값 검증 없이 바로 서비스 로직 호출")
                                .evidence("if (request == null) 체크 없이 request.getId() 호출")
                                .build()
                ))
                .build();
    }

    private ReportWrapper sampleReportWrapper() {
        ReportContent content = new ReportContent();
        content.setPurpose("이 프로젝트는 Git 커밋을 분석하여 리포트를 생성하는 서비스입니다.");
        content.setStack(new Stack("Java", "Spring Boot", "Lombok", "Spring Security"));
        content.setScale(new Scale(27, 10));
        content.setReports("전체 커밋 활동을 분석한 결과 ...(4문장 이상 서술)...");
        content.setFeatures(null);
        content.setImprovements(null);

        ReportWrapper wrapper = new ReportWrapper();
        wrapper.setContent(content);
        return wrapper;
    }

    private ReportSavedEvent sampleEvent() {
        ReportReqDto.Repo dto = new ReportReqDto.Repo();
        dto.setFullName("testuser/testrepo");

        return new ReportSavedEvent(
                1L,
                "user@example.com",
                "testuser",
                dto
        );
    }
}