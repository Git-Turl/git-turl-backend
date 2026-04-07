package root.git_turl.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
