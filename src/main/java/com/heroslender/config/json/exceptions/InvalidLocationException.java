package com.heroslender.config.json.exceptions;

import com.google.gson.JsonParseException;

public class InvalidLocationException extends JsonParseException {
    public InvalidLocationException(String message) {
        super(message);
    }
}
