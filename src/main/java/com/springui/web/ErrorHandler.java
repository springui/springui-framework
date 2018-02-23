package com.springui.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stephan Grundner
 */
public interface ErrorHandler {

    void handleError(HttpServletRequest request, HttpServletResponse response, Exception e);
}
