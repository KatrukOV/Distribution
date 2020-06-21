package com.katruk.min;

import java.util.TreeSet;

public final class MinFloat implements Min {

    private final TreeSet<Float> numbers = new TreeSet<>();

    public MinFloat of(float value) {
        this.numbers.add(value);
        return this;
    }

    @Override
    public float floatValue() {
        return this.numbers.first();
    }

}
