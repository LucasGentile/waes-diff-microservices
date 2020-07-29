package com.waes.diffservice.service;

import com.waes.diffservice.repository.DiffRepository;
import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.waes.diffservice.utils.TestUtils.getRandomId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test classes naming pattern:
 * methodCalled_input_output
 */
@RunWith(MockitoJUnitRunner.class)
public class DiffServiceTest {

    @InjectMocks
    private DiffService diffService;

    @Mock
    private DiffRepository diffRepository;

    @Test
    public void executeGetDiff_DiffNotFound_NotFoundException() {
        // given
        Long id = getRandomId();
        String expectedErrorMessage;

        // when
        expectedErrorMessage = assertThrows(NotFoundException.class, () -> diffService.getDiff(id))
                .getMessage();

        // then
        assertThat(expectedErrorMessage).isEqualTo("Diff with id '" + id + "' not found.");
    }
}
