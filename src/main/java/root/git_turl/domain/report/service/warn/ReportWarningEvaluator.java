package root.git_turl.domain.report.service.warn;

import org.springframework.stereotype.Service;
import root.git_turl.domain.report.dto.GitAnalysisResult;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportWarningEvaluator {

    private static final int MIN_COMMIT_THRESHOLD = 5;
    private static final int MIN_FILE_THRESHOLD = 10;

    public List<String> evaluate(GitAnalysisResult result, boolean emailConfirmed) {
        List<String> warnings = new ArrayList<>();

        if (result.getUserTotalCommits() < MIN_COMMIT_THRESHOLD) {
            warnings.add(String.format(
                    "커밋 수가 %d개로 적어 분석 정확도가 낮을 수 있습니다.",
                    result.getUserTotalCommits()
            ));
        }

        if (result.getReadmeSummary() == null || result.getReadmeSummary().isBlank()) {
            warnings.add("README가 없어 프로젝트 목적 파악이 부정확할 수 있습니다.");
        }

        if (!emailConfirmed) {
            warnings.add("GitHub 커밋 이메일이 확인되지 않아 기여도 계산이 부정확할 수 있습니다. 설정에서 이메일을 등록해주세요.");
        }

        if (result.getUserRepresentativeFiles().size() < MIN_FILE_THRESHOLD) {
            warnings.add("분석 가능한 파일 수가 적어 개선사항 분석이 제한적일 수 있습니다.");
        }

        return warnings;
    }
}