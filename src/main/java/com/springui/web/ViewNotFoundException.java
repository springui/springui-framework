package com.springui.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Stephan Grundner
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ViewNotFoundException extends RuntimeException {

    private static String createMessage(String path) {
        return String.format("No view registered for path [%s]", path);
    }

    private final String path;

    public ViewNotFoundException(String path) {
        super(createMessage(path));
        this.path = path;
    }
}
