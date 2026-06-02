package root.git_turl.domain.board.repository;

import root.git_turl.domain.board.entity.Board;

public interface BoardPreviewProjection {
    Board getBoard();
    Long getLikeCount();
}
