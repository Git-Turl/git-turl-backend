package root.git_turl.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.board.converter.BoardConverter;
import root.git_turl.domain.board.dto.BoardResDto;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.board.enums.BoardType;
import root.git_turl.domain.board.repository.BoardRepository;

@Service
@RequiredArgsConstructor
public class BoardQueryService {

    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public BoardResDto.BoardPreviewListDto getBoardList(Integer page, BoardType boardType) {
        PageRequest pageRequest = PageRequest.of(
                page,
                10,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Board> boardPage = boardType == null
                ? boardRepository.findAll(pageRequest)
                : boardRepository.findAllByBoardType(boardType, pageRequest);

        return BoardConverter.toBoardPreviewListDto(boardPage);
    }
}
