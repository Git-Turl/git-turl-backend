package root.git_turl.domain.report.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.report.entity.Report;

import java.time.LocalDateTime;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Slice<Report> findReportByMember_IdAndIdLessThanOrderByIdDesc(Long memberId, Long id, PageRequest pageRequest);
    Slice<Report> findReportByMember_IdOrderByIdDesc(Long memberId, PageRequest pageRequest);
    Slice<Report> findByMember_IdAndIdLessThanAndCreatedAtBetweenOrderByIdDesc(
            Long memberId,
            Long id,
            PageRequest pageRequest,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
    Slice<Report> findByMember_IdAndCreatedAtBetweenOrderByIdDesc(
            Long memberId,
            PageRequest pageRequest,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
