package com.waes.diffservice.validation;

import com.waes.diffservice.model.Diff;
import org.junit.Test;

import static com.waes.diffservice.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DiffServiceValidationTest {

    @Test
    public void validateDiffDataWithBothSides_noExceptions() {
        // given
        Long id = getRandomId();
        Diff diff = createDiff(id, encodeBase64String("ABC"), encodeBase64String("ABC"));

        // when
        DiffServiceValidation.validate(id, diff);

        // then no exceptions should be thrown
    }

    @Test
    public void validateDiffWithoutLeftSide_illegalStateException() {
        // given
        Long id = getRandomId();
        Diff diff = createDiff(id, null, encodeBase64String("ABB"));
        String expectedErrorMessage;

        // when
        expectedErrorMessage = assertThrows(IllegalStateException.class, () -> DiffServiceValidation.validate(id, diff))
                .getMessage();

        // then
        assertThat(expectedErrorMessage).isEqualTo("Diff with id '" + id + "' missing LEFT side.");
    }

    @Test
    public void validateDiffWithoutRightSide_illegalStateException() {
        // given
        Long id = getRandomId();
        Diff diff = createDiff(id, encodeBase64String("AAA"), null);
        String expectedErrorMessage;

        // when
        expectedErrorMessage = assertThrows(IllegalStateException.class, () -> DiffServiceValidation.validate(id, diff))
                .getMessage();

        // then
        assertThat(expectedErrorMessage).isEqualTo("Diff with id '" + id + "' missing RIGHT side.");
    }
}
