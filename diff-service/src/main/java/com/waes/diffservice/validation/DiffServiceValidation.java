package com.waes.diffservice.validation;

import com.waes.diffservice.model.Diff;

public class DiffServiceValidation {
    public static void validate(Long id, Diff diff) {
        if (diff.getLeftSide() == null) {
            throw new IllegalStateException("Diff with id '" + id + "' missing LEFT side.");
        }
        if (diff.getRightSide() == null) {
            throw new IllegalStateException("Diff with id '" + id + "' missing RIGHT side.");
        }
    }
}
