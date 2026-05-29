package root.git_turl.global.util.prompt;

import org.springframework.stereotype.Component;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.report.dto.GitAnalysisResult;
import root.git_turl.domain.report.dto.commit.MajorCommit;
import root.git_turl.global.util.parser.DiffStructureParser;

import java.util.List;

@Component
public class BuildJudgePrompt {
    private static String BASE_PROMPT = """
        [출력 형식]
        반드시 JSON만 출력한다.
         {
           "deductions": [
             {"item": "A", "count": 0, "detail": ""},
             {"item": "B", "count": 0, "detail": ""},
             {"item": "C", "count": 0, "detail": ""},
             {"item": "D", "count": 0, "detail": ""},
             {"item": "E", "count": 0, "detail": ""}
           ],
           "score": 0,
           "result": "SUCCESS",
           "reason": ""
         }""";
    private static String REPORT_PROMPT = """
        너는 리포트 품질 평가자다. 아래 체크리스트로 감점하여 최종 점수를 계산하라.
    
       [기본 점수: 10점]

         [감점 규칙 - 해당 항목 발견 시 즉시 감점]
         A. improvements 항목에 실제 파일명/클래스명/메서드명이 없는 경우 → -2점/개
         B. currentStatus가 1문장 이하이거나 데이터 근거 없는 경우 → -1점/개
         C. example이 없거나 "~할 수 있습니다" 수준의 추상적 설명인 경우 → -1점/개
         D. actionPlan이 없거나 "주기적으로 검토", "재점검" 수준인 경우 → -1점/개
         E. improvements 전체가 "테스트 부족", "문서화 필요", "가독성 향상" 같은
            스프링/자바 일반론으로만 구성된 경우 → -3점

       [계산]
       최종 점수 = 10 - 각 감점 합산 (최저 1점)
       7점 이상 = SUCCESS, 6점 이하 = FAIL
        
        [반드시 확인할 항목]
        1. 개선 사항마다 실행 계획이 있는가
        2. 개선 사항마다 실제 예시가 있는가
        3. 파일명/클래스명/메서드명이 포함되는가
        4. 일반론만 반복하지 않는가
        5. 프로젝트 고유의 근거가 있는가
        
      
    """;

    private static String QUESTION_PROMPT = """
         너는 개발자 면접 질문 품질 평가자다. 아래 체크리스트로 감점하여 최종 점수를 계산하라.
    
        [기본 점수: 10점]

        [감점 규칙]
        A. 질문이 "~를 아시나요?", "~란 무엇인가요?" 수준의 단순 암기형인 경우 → -1점/개
        B. 질문이 신입/주니어 수준에 비해 너무 쉬운 경우 (예: "Java가 뭔가요?") → -1점/개
        C. 질문이 해당 레포와 무관한 일반 CS 지식만 묻는 경우 → -2점/개
           (예: 레포에 JWT 코드가 있는데 "OSI 7계층을 설명하세요" 같은 질문)
        D. 질문이 실무 면접에서 나오기 어려운 수준으로 난해한 경우 → -1점/개
        E. 유사한 질문이 중복되는 경우 → -1점/쌍

        [판정]
        최종 점수 = 10 - 감점 합산 (최저 1점)
        7점 이상 = SUCCESS, 6점 이하 = FAIL
  
    """;

    public String buildReportJudgePrompt(GitAnalysisResult result, String contentJson) {

        StringBuilder sb = new StringBuilder();
        sb.append(REPORT_PROMPT);
        sb.append(BASE_PROMPT);

        sb.append("다음은 개발자의 Git 활동 데이터와 이를 바탕으로 생성한 요약본이다.\n\n");

        sb.append("\n주요 커밋\n");
        for (MajorCommit mc : result.getMajorCommits()) {
            sb.append("- ").append(mc.getMessage()).append("\n");
            sb.append("  diff:\n");
            sb.append(mc.getDiff()).append("\n\n");
        }

        // diff 반영
        sb.append("\n[diff summary]\n");

        for (DiffStructureParser.DiffSummary summary : result.getSummaryList()) {
            sb.append("- 변경 파일 수: ")
                    .append(summary.getFileCount())
                    .append("\n");

            sb.append("  추가 라인: ")
                    .append(summary.getAddedLines())
                    .append("\n");

            sb.append("  삭제 라인: ")
                    .append(summary.getDeletedLines())
                    .append("\n");

            for (DiffStructureParser.ChangedFile file : summary.getChangedFiles()) {
                sb.append("    * ")
                        .append(file.getFileName())
                        .append(" (+")
                        .append(file.getAddedLines())
                        .append(", -")
                        .append(file.getDeletedLines())
                        .append(")\n");
            }

            sb.append("\n");
        }

        sb.append("생성된 리포트 내용: %s".formatted(contentJson));

        sb.append("다음 Git 분석 데이터를 기반으로 개발자 분석 리포트를 평가하라.\n" +
                "\n" +
                "반드시 아래 조건을 모두 지켜라:\n" +
                "\n" +
                "1. 반드시 JSON 형식으로만 응답하라.\n" +
                "2. JSON 외의 텍스트(설명, 마크다운, 코드블록, 주석) 절대 포함 금지.\n" +
                "3. 모든 필드는 반드시 채워라\n" +
                "4. key 이름은 절대 변경하지 마라.\n" +
                "5. 문자열은 모두 큰따옴표(\"\") 사용.\n" +
                "6. 숫자는 숫자 타입으로 작성 (따옴표 금지).\n" +
                "7. JSON 문법 오류 발생 시 실패로 간주한다.\n" +
                "8. 추측성 말투를 쓰지 말것.\n" +
                "\n" +
                "---\n" +
                "\n" +
                "다음 구조를 정확히 따라라:\n" +
                "{\n" +
                "  \"score\": 6,\n" +
                "  \"result\": \"SUCCESS 또는 FAIL\",\n" +
                "  \"reason\": \"근거 부족, 예시 없음등 이유 1~2줄\"\n" +
                "}" );
        return sb.toString();
    }

    public String buildQuestionJudgePrompt (String question) {
        StringBuilder sb = new StringBuilder();

        sb.append(QUESTION_PROMPT);
        sb.append(BASE_PROMPT);

        return sb.toString();
    }
}
