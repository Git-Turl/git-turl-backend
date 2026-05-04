package root.git_turl.domain.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.member.code.MemberErrorCode;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.exception.MemberException;
import root.git_turl.domain.member.repository.MemberRepository;
import root.git_turl.domain.question.converter.QuestionConverter;
import root.git_turl.domain.question.dto.QuestionContent;
import root.git_turl.domain.question.dto.QuestionReqDto;
import root.git_turl.domain.question.dto.QuestionResDto;
import root.git_turl.domain.question.entity.Question;
import root.git_turl.domain.question.exception.QuestionException;
import root.git_turl.domain.question.exception.code.QuestionErrorCode;
import root.git_turl.domain.question.repository.QuestionRepository;
import root.git_turl.domain.report.code.ReportErrorCode;
import root.git_turl.domain.report.dto.ReportResDto;
import root.git_turl.domain.report.entity.Report;
import root.git_turl.domain.report.exception.ReportException;
import root.git_turl.domain.report.repository.ReportRepository;
import root.git_turl.global.util.Pagination;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AsyncQuestionService asyncQuestionService;

    @Transactional
    public void saveQuestion(Member currentMember, Long reportId, QuestionReqDto.QuestionCount dto) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException(ReportErrorCode.REPORT_NOT_FOUND));
        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        List<Question> questions = new ArrayList<>();
        for (int i=0; i<dto.getQuestionCount(); i++) {
            Question question = QuestionConverter.toQuestion(report, member);
            questions.add(question);
        }
        questionRepository.saveAll(questions);
        asyncQuestionService.makeQuestion(reportId, dto.getQuestionCount(), questions.stream().map(Question::getId).toList());
    }

    public ReportResDto.Pagination<QuestionResDto.QuestionInfo> getQuestionList(
            Long reportId,
            Integer pageSize,
            String cursor
    ) {
        if (pageSize == null) pageSize = 10;
        if (cursor == null) cursor = "-1";

        PageRequest pageRequest = PageRequest.of(0, pageSize);

        long idCursor;
        Slice<Question> questionList;
        String nextCursor;

        if (!cursor.equals("-1")) {
            idCursor = Long.parseLong(cursor);
            questionList = questionRepository.findQuestionByReport_IdAndIdLessThanOrderByIdDesc(reportId, idCursor, pageRequest);
        } else {
            questionList = questionRepository.findQuestionByReport_IdOrderByIdDesc(reportId, pageRequest);
        }

        nextCursor = questionList.getContent().getLast().getId().toString();

        return Pagination.toPagination(
                questionList
                        .stream()
                        .map(QuestionConverter::toQuestionInfo)
                        .toList(),
                questionList.hasNext(),
                nextCursor,
                questionList.getSize()
        );
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionException(QuestionErrorCode.NOT_FOUND));

        questionRepository.delete(question);
    }
}
