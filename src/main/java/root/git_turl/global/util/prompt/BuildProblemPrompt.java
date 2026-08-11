package root.git_turl.global.util.prompt;

import org.springframework.stereotype.Component;
import root.git_turl.domain.report.dto.GitAnalysisResult;
import root.git_turl.domain.report.dto.commit.MajorCommit;

@Component
public class BuildProblemPrompt {
    public String buildReportProblemPrompt(GitAnalysisResult result) {
        StringBuilder sb = new StringBuilder();

        sb.append("""
        아래 diff 데이터를 분석하여 코드에서 발견되는 구체적인 문제점을 추출하라.
        
        [추출 규칙]
         - 반드시 실제 파일명, 클래스명, 메서드명을 언급하라
         - 추측 금지. diff에 보이는 것만 작성하라
         - 일반론 금지 ("예외 처리 부족", "테스트 필요" 같은 표현 사용 금지)
         - 위 [프로젝트 개요]의 비즈니스 도메인과 연결하여 설명하라
         - 최소 3개, 최대 5개 문제점 추출
        
        [출력 형식]
            - 반드시 JSON만 출력하라.
            - ```json 같은 마크다운 코드블록 절대 사용 금지.
            - { 로 시작해서 } 로 끝나야 한다.
        {
          "problems": [
            {
              "file": "실제파일명",
              "issue": "구체적 문제 1문장",
              "evidence": "diff에서 발견한 근거 (코드 스니펫 또는 변경 내용)"
            }
          ]
        }
        
        [diff 데이터]
        """);

        for (MajorCommit mc : result.getMajorCommits()) {
            sb.append("- ").append(mc.getMessage()).append("\n");
            sb.append(mc.getDiff()).append("\n\n");
        }

        return sb.toString();
    }
}
