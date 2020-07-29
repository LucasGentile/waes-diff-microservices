package com.waes.diffservice.service;

import com.waes.diffservice.converter.DiffDataConverter;
import com.waes.diffservice.data.DiffData;
import com.waes.diffservice.enums.DiffType;
import com.waes.diffservice.model.Diff;
import com.waes.diffservice.repository.DiffRepository;
import com.waes.diffservice.validation.DiffServiceValidation;
import javassist.NotFoundException;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Log
@Service
@NoArgsConstructor
public class DiffService {

    private DiffRepository diffRepository;

    @Autowired
    public DiffService(DiffRepository diffRepository) {
        this.diffRepository = diffRepository;
    }

    public DiffData getDiff(Long id) throws NotFoundException {
        Optional<Diff> persistedDiff = diffRepository.findById(id);

        Diff diff = persistedDiff.orElseThrow(() -> new NotFoundException("Diff with id '" + id + "' not found."));

        DiffServiceValidation.validate(id, diff);

        return diffInsight(DiffDataConverter.convert(diff));
    }

    /**
     * Get the offsets where there are differences between LEFT and RIGHT sides from the Diff
     *
     * @param diffData element containing the LEFT and RIGHT side strings the will be diff-ed
     * @return diffData element containing the Diff element that was diff-ed, the ResultType and also a formatted string with the offsets
     */
    private DiffData diffInsight(DiffData diffData) {
        char[] leftSide = diffData.getLeftSide().toCharArray();
        char[] rightSide = diffData.getRightSide().toCharArray();

        if (Arrays.equals(leftSide, rightSide)) {
            diffData.setType(DiffType.EQUAL);
        } else if (leftSide.length != rightSide.length) {
            diffData.setType(DiffType.DIFFERENT_LENGTH);
        } else {
            diffData.setType(DiffType.DIFF);

            List<Integer> diffList = new ArrayList<>();
            for (int offset = 0; offset < leftSide.length; offset++) {
                if (leftSide[offset] != rightSide[offset]) {
                    diffList.add(offset);
                }
            }

            diffData.setInsight(diffResultFormatter(diffList));
        }

        return diffData;
    }

    /**
     * Format the offset output .
     * <p>
     * When it's a single offset in the middle of the string, it will be displayed as a single number:
     * [9]
     * If it's more than one offset in a sequence it will be displayed showing the interval:
     * [3-9]
     * <p>
     * An example of a complete formatted output would be:
     * [1, 3-6, 7]
     *
     * @param diffList list containing all the offsets
     * @return Formatted message
     */
    private String diffResultFormatter(List<Integer> diffList) {
        StringBuilder diffs = new StringBuilder("[");

        for (int offsetPosition = 0; offsetPosition < diffList.size(); offsetPosition++) {

            Integer currentOffset = diffList.get(offsetPosition);
            if (offsetPosition > 0) {
                Integer previousOffset = diffList.get(offsetPosition - 1);
                if ((currentOffset - previousOffset) == 1) {
                    if (diffs.charAt((diffs.length() - 1)) != '-') {
                        if (offsetPosition == (diffList.size() - 1)) {
                            diffs.append("-").append(currentOffset);
                        } else {
                            diffs.append("-");
                        }
                    } else if (offsetPosition == (diffList.size() - 1)) {
                        diffs.append(currentOffset);
                    }
                } else {
                    if (diffs.charAt((diffs.length() - 1)) == '-') {
                        diffs.append(previousOffset).append(", ").append(currentOffset);
                    } else {
                        diffs.append(", ").append(currentOffset);
                    }
                }
            } else {
                diffs.append(currentOffset);
            }
        }
        diffs.append("]");

        return diffs.toString();
    }
}