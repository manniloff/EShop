package com.amdaris.mentoring.core.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageView {
    private int pageSize = 10;

    private int page = 0;

    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sort = "id";
}
