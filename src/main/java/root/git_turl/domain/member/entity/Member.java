package root.git_turl.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import root.git_turl.domain.enums.JobType;
import root.git_turl.domain.enums.Status;
import root.git_turl.global.entity.BaseEntity;

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

    @Column(name = "github_id")
    private String githubId;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "job_type")
    private JobType jobType;

    @Column(name = "status")
    private Status status = Status.ACTIVATE;

}
