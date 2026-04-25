package root.git_turl.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.report.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
