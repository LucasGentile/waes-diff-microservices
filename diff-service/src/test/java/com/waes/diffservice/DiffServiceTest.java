package com.waes.diffservice;

import com.waes.diffservice.data.DiffData;
import com.waes.diffservice.enums.DiffType;
import com.waes.diffservice.model.Diff;
import com.waes.diffservice.repository.DiffRepository;
import com.waes.diffservice.service.DiffService;
import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.waes.diffservice.utils.TestUtils.*;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Test classes naming patter:
 *     methodCalled_input_output
 *
 */
@RunWith(SpringRunner.class)
public class DiffServiceTest {

    @TestConfiguration
    static class DiffServiceTestContextConfiguration {

        @Bean
        public DiffService diffService() {
            return new DiffService();
        }

    }

    @Autowired
    private DiffService diffService;

    @MockBean
    private DiffRepository diffRepository;

    @Test(expected = NotFoundException.class)
    public void executeGetDiff_DiffNotFound_NotFoundException() throws Exception {
        Long diffId = getRandomId();

        when(diffRepository.getOne(diffId)).thenReturn(null);

        diffService.getDiff(diffId);
    }

    @Test(expected = NotFoundException.class)
    public void executeGetDiff_DiffLeftSideNull_NotFoundException() throws Exception {
        Long diffId = getRandomId();

        Diff diffLeftSideNull = createDiff(diffId, null, encodeBase64String("test"));

        when(diffRepository.findById(diffId)).thenReturn(Optional.of(diffLeftSideNull));

        diffService.getDiff(diffId);
    }

    @Test(expected = NotFoundException.class)
    public void executeGetDiff_DiffRightSideNull_NotFoundException() throws Exception {
        Long diffId = getRandomId();

        Diff diffRightSideNull = createDiff(diffId, encodeBase64String("test"), null);

        when(diffRepository.findById(diffId)).thenReturn(Optional.of(diffRightSideNull));

        diffService.getDiff(diffId);
    }

    @Test
    public void executeGetDiff_DiffEqualSides_DiffTypeEqual() throws Exception {
        Long diffId = getRandomId();

        Diff diff = createDiff(diffId, encodeBase64String("equal_string_to_be_diff-ed"), encodeBase64String("equal_string_to_be_diff-ed"));

        when(diffRepository.findById(diffId)).thenReturn(Optional.of(diff));

        DiffData diffData = diffService.getDiff(diffId);

        assertEquals(diff.getLeftSide(), diffData.getLeftSide());
        assertEquals(diff.getRightSide(), diffData.getRightSide());
        assertEquals(diffData.getType(), DiffType.EQUAL);
        assertNull(diffData.getInsight());
    }

    @Test
    public void executeGetDiff_DiffSidesDifferentLength_DiffTypeDifferentLength() throws Exception {
        Long diffId = getRandomId();

        Diff diff = createDiff(diffId, encodeBase64String("equal_string_to_be_diff-ed"), encodeBase64String("different_length_string_to_be_diff-ed"));

        when(diffRepository.findById(diffId)).thenReturn(Optional.of(diff));

        DiffData diffData = diffService.getDiff(diffId);

        assertEquals(diff.getLeftSide(), diffData.getLeftSide());
        assertEquals(diff.getRightSide(), diffData.getRightSide());
        assertEquals(diffData.getType(), DiffType.DIFFERENT_LENGTH);
        assertNull(diffData.getInsight());
    }

    @Test
    public void executeGetDiff_DiffOffsetLengthAtFirstChars_DiffTypeDiff() throws Exception {
        Long diffId = getRandomId();

        Diff diff = createDiff(diffId, encodeBase64String("string_to_be_diff-ed"), encodeBase64String("diffng_to_be_diff-ed"));

        when(diffRepository.findById(diffId)).thenReturn(Optional.of(diff));

        DiffData diffData = diffService.getDiff(diffId);

        assertEquals(diff.getLeftSide(), diffData.getLeftSide());
        assertEquals(diff.getRightSide(), diffData.getRightSide());
        assertEquals(DiffType.DIFF, diffData.getType());
        assertEquals("[0-5]", diffData.getInsight());
    }

    @Test
    public void executeGetDiff_DiffOffsetLengthAtLastChars_DiffTypeDiff() throws Exception {
        Long diffId = getRandomId();

        Diff diff = createDiff(diffId, encodeBase64String("string_to_be_diff-ed"), encodeBase64String("string_to_be_difdiff"));

        when(diffRepository.findById(diffId)).thenReturn(Optional.of(diff));

        DiffData diffData = diffService.getDiff(diffId);

        assertEquals(diff.getLeftSide(), diffData.getLeftSide());
        assertEquals(diff.getRightSide(), diffData.getRightSide());
        assertEquals(diffData.getType(), DiffType.DIFF);
        assertEquals("[22-23, 25-26]", diffData.getInsight());
    }

    @Test
    public void executeGetDiff_DiffDataOffsetLengthAllChars_DiffTypeDiff() throws Exception {
        Long diffId = getRandomId();

        Diff diff = createDiff(diffId, encodeBase64String("string_to_be_diff-ed"), encodeBase64String("12345678910987654321"));

        when(diffRepository.findById(diffId)).thenReturn(Optional.of(diff));

        DiffData diffData = diffService.getDiff(diffId);

        assertEquals(diff.getLeftSide(), diffData.getLeftSide());
        assertEquals(diff.getRightSide(), diffData.getRightSide());
        assertEquals(diffData.getType(), DiffType.DIFF);
        assertEquals("[0-26]", diffData.getInsight());
    }

    @Test
    public void executeGetDiff_DiffDataSingleOffsetAtFirstChar_DiffTypeDiff() throws Exception {
        Long diffId = getRandomId();

        Diff diff = createDiff(diffId, encodeBase64String("string_to_be_diff-ed"), encodeBase64String("ktring_to_be_diff-ed"));

        when(diffRepository.findById(diffId)).thenReturn(Optional.of(diff));

        DiffData diffData = diffService.getDiff(diffId);

        assertEquals(diff.getLeftSide(), diffData.getLeftSide());
        assertEquals(diff.getRightSide(), diffData.getRightSide());
        assertEquals(diffData.getType(), DiffType.DIFF);
        assertEquals("[0]", diffData.getInsight());
    }

    @Test
    public void executeGetDiff_DiffDataSingleOffsetAtLastChar_DiffTypeDiff() throws Exception {
        Long diffId = getRandomId();

        Diff diff = createDiff(diffId, encodeBase64String("string_to_be_diff-ed"), encodeBase64String("string_to_be_diff-ee"));

        when(diffRepository.findById(diffId)).thenReturn(Optional.of(diff));

        DiffData diffData = diffService.getDiff(diffId);

        assertEquals(diff.getLeftSide(), diffData.getLeftSide());
        assertEquals(diff.getRightSide(), diffData.getRightSide());
        assertEquals(diffData.getType(), DiffType.DIFF);
        assertEquals("[26]", diffData.getInsight());
    }
}
