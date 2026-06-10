package root.git_turl.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import root.git_turl.domain.board.converter.BoardConverter;
import root.git_turl.domain.board.dto.BoardResDto;
import root.git_turl.domain.board.enums.TechStack;
import root.git_turl.domain.board.repository.BoardPreviewProjection;
import root.git_turl.domain.board.repository.BoardRepository;
import root.git_turl.domain.board.repository.BoardRecommendInterestStackRepository;
import root.git_turl.domain.member.entity.InterestStack;
import root.git_turl.domain.member.entity.Member;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardRecommendService {

    private final BoardRepository boardRepository;
    private final BoardConverter boardConverter;
    private final BoardRecommendInterestStackRepository interestStackRepository;

    @Transactional(readOnly = true)
    public List<BoardResDto.RecommendProjectDto> getRecommendedProjects(
            Member member,
            Integer page
    ) {
        PageRequest pageRequest = PageRequest.of(page, 10);

        List<TechStack> matchedStacks =
                interestStackRepository.findAllByMemberId(member.getId()).stream()
                        .map(InterestStack::getTechStack)
                        .flatMap(stack -> convertToBoardTechStacks(stack).stream())
                        .distinct()
                        .toList();

        List<BoardPreviewProjection> boards;

        if (matchedStacks.isEmpty()) {
            boards = boardRepository.findRandomProjects(pageRequest);
        } else {
            boards = boardRepository.findRecommendedProjectsByTechStacks(
                    matchedStacks,
                    pageRequest
            );

            if (boards.isEmpty()) {
                boards = boardRepository.findRandomProjects(pageRequest);
            }
        }

        return boards.stream()
                .map(result -> boardConverter.toRecommendProjectDto(
                        result.getBoard(),
                        result.getLikeCount()
                ))
                .toList();
    }

    private List<root.git_turl.domain.board.enums.TechStack> convertToBoardTechStacks(
            root.git_turl.domain.member.enums.TechStack memberTechStack
    ) {
        return switch (memberTechStack) {

            case NODE_JS -> List.of(
                    root.git_turl.domain.board.enums.TechStack.NODE_JS,
                    root.git_turl.domain.board.enums.TechStack.EXPRESS
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

            case REACT -> List.of(
                    root.git_turl.domain.board.enums.TechStack.REACT,
                    root.git_turl.domain.board.enums.TechStack.JAVASCRIPT,
                    root.git_turl.domain.board.enums.TechStack.TYPESCRIPT
            );

            case VUE_JS -> List.of(
                    root.git_turl.domain.board.enums.TechStack.VUE,
                    root.git_turl.domain.board.enums.TechStack.JAVASCRIPT
            );

            case NEXT_JS -> List.of(
                    root.git_turl.domain.board.enums.TechStack.NEXT_JS,
                    root.git_turl.domain.board.enums.TechStack.REACT,
                    root.git_turl.domain.board.enums.TechStack.TYPESCRIPT
            );

            case JAVASCRIPT -> List.of(
                    root.git_turl.domain.board.enums.TechStack.JAVASCRIPT
            );

            case TYPESCRIPT -> List.of(
                    root.git_turl.domain.board.enums.TechStack.TYPESCRIPT
            );

            case HTML_CSS -> List.of(
                    root.git_turl.domain.board.enums.TechStack.HTML_CSS
            );

            case TAILWIND_CSS -> List.of(
                    root.git_turl.domain.board.enums.TechStack.TAILWIND_CSS
            );

            case MYSQL -> List.of(
                    root.git_turl.domain.board.enums.TechStack.MYSQL
            );

            case POSTGRESQL -> List.of(
                    root.git_turl.domain.board.enums.TechStack.POSTGRESQL
            );

            default -> List.of();
        };
    }
}