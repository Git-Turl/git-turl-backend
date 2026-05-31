package root.git_turl.domain.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import root.git_turl.domain.board.code.BoardErrorCode;
import root.git_turl.domain.board.converter.BoardConverter;
import root.git_turl.domain.board.dto.BoardReqDto;
import root.git_turl.domain.board.dto.BoardResDto;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.board.enums.BoardType;
import root.git_turl.domain.board.enums.StudyTag;
import root.git_turl.domain.board.exception.BoardException;
import root.git_turl.domain.board.repository.BoardLikeRepository;
import root.git_turl.domain.board.repository.BoardRepository;
import root.git_turl.domain.comment.repository.CommentLikeRepository;
import root.git_turl.domain.comment.repository.CommentRepository;
import root.git_turl.domain.member.code.MemberErrorCode;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.exception.MemberException;
import root.git_turl.domain.member.repository.MemberRepository;
import root.git_turl.infrastructure.aws.AwsFileService;


import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BoardCommandService {

    private final BoardRepository boardRepository;
    private final BoardConverter boardConverter;
    private final MemberRepository memberRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final AwsFileService awsFileService;

    @Transactional
    public BoardResDto.BoardCreateResultDto createBoard(
            Member currentMember,
            BoardReqDto.CreateBoardReqDto request,
            MultipartFile image
    ) {
        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        validateCreateRequest(request);

        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            try {
                imageUrl = awsFileService.uploadBoardImg(image);
            } catch (IOException e) {
                throw new BoardException(BoardErrorCode.IMAGE_UPLOAD_FAIL);
            }
        }

        Board board = boardConverter.toBoard(request, member, imageUrl);
        boardRepository.save(board);

        return BoardConverter.toCreateResultDto(board);
    }

    @Transactional
    public BoardResDto.BoardUpdateResultDto updateBoard(
            Member currentMember,
            Long boardId,
            BoardReqDto.UpdateDto request,
            MultipartFile image
    ) {
        if (request.getTitle() == null
                && request.getContent() == null
                && request.getBoardType() == null
                && request.getStudyTag() == null
                && request.getProjectStatus() == null
                && request.getRecruitStacks() == null
                && request.getProjectStacks() == null
                && request.getPlatformTypes() == null
                && request.getRecruitCount() == null
                && request.getRecruitDeadline() == null
                && request.getCertificateType() == null
                && (image == null || image.isEmpty())) {
            throw new BoardException(BoardErrorCode.NO_EDIT);
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        validateWriter(board, currentMember);
        validateUpdateRequest(board, request);

        String imageUrl = board.getImageUrl();

        if (image != null && !image.isEmpty()) {
            try {
                imageUrl = awsFileService.uploadBoardImg(image);
            } catch (IOException e) {
                throw new BoardException(BoardErrorCode.IMAGE_UPLOAD_FAIL);
            }
        }

        BoardType boardType = request.getBoardType() != null
                ? request.getBoardType()
                : board.getBoardType();

        board.update(
                request.getTitle(),
                request.getContent(),
                imageUrl,
                boardType,

                // study
                boardType == BoardType.STUDY ? request.getStudyTag() : null,

                // project
                boardType == BoardType.PROJECT ? request.getProjectStatus() : null,

                boardType == BoardType.PROJECT ? request.getPlatformTypes() : null,

                // common
                request.getRecruitCount(),
                request.getRecruitDeadline(),

                // certificate
                boardType == BoardType.STUDY
                        && request.getStudyTag() == StudyTag.CERTIFICATE
                        ? request.getCertificateType()
                        : null,

                // recruit stacks
                boardType == BoardType.PROJECT
                        ? request.getRecruitStacks()
                        : null,

                // project stacks
                boardType == BoardType.PROJECT
                        ? request.getProjectStacks()
                        : null
        );

        return BoardConverter.toUpdateResultDto(board);
    }

    @Transactional
    public BoardResDto.BoardDeleteResultDto deleteBoard(
            Member currentMember,
            Long boardId
    ) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        validateWriter(board, currentMember);

        commentLikeRepository.deleteAllByCommentBoard(board);
        commentRepository.deleteAllByBoard(board);
        boardLikeRepository.deleteAllByBoard(board);

        boardRepository.delete(board);

        return BoardConverter.toDeleteResultDto(boardId);
    }

    private void validateCreateRequest(BoardReqDto.CreateBoardReqDto request) {
        if (request.getBoardType() == BoardType.STUDY) {
            if (request.getStudyTag() == null) {
                throw new BoardException(BoardErrorCode.INVALID_BOARD_TYPE);
            }
        }

        if (request.getBoardType() == BoardType.PROJECT) {
            if (request.getProjectStatus() == null
                    || request.getRecruitStacks() == null || request.getRecruitStacks().isEmpty()
                    || request.getProjectStacks() == null || request.getProjectStacks().isEmpty()
                    || request.getPlatformTypes() == null || request.getPlatformTypes().isEmpty()) {

                throw new BoardException(BoardErrorCode.INVALID_BOARD_TYPE);
            }
        }
    }

    private void validateUpdateRequest(Board board, BoardReqDto.UpdateDto request) {
        BoardType boardType = request.getBoardType() != null
                ? request.getBoardType()
                : board.getBoardType();

        if (boardType == BoardType.STUDY) {
            if (request.getStudyTag() == null && board.getStudyTag() == null) {
                throw new BoardException(BoardErrorCode.INVALID_BOARD_TYPE);
            }
        }

        if (boardType == BoardType.PROJECT) {
            if ((request.getProjectStatus() == null && board.getProjectStatus() == null)

                    || (request.getRecruitStacks() == null
                    && (board.getRecruitStacks() == null || board.getRecruitStacks().isEmpty()))

                    || (request.getProjectStacks() == null
                    && (board.getProjectStacks() == null || board.getProjectStacks().isEmpty()))

                    || (request.getPlatformTypes() == null
                    && (board.getPlatformTypes() == null || board.getPlatformTypes().isEmpty()))) {

                throw new BoardException(BoardErrorCode.INVALID_BOARD_TYPE);
            }
        }
    }

    private void validateWriter(Board board, Member currentMember) {
        if (!board.getMember().getId().equals(currentMember.getId())) {
            throw new BoardException(BoardErrorCode.NO_BOARD_PERMISSION);
        }
    }
}