package root.git_turl.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;
import root.git_turl.domain.member.enums.JobType;
import root.git_turl.domain.member.enums.Status;
import root.git_turl.domain.member.enums.TechStack;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.report.entity.Report;
import root.git_turl.global.entity.BaseEntity;

import java.time.LocalDateTime;
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

    @Setter
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "nickname", length = 20, unique = true)
    private String nickname;

    @Column(name = "github_name")
    private String githubName;

    @Column(name = "github_id")
    private String githubId;

    @Column(name = "social_uid")
    private String socialUid;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "job_type")
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Builder.Default
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVATE;

    @Column(name = "github_access_token")
    private String githubAccessToken;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterestStack> interestStacks = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshToken refreshToken;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    public void addInterestStack(TechStack techStack) {
        InterestStack interestStack = InterestStack.builder()
                .techStack(techStack)
                .build();

        interestStacks.add(interestStack);
        interestStack.assignMember(this);
    }

    public void updateGithubInfo(String githubId, String githubName, String email) {
        this.githubId = githubId;
        this.githubName = githubName;
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

    public void updateProfile(String nickname, JobType jobType, List<TechStack> techStackList) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (jobType != null) {
            this.jobType = jobType;
        }
        if (techStackList != null) {
            this.interestStacks.clear();

            for (TechStack techStack : techStackList) {
                addInterestStack(techStack);
            }
        }
    }

    public void updateGithubToken(String token) {
        this.githubAccessToken = token;
    }

    public void activateMember() {
        this.status = Status.ACTIVATE;
    }

    public void inactivateMember() {
        this.status = Status.INACTIVATE;
        this.deletedAt = LocalDateTime.now();
    }

    public void deleteMember() {
        this.status = Status.DELETED;
        this.nickname = "탈퇴한 사용자"+id;
        this.socialUid = null;
        this.email = null;
        this.profileImage = null;
        this.githubId = null;
        this.githubName = null;
        interestStacks.clear();
        reports.clear();
        this.refreshToken = null;
    }
}