package com.heroslender.exceptions.inventory;

public class ItemNotDefinedException extends NullPointerException {

    public ItemNotDefinedException(String message) {
        super(message);
    }

}
