package root.git_turl.domain.question.converter;

import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.report.entity.Report;

public class QuestionConverter {

    public static Question toQuestion(
        Report report,
        Member member

    ) {
        return Question.builder()
                .report(report)
                .member(member)
                .build();
    }
}
