package root.git_turl.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialUid(String socialUid);
}
