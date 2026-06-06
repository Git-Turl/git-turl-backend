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
import root.git_turl.domain.board.repository.BoardPreviewProjection;
import root.git_turl.domain.board.repository.BoardRepository;
import root.git_turl.domain.member.entity.Member;

import java.util.List;

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

        Page<BoardPreviewProjection> boardPage;

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

        return BoardConverter.toBoardPreviewListDto(boardPage);
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

    @Transactional(readOnly = true)
    public BoardResDto.BoardPreviewListDto getMyBoards(Member member, Integer page) {
        return getMemberBoards(member.getId(), page);
    }

    @Transactional(readOnly = true)
    public BoardResDto.BoardPreviewListDto getMemberBoards(Long memberId, Integer page) {
        Page<BoardPreviewProjection> boardPage =
                boardRepository.findBoardsByMemberIdOrderByLatest(
                        memberId,
                        PageRequest.of(page, 10)
                );

        return BoardConverter.toBoardPreviewListDto(boardPage);
    }

    @Transactional(readOnly = true)
    public List<BoardResDto.RecommendProjectDto> getRecommendProjects(Member member) {

        List<root.git_turl.domain.board.enums.TechStack> boardTechStacks =
                convertToBoardTechStacks(member);

        List<Board> boards;

        if (boardTechStacks.isEmpty()) {
            boards = boardRepository.findRecommendProjectsAll(PageRequest.of(0, 3));
        } else {
            boards = boardRepository.findRecommendProjectsByTechStacks(
                    boardTechStacks,
                    PageRequest.of(0, 3)
            );
        }

        return boards.stream()
                .map(board -> boardConverter.toRecommendProjectDto(
                        board,
                        boardLikeRepository.countByBoard(board)
                ))
                .toList();
    }

    private List<root.git_turl.domain.board.enums.TechStack> convertToBoardTechStacks(Member member) {
        return member.getInterestStacks()
                .stream()
                .flatMap(interestStack ->
                        convertToBoardTechStacks(interestStack.getTechStack()).stream()
                )
                .distinct()
                .toList();
    }

    private List<root.git_turl.domain.board.enums.TechStack> convertToBoardTechStacks(
            root.git_turl.domain.member.enums.TechStack memberTechStack
    ) {
        return switch (memberTechStack) {
            case PHP -> List.of(
                    root.git_turl.domain.board.enums.TechStack.MYSQL,
                    root.git_turl.domain.board.enums.TechStack.POSTGRESQL
            );

            case NODE_JS -> List.of(
                    root.git_turl.domain.board.enums.TechStack.NODE_JS,
                    root.git_turl.domain.board.enums.TechStack.EXPRESS,
                    root.git_turl.domain.board.enums.TechStack.JAVASCRIPT
            );

            case NEST_JS -> List.of(
                    root.git_turl.domain.board.enums.TechStack.NODE_JS,
                    root.git_turl.domain.board.enums.TechStack.EXPRESS,
                    root.git_turl.domain.board.enums.TechStack.TYPESCRIPT
            );

            case SPRING_BOOT -> List.of(
                    root.git_turl.domain.board.enums.TechStack.SPRING_BOOT,
                    root.git_turl.domain.board.enums.TechStack.SPRING,
                    root.git_turl.domain.board.enums.TechStack.JAVA
            );

            case DJANGO -> List.of(
                    root.git_turl.domain.board.enums.TechStack.MYSQL,
                    root.git_turl.domain.board.enums.TechStack.POSTGRESQL
            );

            case REACT -> List.of(
                    root.git_turl.domain.board.enums.TechStack.REACT,
                    root.git_turl.domain.board.enums.TechStack.JAVASCRIPT,
                    root.git_turl.domain.board.enums.TechStack.TYPESCRIPT
            );

            case TYPESCRIPT -> List.of(
                    root.git_turl.domain.board.enums.TechStack.TYPESCRIPT,
                    root.git_turl.domain.board.enums.TechStack.JAVASCRIPT,
                    root.git_turl.domain.board.enums.TechStack.REACT,
                    root.git_turl.domain.board.enums.TechStack.NEXT_JS
            );

            case KOTLIN -> List.of(
                    root.git_turl.domain.board.enums.TechStack.JAVA,
                    root.git_turl.domain.board.enums.TechStack.SPRING_BOOT
            );

            case SWIFT -> List.of(
                    root.git_turl.domain.board.enums.TechStack.REACT,
                    root.git_turl.domain.board.enums.TechStack.JAVASCRIPT
            );

            case JAVASCRIPT -> List.of(
                    root.git_turl.domain.board.enums.TechStack.JAVASCRIPT,
                    root.git_turl.domain.board.enums.TechStack.REACT,
                    root.git_turl.domain.board.enums.TechStack.NODE_JS,
                    root.git_turl.domain.board.enums.TechStack.EXPRESS
            );

            case ETC -> List.of(
                    root.git_turl.domain.board.enums.TechStack.JAVASCRIPT,
                    root.git_turl.domain.board.enums.TechStack.JAVA,
                    root.git_turl.domain.board.enums.TechStack.NODE_JS
            );
        };
    }
}