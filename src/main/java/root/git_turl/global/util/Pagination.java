package root.git_turl.global.util;

import root.git_turl.domain.report.dto.ReportResDto;

import java.util.List;


public class Pagination {
    public static <T> ReportResDto.Pagination<T> toPagination(
            List<T> data,
            Boolean hasNext,
            String nextCursor,
            Integer pageSize
    ) {
        return ReportResDto.Pagination.<T>builder()
                .data(data)
                .hasNext(hasNext)
                .nextCursor(nextCursor)
                .pageSize(pageSize)
                .build();
    }
}
