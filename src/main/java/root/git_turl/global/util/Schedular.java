package root.git_turl.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.enums.Status;
import root.git_turl.domain.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Schedular {
    private final MemberRepository memberRepository;

    @Transactional
    @Scheduled(cron = "0 16 18 * * *")
    public void deleteInactiveMembers() {

        LocalDateTime standard = LocalDateTime.now().minusDays(7);

        List<Member> members =
                memberRepository
                        .findAllByStatusAndDeletedAtBefore(
                                Status.INACTIVATE,
                                standard
                        );

        for (Member m : members) {
            m.deleteMember();
        }
    }
}
