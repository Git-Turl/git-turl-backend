package root.git_turl.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expire_date", nullable = false)
    private LocalDateTime expireDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public RefreshToken(String token, Member member, LocalDateTime expireDate) {
        this.token = token;
        this.member = member;
        this.expireDate = expireDate;
    }

    public void update(String newToken, LocalDateTime newExpireDate) {
        this.token = newToken;
        this.expireDate = newExpireDate;
    }
}