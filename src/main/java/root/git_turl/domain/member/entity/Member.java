package root.git_turl.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import root.git_turl.domain.member.enums.JobType;
import root.git_turl.domain.member.enums.Status;
import root.git_turl.domain.member.enums.TechStack;
import root.git_turl.global.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "nickname", length = 20, unique = true)
    private String nickname;

    @Column(name = "github_id", nullable = false )
    private String githubId;

    @Column(name = "social_uid", nullable = false)
    private String socialUid;

    @Column(name = "profile_image", nullable = false)
    private String profileImage;

    @Column(name = "job_type")
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVATE;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterestStack> interestStacks = new ArrayList<>();

    public void addInterestStack(TechStack techStack) {
        InterestStack interestStack = InterestStack.builder()
                .techStack(techStack)
                .build();

        interestStacks.add(interestStack);
        interestStack.assignMember(this);
    }

    public void updateGithubInfo(String githubId, String profileImage, String email) {
        this.githubId = githubId;
        this.profileImage = profileImage;
        this.email = email;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void saveProfile(String nickname, JobType jobType, List<TechStack> techStackList) {
        this.nickname = nickname;
        this.jobType = jobType;
        if (!techStackList.isEmpty()) {
            for (TechStack techStack : techStackList) {
                addInterestStack(techStack);
            }
        }
    }
}
