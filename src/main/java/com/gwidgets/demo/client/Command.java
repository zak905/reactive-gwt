package com.gwidgets.demo.client;

public class Command {

    int position;
    String value;

    public Command(int position, String value) {
        this.position = position;
        this.value = value;
    }

    public int getPosition() {
        return position;
    }

    public String getValue() {
        return value;
    }
}
