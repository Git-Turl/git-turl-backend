package root.git_turl.domain.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import root.git_turl.domain.board.code.BoardErrorCode;
import root.git_turl.domain.board.converter.BoardConverter;
import root.git_turl.domain.board.dto.BoardReqDto;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.board.exception.BoardException;
import root.git_turl.domain.board.repository.BoardRepository;
import root.git_turl.domain.member.entity.Member;
import root.git_turl.global.aws.AwsFileService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardCommandService {

    private final BoardRepository boardRepository;
    private final BoardConverter boardConverter;
    private final AwsFileService awsFileService;

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
}