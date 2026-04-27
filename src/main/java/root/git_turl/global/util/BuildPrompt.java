package root.git_turl.global.util;

import org.springframework.stereotype.Component;
import root.git_turl.domain.report.dto.GitAnalysisResult;
import root.git_turl.domain.report.dto.commit.MajorCommit;

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
}
