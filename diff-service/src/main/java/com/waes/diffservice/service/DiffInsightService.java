package com.waes.diffservice.service;

import com.waes.diffservice.data.DiffData;
import com.waes.diffservice.data.DiffInsightData;
import com.waes.diffservice.enums.DiffType;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.*;

@Log
@Service
@NoArgsConstructor
public class DiffInsightService {

    /**
     * Get the offsets where there are differences between LEFT and RIGHT sides from the Diff
     *
     * @param diffData element containing the LEFT and RIGHT side strings the will be diff-ed
     * @return diffData element containing the Diff element that was diff-ed, the ResultType and also a formatted string with the offsets
     */
    protected DiffData provideDiffInsight(DiffData diffData) {
        char[] leftSideCharArray = diffData.getLeftSide().toCharArray();
        char[] rightSideCharArray = diffData.getRightSide().toCharArray();

        diffData.setType(defineDiffType(leftSideCharArray, rightSideCharArray));

        if (DiffType.DIFF.equals(diffData.getType())) {
            diffData.setInsights(getDiffOffsetSizes(leftSideCharArray, rightSideCharArray));
        }

        return diffData;
    }

    private LinkedHashSet<DiffInsightData> getDiffOffsetSizes(char[] leftSideCharArray, char[] rightSideCharArray) {
        Map<Integer, Integer> offsetSizeMap = new HashMap<>();
        for (int offset = 0, currentOffset = 0; offset < leftSideCharArray.length; offset++) {
            if (leftSideCharArray[offset] != rightSideCharArray[offset]) {
                if (!offsetSizeMap.containsKey(currentOffset)) {
                    currentOffset = offset;
                }
                offsetSizeMap.merge(currentOffset, 1, Integer::sum);
            } else {
                currentOffset = offset;
            }
        }

        LinkedHashSet<DiffInsightData> insights = new LinkedHashSet<>();
        offsetSizeMap.forEach((offset, size) -> insights.add(new DiffInsightData(offset, size)));

        return insights;
    }

    private DiffType defineDiffType(char[] leftSide, char[] rightSide) {
        DiffType diffType;
        if (Arrays.equals(leftSide, rightSide)) {
            diffType = DiffType.EQUAL;
        } else if (leftSide.length != rightSide.length) {
            diffType = DiffType.DIFFERENT_LENGTH;
        } else {
            diffType = DiffType.DIFF;
        }
        return diffType;
    }
}