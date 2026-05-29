package root.git_turl.infrastructure.judge;

import java.util.List;

public record JudgeResult(
        List<Deduction> deductions,
        int score,
        Result result,
        String reason
) {}