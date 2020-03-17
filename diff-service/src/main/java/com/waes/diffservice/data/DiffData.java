package com.waes.diffservice.data;

import com.waes.diffservice.enums.DiffType;
import lombok.Data;

@Data
public class DiffData {
    private Long id;
    private String leftSide;
    private String rightSide;
    private DiffType type;
    private String insight;
}
