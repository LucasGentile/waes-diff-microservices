package com.waes.diffservice.service;

import com.waes.diffservice.data.DiffData;
import com.waes.diffservice.enums.DiffType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static com.waes.diffservice.utils.TestUtils.*;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test classes naming pattern:
 * methodCalled_input_output
 */
@RunWith(MockitoJUnitRunner.class)
public class DiffInsightServiceTest {

    @InjectMocks
    private DiffInsightService diffInsightService;

    @Test
    public void executeGetDiff_DiffEqualSides_DiffTypeEqual() {
        Long diffId = getRandomId();

        DiffData diffData = createDiffData(diffId, encodeBase64String("equal_string_to_be_diff-ed"), encodeBase64String("equal_string_to_be_diff-ed"));

        DiffData diffDataWithInsight = diffInsightService.provideDiffInsight(diffData);

        assertEquals(diffData.getLeftSide(), diffDataWithInsight.getLeftSide());
        assertEquals(diffData.getRightSide(), diffDataWithInsight.getRightSide());
        assertEquals(diffDataWithInsight.getType(), DiffType.EQUAL);
        assertNull(diffData.getInsights());
    }

    @Test
    public void executeGetDiff_DiffSidesDifferentLength_DiffTypeDifferentLength() {
        Long diffId = getRandomId();

        DiffData diffData = createDiffData(diffId, encodeBase64String("equal_string_to_be_diff-ed"), encodeBase64String("different_length_string_to_be_diff-ed"));

        DiffData diffDataWithInsight = diffInsightService.provideDiffInsight(diffData);

        assertEquals(diffData.getLeftSide(), diffDataWithInsight.getLeftSide());
        assertEquals(diffData.getRightSide(), diffDataWithInsight.getRightSide());
        assertEquals(diffDataWithInsight.getType(), DiffType.DIFFERENT_LENGTH);
        assertNull(diffData.getInsights());
    }

    @Test
    public void executeGetDiff_DiffOffsetLengthAtFirstChars_DiffTypeDiff() {
        Long diffId = getRandomId();

        DiffData diffData = createDiffData(diffId, encodeBase64String("string_to_be_diff-ed"), encodeBase64String("diffng_to_be_diff-ed"));

        DiffData diffDataWithInsight = diffInsightService.provideDiffInsight(diffData);

        assertEquals(diffData.getLeftSide(), diffDataWithInsight.getLeftSide());
        assertEquals(diffData.getRightSide(), diffDataWithInsight.getRightSide());
        assertEquals(DiffType.DIFF, diffDataWithInsight.getType());
        assertEquals("[0-5]", diffDataWithInsight.getInsightsAsString());
    }

    @Test
    public void executeGetDiff_DiffOffsetLengthAtLastChars_DiffTypeDiff() {
        Long diffId = getRandomId();

        DiffData diffData = createDiffData(diffId, encodeBase64String("string_to_be_diff-ed"), encodeBase64String("string_to_be_difdiff"));

        DiffData diffDataWithInsight = diffInsightService.provideDiffInsight(diffData);

        assertEquals(diffData.getLeftSide(), diffDataWithInsight.getLeftSide());
        assertEquals(diffData.getRightSide(), diffDataWithInsight.getRightSide());
        assertEquals(diffDataWithInsight.getType(), DiffType.DIFF);
        assertEquals("[22-23, 25-26]", diffDataWithInsight.getInsightsAsString());
    }

    @Test
    public void executeGetDiff_DiffDataOffsetLengthAllChars_DiffTypeDiff() {
        Long diffId = getRandomId();

        DiffData diffData = createDiffData(diffId, encodeBase64String("string_to_be_diff-ed"), encodeBase64String("12345678910987654321"));

        DiffData diffDataWithInsight = diffInsightService.provideDiffInsight(diffData);

        assertEquals(diffData.getLeftSide(), diffDataWithInsight.getLeftSide());
        assertEquals(diffData.getRightSide(), diffDataWithInsight.getRightSide());
        assertEquals(diffDataWithInsight.getType(), DiffType.DIFF);
        assertEquals("[0-26]", diffData.getInsightsAsString());
    }

    @Test
    public void executeGetDiff_DiffDataSingleOffsetAtFirstChar_DiffTypeDiff() {
        Long diffId = getRandomId();

        DiffData diffData = createDiffData(diffId, encodeBase64String("string_to_be_diff-ed"), encodeBase64String("ktring_to_be_diff-ed"));

        DiffData diffDataWithInsight = diffInsightService.provideDiffInsight(diffData);

        assertEquals(diffData.getLeftSide(), diffDataWithInsight.getLeftSide());
        assertEquals(diffData.getRightSide(), diffDataWithInsight.getRightSide());
        assertEquals(diffDataWithInsight.getType(), DiffType.DIFF);
        assertEquals("[0]", diffData.getInsightsAsString());
    }

    @Test
    public void executeGetDiff_DiffDataSingleOffsetAtLastChar_DiffTypeDiff() {
        Long diffId = getRandomId();

        DiffData diffData = createDiffData(diffId, encodeBase64String("string_to_be_diff-ed"), encodeBase64String("string_to_be_diff-ee"));

        DiffData diffDataWithInsight = diffInsightService.provideDiffInsight(diffData);

        assertEquals(diffData.getLeftSide(), diffDataWithInsight.getLeftSide());
        assertEquals(diffData.getRightSide(), diffDataWithInsight.getRightSide());
        assertEquals(diffDataWithInsight.getType(), DiffType.DIFF);
        assertEquals("[26]", diffData.getInsightsAsString());
    }
}
