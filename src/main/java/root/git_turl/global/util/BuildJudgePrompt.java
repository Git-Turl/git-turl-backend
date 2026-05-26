package root.git_turl.global.util;

import org.springframework.stereotype.Component;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.report.dto.GitAnalysisResult;
import root.git_turl.domain.report.dto.commit.MajorCommit;
import root.git_turl.domain.report.entity.Report;

@Component
public class BuildJudgePrompt {
    private static String BASE_PROMPT = "반드시 아래 기준으로 평가해라.\n" +
            "\n" +
            "- 8~10: 매우 우수\n" +
            "- 6~7: 보통\n" +
            "- 1~5: 부족\n" +
            "\n" +
            "score가 7 이상이면 SUCCESS\n" +
            "그 미만이면 FAIL";

    public String buildReportJudgePrompt(GitAnalysisResult result, String contentJson) {

        StringBuilder sb = new StringBuilder();

        sb.append("다음은 개발자의 Git 활동 데이터와 이를 바탕으로 생성한 요약본이다.\n\n");

        sb.append("\n주요 커밋\n");
        for (MajorCommit mc : result.getMajorCommits()) {
            sb.append("- ").append(mc.getMessage()).append("\n");
            sb.append("  diff:\n");
            sb.append(mc.getDiff()).append("\n\n");
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
                "  \"result\": SUCCESS 또는 FAIL,\n" +
                "  \"reason\": \"근거 부족, 예시 없음등 이유 1~2줄\"\n" +
                "}" );

        sb.append(BASE_PROMPT);
        return sb.toString();
    }
}
