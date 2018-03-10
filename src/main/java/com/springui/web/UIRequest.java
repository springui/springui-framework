package com.springui.web;

import com.springui.ui.UI;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Stephan Grundner
 */
public interface UIRequest extends WebRequest {

    String REDIRECT_URL_ATTRIBUTE_NAME = UIRequest.class.getName() + "#redirectUrl";

    UI getUi();

    String getRedirectUrl();
    void setRedirectUrl(String url);
}
