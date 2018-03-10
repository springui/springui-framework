package com.springui.web.servlet;

import com.springui.ui.UI;
import com.springui.web.UIRequest;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stephan Grundner
 */
public class ServletUIRequest extends ServletWebRequest implements UIRequest {

    @Override
    public UI getUi() {
        return UI.forSession(getSession(false));
    }

    @Override
    public String getRedirectUrl() {
        return (String) getRequest().getAttribute(REDIRECT_URL_ATTRIBUTE_NAME);
    }

    @Override
    public void setRedirectUrl(String url) {
        getRequest().setAttribute(REDIRECT_URL_ATTRIBUTE_NAME, url);
    }

    public ServletUIRequest(HttpServletRequest request) {
        super(request);
    }

    public ServletUIRequest(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
}
