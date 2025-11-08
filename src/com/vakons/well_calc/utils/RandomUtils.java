package com.vakons.well_calc.utils;

import java.util.Random;

public class RandomUtils {
    private static final Random random = new Random(System.currentTimeMillis());

    public static float randomRange(float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }

    public static float randomFloat() {
        return random.nextFloat();
    }
}
