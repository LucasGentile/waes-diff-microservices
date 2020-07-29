package com.waes.diffservice.converter;

import com.waes.diffservice.data.DiffData;
import com.waes.diffservice.model.Diff;

public class DiffDataConverter {
    public static DiffData convert(Diff diff) {
        DiffData diffData = new DiffData();
        diffData.setId(diff.getId());
        diffData.setLeftSide(diff.getLeftSide());
        diffData.setRightSide(diff.getRightSide());

        return diffData;
    }
}
