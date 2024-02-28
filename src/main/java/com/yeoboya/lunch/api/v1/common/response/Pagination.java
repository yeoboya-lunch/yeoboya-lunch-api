package com.yeoboya.lunch.api.v1.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class Pagination {
    private final int page;
    private final boolean isFirst;
    private final boolean isLast;
    private final boolean isEmpty;
    private final int totalPages;
    private final long totalElements;
}
