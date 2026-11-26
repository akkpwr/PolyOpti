package org.butbetter.polyopti.client.render;

public enum CullMode {

    OFF,
    BALANCED,
    PERFORMANCE,
    AGGRESSIVE;

    private static final CullMode[] VALUES = values();

    public static CullMode byIndex(int index) {
        return index >= 0 && index < VALUES.length ? VALUES[index] : PERFORMANCE;
    }

    public boolean atLeast(CullMode other) {
        return ordinal() >= other.ordinal();
    }
}
