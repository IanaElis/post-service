package com.iana.postservice.dtos;

import java.time.Instant;
import java.util.List;

public record SliceResult<T>(
        List<T> content,
       // int pageNumber,
        Instant cursor,
        boolean hasNext
) {
}
