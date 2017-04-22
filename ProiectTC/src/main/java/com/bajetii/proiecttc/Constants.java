package com.bajetii.proiecttc;

/**
 * Created by alexandru on 22.04.2017.
 */
public enum Constants {
    LAMBDA('~');

    private char value;

    Constants(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }
}
