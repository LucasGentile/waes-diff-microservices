package com.waes.apigateway.data;

import com.waes.apigateway.enums.DiffType;
import lombok.Data;

import java.util.LinkedHashSet;

@Data
public class Diff {
    private Long id;
    private String leftSide;
    private String rightSide;
    private DiffType type;
    private LinkedHashSet<DiffInsight> insights;
}
