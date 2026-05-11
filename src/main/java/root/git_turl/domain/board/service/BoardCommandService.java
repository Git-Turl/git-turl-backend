package root.git_turl.domain.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import root.git_turl.domain.board.code.BoardErrorCode;
import root.git_turl.domain.board.converter.BoardConverter;
import root.git_turl.domain.board.dto.BoardReqDto;
import root.git_turl.domain.board.dto.BoardResDto;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.board.exception.BoardException;
import root.git_turl.domain.board.repository.BoardRepository;
import root.git_turl.domain.member.code.MemberErrorCode;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.domain.member.exception.MemberException;
import root.git_turl.domain.member.repository.MemberRepository;
import root.git_turl.global.aws.AwsFileService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BoardCommandService {

    private final BoardRepository boardRepository;
    private final BoardConverter boardConverter;
    private final MemberRepository memberRepository;
    private final AwsFileService awsFileService;

    @Transactional
    public Board createBoard(BoardReqDto.CreateBoardReqDto request,
                             Member member,
                             MultipartFile image
                             ) {
        String imageUrl = null;
        try {
            imageUrl = awsFileService.uploadBoardImg(image);
        } catch (IOException e) {
            throw new BoardException(BoardErrorCode.IMAGE_UPLOAD_FAIL);
        }
        Board board = boardConverter.toBoard(request, member, imageUrl);
        return boardRepository.save(board);
    }

    @Transactional
    public BoardResDto.BoardCreateResultDto createBoard(
            Member currentMember,
            BoardReqDto.CreateBoardReqDto request
    ) {
        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND));

        String imageUrl = null;

        if (request.getBoardImage() != null) {
            try {
                imageUrl = awsFileService.uploadBoardImg(
                        request.getBoardImage()
                );
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
            BoardReqDto.UpdateDto request
    ) {
        if (request.getTitle() == null
                && request.getContent() == null
                && request.getBoardType() == null
                && request.getBoardImage() == null) {
            throw new BoardException(BoardErrorCode.NO_EDIT);
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        validateWriter(board, currentMember);

        String imageUrl = board.getImageUrl();

        if (request.getBoardImage() != null) {
            try {
                imageUrl = awsFileService.uploadBoardImg(
                        request.getBoardImage()
                );
            } catch (IOException e) {
                throw new BoardException(BoardErrorCode.IMAGE_UPLOAD_FAIL);
            }
        }

        board.update(
                request.getTitle(),
                request.getContent(),
                imageUrl,
                request.getBoardType()
        );

        return BoardConverter.toUpdateResultDto(board);
    }

    @Transactional
    public BoardResDto.BoardDeleteResultDto deleteBoard(Member currentMember, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        validateWriter(board, currentMember);

        boardRepository.delete(board);

        return BoardConverter.toDeleteResultDto(boardId);
    }

    private void validateWriter(Board board, Member currentMember) {
        if (!board.getMember().getId().equals(currentMember.getId())) {
            throw new BoardException(BoardErrorCode.NO_BOARD_PERMISSION);
        }
    }
}