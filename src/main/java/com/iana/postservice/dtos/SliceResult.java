package com.iana.postservice.dtos;

import java.util.List;

public record SliceResult<T>(
        List<T> content,
        int pageNumber,
        boolean hasNext
) {
}
