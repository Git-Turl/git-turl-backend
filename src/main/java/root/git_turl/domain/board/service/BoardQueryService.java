package root.git_turl.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.board.code.BoardErrorCode;
import root.git_turl.domain.board.converter.BoardConverter;
import root.git_turl.domain.board.dto.BoardResDto;
import root.git_turl.domain.board.entity.Board;
import root.git_turl.domain.board.enums.*;
import root.git_turl.domain.board.exception.BoardException;
import root.git_turl.domain.board.repository.BoardLikeRepository;
import root.git_turl.domain.board.repository.BoardRepository;
import root.git_turl.domain.member.entity.Member;

@Service
@RequiredArgsConstructor
public class BoardQueryService {

    private final BoardRepository boardRepository;
    private final BoardConverter boardConverter;
    private final BoardLikeRepository boardLikeRepository;

    @Transactional(readOnly = true)
    public BoardResDto.BoardPreviewListDto getBoardList(
            Integer page,
            BoardType boardType,
            StudyTag studyTag,
            ProjectStatus projectStatus,
            TechStack recruitStack,
            TechStack projectStack,
            PlatformType platformType,
            BoardSortType sort
    ) {
        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<Board> boardPage;

        if (sort == BoardSortType.LIKE) {
            boardPage = boardRepository.findBoardListWithFiltersOrderByLikeCount(
                    boardType,
                    studyTag,
                    projectStatus,
                    recruitStack,
                    projectStack,
                    platformType,
                    pageRequest
            );
        } else {
            boardPage = boardRepository.findBoardListWithFiltersOrderByLatest(
                    boardType,
                    studyTag,
                    projectStatus,
                    recruitStack,
                    projectStack,
                    platformType,
                    pageRequest
            );
        }

        return BoardConverter.toBoardPreviewListDto(
                boardPage,
                boardLikeRepository
        );
    }

    @Transactional
    public BoardResDto.BoardDetailDto getBoardDetail(
            Member currentMember,
            Long boardId
    ) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.BOARD_NOT_FOUND));

        board.increaseViews();

        Long likeCount =
                boardLikeRepository.countByBoard(board);

        Boolean isLiked =
                boardLikeRepository.existsByBoardAndMember(
                        board,
                        currentMember
                );

        return boardConverter.toBoardDetailDto(
                board,
                likeCount,
                isLiked
        );
    }
}