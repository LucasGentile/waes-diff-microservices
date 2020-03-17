package com.waes.apigateway.data;

import com.waes.apigateway.enums.DiffType;
import lombok.Data;

@Data
public class Diff {
    private Long id;
    private String leftSide;
    private String rightSide;
    private DiffType type;
    private String insight;
}
