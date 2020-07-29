package com.waes.diffservice.service;

import com.waes.diffservice.service.encoder.Base64InputEncoder;
import com.waes.diffservice.service.encoder.InputEncoder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Base64InputEncoderTest {
    InputEncoder inputEncoder;

    public Base64InputEncoderTest() {
        this.inputEncoder = new Base64InputEncoder();
    }

    @Test
    public void inputIsEncoded_returnTrue() {
        // given
        String encodedInput = "QUJDRA==";
        boolean isEncoded;

        // when
        isEncoded = inputEncoder.isEncoded(encodedInput);

        // then
        assertThat(isEncoded).isTrue();
    }

    @Test
    public void inputIsNotEncoded_returnFalse() {
        // given
        String notEncodedInput = "$&&%¨&#*@+_";
        boolean isEncoded;

        // when
        isEncoded = inputEncoder.isEncoded(notEncodedInput);

        // then
        assertThat(isEncoded).isFalse();
    }

    @Test
    public void encodeValidInput_returnInputEncoded() {
        // given
        String input = "ABCD";
        String expectedInputEncoded = "QUJDRA==";
        String inputEncoded;

        // when
        inputEncoded = inputEncoder.encode(input);

        // then
        assertThat(inputEncoded).isEqualTo(expectedInputEncoded);
    }

    @Test
    public void decodeValidInput_returnInputDecoded() {
        // given
        String input = "QUJDRA==";
        String expectedInputDecoded = "ABCD";
        String inputDecoded;

        // when
        inputDecoded = inputEncoder.decode(input);

        // then
        assertThat(inputDecoded).isEqualTo(expectedInputDecoded);
    }

    @Test
    public void decodeInvalidInput_returnInputDecoded() {
        // given
        String input = "$&&%¨&#*@+_";
        String expectedErrorMessage;

        // when
        expectedErrorMessage = assertThrows(IllegalArgumentException.class, () -> inputEncoder.decode(input))
                .getMessage();

        // then
        assertThat(expectedErrorMessage).isEqualTo("Input is not in Base64 format: " + input);
    }
}
