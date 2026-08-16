package root.git_turl.domain.report.dto;

import lombok.Builder;
import lombok.Getter;
import root.git_turl.domain.report.dto.commit.CommitTypeCount;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class GitAnalysisResult {

    private int totalCommits;
    private int userTotalCommits;
    private double contributionRate;

    private CommitTypeCount commitTypeCount;
    private Integer totalFileCount;

    private List<String> sampleMessages;
    private Map<String, Long> contributionAnalyze;

    private String readmeSummary;
    private List<RepresentativeFile> projectRepresentativeFiles;
    private List<RepresentativeFile> userRepresentativeFiles;
}