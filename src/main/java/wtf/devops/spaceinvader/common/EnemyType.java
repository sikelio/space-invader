package wtf.devops.spaceinvader.common;

import java.util.Random;

public enum EnemyType{
    OCTOPUS,
    CRAB,
    SQUID,
    UFO;

    private static final Random random = new Random();

    public static EnemyType randomValue() {
        EnemyType[] values = values();

        return values[random.nextInt(values.length)];
    }
}
