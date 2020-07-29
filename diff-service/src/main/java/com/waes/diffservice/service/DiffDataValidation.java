package com.waes.diffservice.service;

import com.waes.diffservice.data.DiffData;
import com.waes.diffservice.service.encoder.Base64InputEncoder;
import com.waes.diffservice.service.encoder.InputEncoder;

public class DiffDataValidation {
    private final static InputEncoder base64InputEncoder = new Base64InputEncoder();

    protected static void validate(DiffData diffData) {
        if (diffData.getLeftSide() == null && diffData.getRightSide() == null) {
            throw new IllegalArgumentException("Both Diff sides are empty.");
        }

        if (diffData.getLeftSide() != null) {
            if (!base64InputEncoder.isEncoded(diffData.getLeftSide())) {
                throw new IllegalArgumentException("Diff Left side is not Base64 encoded: " + diffData.getLeftSide());
            }
        }

        if (diffData.getRightSide() != null) {
            if (!base64InputEncoder.isEncoded(diffData.getRightSide())) {
                throw new IllegalArgumentException("Diff Right side is not Base64 encoded: " + diffData.getRightSide());
            }
        }
    }
}
