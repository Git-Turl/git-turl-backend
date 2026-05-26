package root.git_turl.infrastructure.judge;

public record JudgeResult(
        int score,
        Result result,
        String reason
) {
}
