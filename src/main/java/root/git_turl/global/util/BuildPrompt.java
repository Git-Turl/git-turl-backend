package root.git_turl.global.util;

import root.git_turl.domain.report.dto.GitAnalysisResult;
import root.git_turl.domain.report.dto.commit.MajorCommit;

public class BuildPrompt {
    public String buildReportPrompt(GitAnalysisResult result) {

        StringBuilder sb = new StringBuilder();

        sb.append("다음은 한 개발자의 Git 활동 데이터입니다.\n\n");

        sb.append("전체 통계\n");
        sb.append("- 전체 커밋 수: ").append(result.getTotalCommits()).append("\n");
        sb.append("- 개인 커밋 수: ").append(result.getUserTotalCommits()).append("\n");
        sb.append("- 기여도: ").append(result.getContributionRate()).append("%\n\n");

        sb.append("커밋 유형\n");
        sb.append("- feat: ").append(result.getCommitTypeCount().getFeatCount()).append("\n");
        sb.append("- fix: ").append(result.getCommitTypeCount().getFixCount()).append("\n");
        sb.append("- refactor: ").append(result.getCommitTypeCount().getRefactorCount()).append("\n\n");

        sb.append("기여자\n");
        result.getContributionAnalyze().forEach((k, v) ->
                sb.append("- ").append(k).append(": ").append(v).append("\n")
        );

        sb.append("\n주요 커밋\n");
        for (MajorCommit mc : result.getMajorCommits()) {
            sb.append("- ").append(mc.getMessage()).append("\n");
            sb.append("  diff:\n");
            sb.append(mc.getDiff()).append("\n\n");
        }

        sb.append("\n이 데이터를 기반으로 다음을 포함한 개발자 분석 리포트를 작성하세요:\n");
        sb.append("1. 프로젝트 개요\n");
        sb.append("1-1. 프로젝트 목적\n");
        sb.append("1-2. 기술 스택\n");
        sb.append("2. 프로젝트 커밋 분석\n");
        sb.append("3. 구현 기능 분석\n");
        sb.append("4. 코드 품질 및 개선 방향\n");
        sb.append("5. 종합 평가\n");
        sb.append("위 데이터를 기반으로 A4 2~3페이지 분량의 상세한 개발자 분석 리포트를 작성합니다.\n" +
                "단순 나열이 아니라 분석 중심으로 작성하고,\n" +
                "구체적인 근거를 반드시 포함하세요.");

        return sb.toString();
    }
}
