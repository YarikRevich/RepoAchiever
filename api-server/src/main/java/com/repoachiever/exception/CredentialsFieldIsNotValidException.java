package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class CredentialsFieldIsNotValidException extends IOException {
    public CredentialsFieldIsNotValidException() {
        this("");
    }

    public CredentialsFieldIsNotValidException(Object... message) {
        super(
                new Formatter()
                        .format("Credentials field is not valid: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
