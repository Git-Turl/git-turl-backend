package root.git_turl.global.util.prompt;

import org.springframework.stereotype.Component;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.report.dto.GitAnalysisResult;
import root.git_turl.domain.report.dto.commit.MajorCommit;
import root.git_turl.domain.report.entity.Report;

@Component
public class BuildRetryPrompt {
    public String buildReportRetryPrompt(GitAnalysisResult result, String userId,  int score, String reason) {

        StringBuilder sb = new StringBuilder();

        sb.append("다음은 한 개발자의 Git 활동 데이터입니다.\n\n");

        sb.append("[commitContribution](유저 아이디: %s)\n\\n".formatted(userId));
        sb.append("{\n");
        result.getContributionAnalyze().forEach((k, v) ->
                sb.append("  \"").append(k).append("\": ").append(v).append(",\n")
        );
        if (!result.getContributionAnalyze().isEmpty()) {
            sb.setLength(sb.length() - 2);
            sb.append("\n");
        }
        sb.append("}\n\n");


        sb.append("\n주요 커밋\n");
        for (MajorCommit mc : result.getMajorCommits()) {
            sb.append("- ").append(mc.getMessage()).append("\n");
            sb.append("  diff:\n");
            sb.append(mc.getDiff()).append("\n\n");
        }

        sb.append("실패 원인: ").append(reason).append("\n");
        sb.append("""
            이 실패를 해결하기 위해, 각 개선 항목은 반드시 아래 3가지를 포함해야 한다.
            - 문제 요약
            - 구체적인 실행 계획
            - 실제 적용 예시
            
            예:
            문제 요약: 테스트 코드가 부족하다.
            구체적인 실행 계획: JUnit으로 서비스 계층 단위 테스트를 작성하고, 핵심 예외 케이스를 3개 이상 추가한다.
            실제 적용 예시: QuestionService의 질문 저장 로직에 대해 정상/예외/권한 없음 케이스를 각각 검증한다.
            
            반드시 아래 조건을 만족해야 PASS:
            1. 각 개선점마다 실행 계획이 1개 이상 있어야 한다.
            2. 각 개선점마다 예시가 1개 이상 있어야 한다.
            3. "필요하다", "중요하다" 같은 추상 표현만 쓰면 안 된다.
            4. 사람의 행동 단위로 쓸 것. 예: "JUnit 테스트 3개 작성", "Swagger 예시 추가"
        """);


        sb.append("다음 Git 분석 데이터를 기반으로, 이전 실패 내용을 보강하여 개발자 분석 리포트를 작성하라.\n" +
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
                "8. 추측성 말투를 쓰지 말것.\n" );
        sb.append("{\n")
                .append("  \"content\": {\n")
                .append("    \"purpose\": \"\",\n")
                .append("    \"stack\": {\n")
                .append("      \"language\": \"\",\n")
                .append("      \"framework\": \"\",\n")
                .append("      \"library\": \"\",\n")
                .append("      \"security\": \"\"\n")
                .append("    },\n")
                .append("    \"commitStats\": {\n")
                .append("      \"totalCommits\": 0,\n")
                .append("      \"myCommits\": 0,\n")
                .append("      \"myCommitRate\": 0.0\n")
                .append("    },\n")
                // commitContribution: 실제 데이터에서 키를 그대로 복사하도록 안내
                .append("    \"commitContribution\": {\n")
                .append("      \"유저아이디_예시\": 0\n")
                .append("    },\n")
                .append("    \"scale\": {\n")
                .append("      \"fileCount\": 0,\n")
                .append("      \"commitCount\": 0\n")
                .append("    },\n")
                .append("    \"reports\": \"\",\n")
                .append("    \"features\": {\n")
                .append("      \"feature1\": { \"title\": \"\", \"files\": [], \"content\": \"\" },\n")
                .append("      \"feature2\": { \"title\": \"\", \"files\": [], \"content\": \"\" },\n")
                .append("      \"feature3\": { \"title\": \"\", \"files\": [], \"content\": \"\" },\n")
                .append("      \"feature4\": { \"title\": \"\", \"files\": [], \"content\": \"\" },\n")
                .append("      \"feature5\": { \"title\": \"\", \"files\": [], \"content\": \"\" }\n")
                .append("    },\n")
                .append("    \"improvements\": {\n")
                .append("      \"improvement1\": {\n")
                .append("        \"title\": \"\", \"files\": [], \"currentStatus\": \"\",\n")
                .append("        \"example\": \"\", \"actionPlan\": \"\"\n")
                .append("      },\n")
                .append("      \"improvement2\": {\n")
                .append("        \"title\": \"\", \"files\": [], \"currentStatus\": \"\",\n")
                .append("        \"example\": \"\", \"actionPlan\": \"\"\n")
                .append("      },\n")
                .append("      \"improvement3\": {\n")
                .append("        \"title\": \"\", \"files\": [], \"currentStatus\": \"\",\n")
                .append("        \"example\": \"\", \"actionPlan\": \"\"\n")
                .append("      }\n")
                .append("    }\n")
                .append("  }\n")
                .append("}\n\n");

        // 작성 가이드
        sb.append("""
        [작성 가이드]

        - purpose: 프로젝트 목적을 2~3문장으로 구체적으로 설명.

        - stack: diff에서 확인된 실제 기술 스택만 작성. 추측 금지.

        - commitStats: 위 commitContribution 데이터 합산으로 totalCommits 계산.
          myCommits는 분석 대상 유저(%s)의 커밋 수. myCommitRate = myCommits / totalCommits.

        - commitContribution: 위 [commitContribution] 데이터의 키-값을 그대로 복사.
          절대 추측하거나 변형하지 마라.

        - scale: diff summary 기준 파일 수와 커밋 수를 수치로 작성.
        
        - reports: 분석 내역을 텍스트로 작성, 최소 4문장 이상 구체적으로

        - features: 5개 기능을 diff에서 확인된 실제 파일 기반으로 작성.
          각 content는 2문장 이상.

        - improvements: 3개 이상 작성. 아래 기준 필수 준수.
          ❌ FAIL 처리 (6점 이하): "테스트 코드 부족", "가독성 향상", "예외 처리 필요" 같은 일반론
          ✅ PASS 기준 (7점 이상): 이 프로젝트 고유의 비즈니스 도메인과 연계한 구체적 분석
          
          각 필드 기준:
          · currentStatus: diff/커밋에 등장한 실제 파일명·클래스명·메서드명을 반드시 인용.
                           현재 어떤 구조적 문제가 있는지 2문장 이상.
          · example: 해당 문제가 실제로 어떤 흐름에서 발생하는지 구체적 시나리오 기술.
          · actionPlan: 1단계/2단계/3단계로 코드 또는 아키텍처 레벨의 해결 계획 제시.

          예시 (✅ 허용):
          currentStatus: "GithubAnalysisService.analyze()에서 GitHub API 호출 실패 시
                          예외가 상위로 전파되어 면접 질문 생성 플로우 전체가 중단됨."
          example: "사용자가 분석 요청 시 GitHub API rate limit 초과 → RuntimeException 전파
                    → 면접 질문 생성 불가 → 사용자에게 500 에러 반환"
          actionPlan: "1단계: GithubAnalysisService에 Resilience4j CircuitBreaker 적용
                       2단계: 최근 분석 결과를 Redis에 캐싱하여 fallback 응답 제공
                       3단계: rate limit 임박 시 사전 경고 로직 추가"

        [자가검증 - 출력 전 반드시 확인]
        □ improvements 각 항목에 실제 파일명/클래스명이 포함되었는가?
        □ improvements 각 항목에 example과 actionPlan이 있는가?
        □ 일반론("테스트 부족", "가독성" 등)만 있지 않은가?
        □ reports가 5개 항목을 각각 200자 이상 포함하는가?
        □ trailing comma가 없는가?
        위 항목 중 하나라도 NO이면 해당 항목을 재작성 후 출력하라.
        """.formatted(userId));

        return sb.toString();
    }

    public String buildQuestionRetryPrompt(Report report, int questionCount, String reason) {
        StringBuilder sb = new StringBuilder();

        sb.append("다음 실패 사유를 참고하여 질문을 재생성하여라. 실패 이유: $s".formatted(reason));

        sb.append("다음은 한 개발자의 Github repository 분석본입니다. \n");
        sb.append(report.getContentJson());
        sb.append("\n 해당 분석본을 읽고, 아래 기준을 참고하여 적절한 개발자 면접 질문을 %d개 생성하세요." .formatted(questionCount));
        sb.append("\n레포 연관성:t레포의 특정 코드, 기능, 설계 의도에서 직접 도출된 질문임.\n");
        sb.append("면접 적합성: 실제 기술면접에서 물어볼 만한 수준이며, 지원자의 사고 과정을 확인할 수 있음.");
        sb.append("""
            반드시 아래 JSON 형식으로만 응답하세요.
            설명, 마크다운, 번호, 코드블록 없이 JSON만 반환하세요.
            Map 형태로, key값은 질문, value값은 해당 질문을 답변하기 적정한 초수를 정수형으로 표기하세요.
             {
              "questions": {
                "생성한 질문1": 60,
                "질문2": 45,
                "질문3": 60
              }
            }
        """);
        return sb.toString();
    }

    public String buildFeedbackRetryPrompt(String content, Question question, String feedback, String reason) {
        StringBuilder sb = new StringBuilder();

        sb.append("다음 실패 사유를 참고하여 답변애 대한 피드백을 재생성하여라. 질문: $s, 답변: $s, 피드백: $s, 실패 사유: $s"
                .formatted(question.getContent(), content, feedback, reason));

        sb.append("개발자 면접 질문에 대한 사용자의 답변을 평가하세요. \n");
        sb.append("질문: ")
                .append(question.getContent())
                .append("\n");
        sb.append("적당한 응답 시간: ")
                .append(question.getTime())
                .append("\n");
        sb.append("사용자 응답: ")
                .append(content)
                .append("\n");
        sb.append("평가 항목은 다음과 같습니다. 근거성\t답변 내용이 질문 의도와 레포 내용에 명확히 근거함. “왜 틀렸는지/왜 부족한지”가 분명함.\n" +
                "논리성\t주장-이유-예시-결론 흐름이 자연스럽고 설득력 있음.\n" +
                "구체성\t“어떤 부분을 어떻게 고쳐야 하는지”까지 명확히 제시함.\n" +
                "개선 가능성\t바로 다음 답변이나 다음 작업에 적용 가능함. \n" +
                "반드시 JSON 형식으로만 응답한다. 다른 어떤 텍스트도 포함하지 않는다.\n" +
                "마크다운 금지, 설명 금지, 코드블록 금지.\n" +
                "content 필드는 반드시 하나의 문자열로 작성하며 줄바꿈(\\n) 없이 255자 이내로 작성한다.\n" +
                "문장은 자연스럽게 작성하되 하나의 문단으로만 구성한다.\n" +
                "반환 형식:\n" +
                "{\"content\": \"...\"}\n");
        return sb.toString();
    }
}
