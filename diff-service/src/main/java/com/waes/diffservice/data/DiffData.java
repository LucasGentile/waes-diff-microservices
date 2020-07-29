package com.waes.diffservice.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.waes.diffservice.enums.DiffType;
import lombok.Data;

import java.util.LinkedHashSet;

@Data
public class DiffData {
    private Long id;
    private String leftSide;
    private String rightSide;
    private DiffType type;
    private LinkedHashSet<DiffInsightData> insights;

    @JsonIgnore
    public String getInsightsAsString() {
        StringBuilder insightsAsString = new StringBuilder("[");
        this.insights.forEach(diffInsightData -> {
            if (diffInsightData.getSize() == 1) {
                insightsAsString.append(diffInsightData.getOffset());
            } else if (diffInsightData.getSize() > 0) {
                insightsAsString.append(diffInsightData.getOffset()).append("-").append((diffInsightData.getOffset() + (diffInsightData.getSize() - 1)));
            } else {
                insightsAsString.append(diffInsightData.getOffset());
            }
            insightsAsString.append(", ");
        });
        insightsAsString.append("]");
        return insightsAsString.toString().replace(", ]", "]");
    }
}
