package ru.javawebinar.topjava.util;

public class MyBoolean {
    private boolean current;

    public MyBoolean() {
        current = false;
    }

    public void changeMyBooleanValue() {
        current = true;
    }

    public boolean getValue() {
        return current;
    }
}
