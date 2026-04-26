package root.git_turl.domain.report.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import root.git_turl.domain.report.dto.GithubRepoResponse;
import root.git_turl.domain.report.dto.ReportReqDto;
import root.git_turl.domain.report.dto.ReportResDto;
import root.git_turl.domain.report.dto.reportDetail.ReportWrapper;
import root.git_turl.domain.report.entity.Report;
import root.git_turl.domain.report.enums.GenerationStatus;
import root.git_turl.domain.report.enums.Status;

public class ReportConverter {

    public static ReportResDto.RepoInfo toRepoRes(
            GithubRepoResponse repos
    ) {
        return ReportResDto.RepoInfo.builder()
                .name(repos.getName())
                .fullName(repos.getFullName())
                .description(repos.getDescription())
                .isPrivate(repos.isPrivate())
                .build();
    }

    public static Report toReport(
            String githubId,
            String fullName
    ) {
        return Report.builder()
                .githubId(githubId)
                .status(Status.PRIVATE)
                .generationStatus(GenerationStatus.PROCESSING)
                .repoName(fullName)
                .build();
    }


    public static ReportResDto.ReportId toReportId(
            Report report
    ) {
        return ReportResDto.ReportId.builder()
                .reportId(report.getId())
                .build();
    }

    public static ReportResDto.ReportDetail toReportDetail(
            Report report
    ) {
        return ReportResDto.ReportDetail.builder()
                .reportId(report.getId())
                .status(report.getStatus())
                .repoName(report.getRepoName())
                .createdAt(report.getCreatedAt())
                .githubId(report.getGithubId())
                .content(toReportWrapper(report.getContentJson()))
                .build();
    }

    public static ReportWrapper toReportWrapper(
            String contentJson
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(contentJson, ReportWrapper.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }

    public static ReportResDto.NewStatus toNewStatus(
            Status status
    ) {
        return ReportResDto.NewStatus.builder()
                .status(status)
                .build();
    }

    public static ReportResDto.ReportPreview toReportPreview(
            Report report
    ) {
        return ReportResDto.ReportPreview.builder()
                .reportId(report.getId())
                .reopName(report.getRepoName())
                .description(report.getDescription())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
