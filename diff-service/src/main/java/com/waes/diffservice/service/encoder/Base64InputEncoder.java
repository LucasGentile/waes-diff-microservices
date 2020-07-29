package com.waes.diffservice.service.encoder;

import com.sun.jersey.core.util.Base64;
import lombok.extern.java.Log;

@Log
public class Base64InputEncoder implements InputEncoder {
    @Override
    public boolean isEncoded(String input) {
        return Base64.isBase64(input);
    }

    @Override
    public String encode(String decodedInput) {
        log.info("Input to be encoded:\n" + decodedInput + "\n");

        byte[] decodedInputBytes = decodedInput.getBytes();
        byte[] encodedInputBytes = Base64.encode(decodedInputBytes);
        String encodedInputString = new String(encodedInputBytes);

        log.info("Encoded input:\n" + encodedInputString + "\n");

        return encodedInputString;
    }

    @Override
    public String decode(String encodedInput) {
        log.info("Input to be decoded:\n" + encodedInput + "\n");

        if(!this.isEncoded(encodedInput)){
            throw new IllegalArgumentException("Input is not in Base64 format: " + encodedInput);
        }

        byte[] encodedInputBytes = encodedInput.getBytes();
        byte[] decodedInputBytes = Base64.decode(encodedInputBytes);
        String decodedInputString = new String(decodedInputBytes);

        log.info("Decoded input:\n" + decodedInputString + "\n");

        return decodedInputString;
    }

}
