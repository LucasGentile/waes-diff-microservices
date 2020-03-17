package com.waes.diffservice.service;

import com.waes.diffservice.converter.DiffDataConverter;
import com.waes.diffservice.data.DiffData;
import com.waes.diffservice.enums.DiffType;
import com.waes.diffservice.model.Diff;
import com.waes.diffservice.repository.DiffRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class DiffService {

    @Autowired
    private DiffRepository diffRepository;

    /**
     * @param id       Diff id
     * @param diffData Diff object to be saved
     * @return Saved Diff data object
     */
    @Transactional
    public DiffData save(Long id, DiffData diffData) {
        Diff diff = diffRepository.findById(id).orElse(null);

        if (diff == null) {
            diff = new Diff();
            diff.setId(id);
        }

        if (diffData.getLeftSide() != null) {
            diff.setLeftSide(diffData.getLeftSide());
        } else if (diffData.getRightSide() != null) {
            diff.setRightSide(diffData.getRightSide());
        }

        return DiffDataConverter.convert(diffRepository.save(diff));
    }

    public DiffData getDiff(Long id) throws NotFoundException {
        Diff diff = diffRepository.findById(id).orElse(null);

        if (diff == null) {
            throw new NotFoundException("DiffData with id " + id + " not found.");
        } else {
            if (diff.getLeftSide() == null) {
                throw new NotFoundException("DiffData with id " + id + " missing LEFT side.");
            }
            if (diff.getRightSide() == null) {
                throw new NotFoundException("DiffData with id " + id + " missing RIGHT side.");
            }

            return diffInsight(DiffDataConverter.convert(diff));
        }
    }

    public Collection<DiffData> getAll() {
        return diffRepository.findAll().stream().map(DiffDataConverter::convert).collect(Collectors.toList());
    }

    public String encodeBase64JsonData(String json) {
        byte[] bytes = json.getBytes();
        String jsonBase64Encoded = DatatypeConverter.printBase64Binary(bytes);
        log.info("Encoded Json:\n" + jsonBase64Encoded + "\n");
        return jsonBase64Encoded;
    }

    public String decodeBase64JsonData(String base64JsonEncoded) {
        byte[] base64Decoded = DatatypeConverter.parseBase64Binary(base64JsonEncoded);
        String base64DecodedString = new String(base64Decoded);
        log.info("Decoded Json:\n" + base64DecodedString + "\n");
        return base64DecodedString;
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