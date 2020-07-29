package com.waes.diffservice.utils;

import com.waes.diffservice.data.DiffData;
import com.waes.diffservice.model.Diff;

import java.util.Base64;

public class TestUtils {

    public static Diff createDiff(Long id, String leftSideContent, String rightSideContent) {
        Diff diff = new Diff();
        diff.setId(id);
        diff.setLeftSide(leftSideContent);
        diff.setRightSide(rightSideContent);

        return diff;
    }

    public static DiffData createDiffData(Long id, String leftSideContent, String rightSideContent) {
        DiffData diffData = new DiffData();
        diffData.setId(id);
        diffData.setLeftSide(leftSideContent);
        diffData.setRightSide(rightSideContent);

        return diffData;
    }

    public static String encodeBase64String(String simpleText) {
        byte[] base64EncodedBytes = Base64.getEncoder().encode(simpleText.getBytes());
        return new String(base64EncodedBytes);
    }

    public static Long getRandomId() {
        long leftLimit = 1L;
        long rightLimit = 10000L;
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }
}
