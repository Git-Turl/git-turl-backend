package root.git_turl.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import root.git_turl.domain.member.entity.InterestStack;

import java.util.List;

public interface BoardRecommendInterestStackRepository
        extends JpaRepository<InterestStack, Long> {

    List<InterestStack> findAllByMemberId(Long memberId);
}