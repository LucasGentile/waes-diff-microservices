package com.waes.diffservice.service.encoder;

public interface InputEncoder {
    boolean isEncoded(String input);
    String encode(String decodedInput);
    String decode(String encodedInput);
}
