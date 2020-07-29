package com.waes.diffservice.service;

import com.waes.diffservice.data.DiffData;
import org.junit.Test;

import static com.waes.diffservice.utils.TestUtils.createDiffData;
import static com.waes.diffservice.utils.TestUtils.encodeBase64String;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DiffDataValidationTest {

    @Test
    public void validateDiffDataWithBothSides_noExceptions() {
        // given
        DiffData data = createDiffData(10L, encodeBase64String("ABC"), encodeBase64String("ABB"));

        // when
        DiffDataValidation.validate(data);

        // then no exceptions should be thrown
    }

    @Test
    public void validateDiffDataWithLeftSideOnly_noExceptions() {
        // given
        DiffData data = createDiffData(10L, encodeBase64String("ABC"), null);

        // when
        DiffDataValidation.validate(data);

        // then no exceptions should be thrown
    }

    @Test
    public void validateDiffDataWithRightSideOnly_noExceptions() {
        // given
        DiffData data = createDiffData(10L, null, encodeBase64String("ABB"));

        // when
        DiffDataValidation.validate(data);

        // then no exceptions should be thrown
    }

    @Test
    public void validateDiffDataWithoutBothSides_illegalArgumentException() {
        // given
        DiffData data = createDiffData(10L, null, null);
        String expectedErrorMessage;

        // when
        expectedErrorMessage = assertThrows(IllegalArgumentException.class, () -> DiffDataValidation.validate(data))
                .getMessage();

        // then
        assertThat(expectedErrorMessage).isEqualTo("Both Diff sides are empty.");
    }

    @Test
    public void validateDiffDataLeftSideNotBase64Encoded_illegalArgumentException() {
        // given
        String notEncodedInput = "!@#$@#%_&";
        DiffData data = createDiffData(10L, notEncodedInput, encodeBase64String("ABB"));
        String expectedErrorMessage;

        // when
        expectedErrorMessage = assertThrows(IllegalArgumentException.class, () -> DiffDataValidation.validate(data))
                .getMessage();

        // then
        assertThat(expectedErrorMessage).isEqualTo("Diff Left side is not Base64 encoded: " + notEncodedInput);
    }

    @Test
    public void validateDiffDataRightSideNotBase64Encoded_illegalArgumentException() {
        // given
        String notEncodedInput = "!@#$@#%_&";
        DiffData data = createDiffData(10L, encodeBase64String("ABB"), notEncodedInput);
        String expectedErrorMessage;

        // when
        expectedErrorMessage = assertThrows(IllegalArgumentException.class, () -> DiffDataValidation.validate(data))
                .getMessage();

        // then
        assertThat(expectedErrorMessage).isEqualTo("Diff Right side is not Base64 encoded: " + notEncodedInput);
    }
}
