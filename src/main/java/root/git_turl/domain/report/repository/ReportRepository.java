package root.git_turl.domain.report.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.report.entity.Report;
import root.git_turl.domain.report.enums.GenerationStatus;
import root.git_turl.domain.report.enums.Status;

import java.time.LocalDateTime;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Slice<Report> findReportByMember_IdAndGenerationStatusAndIdLessThanOrderByIdDesc(
            Long memberId, GenerationStatus done, Long id, PageRequest pageRequest
    );
    Slice<Report> findReportByMember_IdAndGenerationStatusOrderByIdDesc(
            Long memberId, GenerationStatus done, PageRequest pageRequest
    );
    Slice<Report> findByMember_IdAndGenerationStatusAndIdLessThanAndCreatedAtBetweenOrderByIdDesc(
            Long memberId,
            GenerationStatus done,
            Long id,
            PageRequest pageRequest,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
    Slice<Report> findByMember_IdAndGenerationStatusAndCreatedAtBetweenOrderByIdDesc(
            Long memberId,
            GenerationStatus done,
            PageRequest pageRequest,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
    Slice<Report> findReportByMember_IdAndGenerationStatusAndStatusAndIdLessThanOrderByIdDesc(
            Long memberId, GenerationStatus done, Status status, Long id, PageRequest pageRequest
    );
    Slice<Report> findReportByMember_IdAndGenerationStatusAndStatusOrderByIdDesc(
            Long memberId, GenerationStatus done, Status status, PageRequest pageRequest
    );
    long countByMemberAndGenerationStatus(Member member, GenerationStatus status);
}
