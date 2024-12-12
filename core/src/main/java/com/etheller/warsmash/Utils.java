package com.etheller.warsmash;

import java.util.Random;

public class Utils {

    public static float nextFloat(Random random, float value)
    {
        return random.nextFloat() * (value);
    }
}
