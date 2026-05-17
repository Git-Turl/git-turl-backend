package root.git_turl.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.enums.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface
MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialUid(String socialUid);
    List<Member> findAllByStatusAndDeletedAtBefore(Status status, LocalDateTime standard);
    Boolean existsByNickname(String nickname);
}
