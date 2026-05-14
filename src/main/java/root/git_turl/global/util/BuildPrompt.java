package root.git_turl.global.util;

import org.springframework.stereotype.Component;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.report.dto.GitAnalysisResult;
import root.git_turl.domain.report.dto.commit.MajorCommit;
import root.git_turl.domain.report.entity.Report;

@Component
public class BuildPrompt {
    public String buildReportPrompt(GitAnalysisResult result) {

        StringBuilder sb = new StringBuilder();

        sb.append("다음은 한 개발자의 Git 활동 데이터입니다.\n\n");

        sb.append("전체 통계\n");
        sb.append("- 전체 커밋 수: ").append(result.getTotalCommits()).append("\n");
        sb.append("- 개인 커밋 수: ").append(result.getUserTotalCommits()).append("\n");
        sb.append("- 기여도: ").append(result.getContributionRate()).append("%\n\n");

        sb.append("[commitContribution]\n");
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

        sb.append("다음 Git 분석 데이터를 기반으로 개발자 분석 리포트를 작성하라.\n" +
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
                "\n" +
                "{\n" +
                "  \"content\": {\n" +
                "    \"purpose\": \"\",\n" +
                "    \"stack\": {\n" +
                "      \"language\": \"\",\n" +
                "      \"framework\": \"\",\n" +
                "      \"library\": \"\",\n" +
                "      \"security\": \"\"\n" +
                "    },\n" +
                "    \"commitStats\": {\n" +
                "      \"totalCommits\": 0,\n" +
                "      \"myCommits\": 0,\n" +
                "      \"myCommitRate\": 0.0\n" +
                "    },\n" +
                "   \"commitContribution\": {\n" +
                "    \"username\": 0,\n" +
                "   }" +
                "    \"scale\": {\n" +
                "      \"fileCount\": 0,\n" +
                "      \"commitCount\": 0\n" +
                "    },\n" +
                "    \"reports\": \"\"\n" +
                "    \"features\": {\n" +
                "       \"feature1\": {\n" +
                "           \"title\": \"\", \n" +
                "           \"files\": [], \n" +
                "           \"content\": \"\", \n" +
                "       },\n" +
                "    },\n" +
                "    \"improvements\": {\n" +
                "       \"improvement1\": {\n" +
                "           \"title\": \"\", \n" +
                "           \"content\": \"\", \n" +
                "       }\n" +
                "   }\n" +
                "}\n" +
                "\n" +
                "---\n" +
                "\n" +
                "작성 가이드:\n" +
                "\n" +
                "- purpose: 프로젝트 목적을 2~3문장으로 구체적으로 설명\n" +
                "- stack: 분석된 기술 스택을 최대한 구체적으로 작성\n" +
                "- commitStats: 제공된 데이터를 기반으로 정확히 작성\n" +
                "- scale: 프로젝트 규모를 수치 기반으로 설명\n" +
                "- reports: " +
                "최소 1200자 이상 작성\n" +
                "  다음 항목을 모두 포함:\n" +
                "    - 커밋 패턴 분석\n" +
                "    - 코드 변경 스타일\n" +
                "    - 협업 방식\n" +
                "    - 개발 성향\n" +
                "    - 프로젝트 구조 추론\n" +
                "- commitContribution: 위 JSON 데이터를 그대로 반영" +
                "- features: 구현 기능 분석 5개, 다음 정보를 포함해서 작성" +
                "    - title: 기능 요약 제목 (예: JWT 토큰 기반 인증)\n" +
                "    - files: 관련 파일들 이름 리스트 (예: [\"JwtService.java\",\"JwtFilter\"]])\n" +
                "    - content: 분석 내용 (2문장 이상)\n" +
                "- improvements: 개선할 점 (최소 3개 이상, 각 항목 구체적 근거 포함)" +
                "    - title: 개선할 점 제목 (예: 공통 응답 처리)\n" +
                "    - content: 분석 내용 (2문장 이상)\n" +
                "\n");

        return sb.toString();
    }

    public String buildQuestionPrompt(Report report, Integer questionCount) {
        StringBuilder sb = new StringBuilder();

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

    public String buildFeedbackPrompt(String content, Question question) {
        StringBuilder sb = new StringBuilder();
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
