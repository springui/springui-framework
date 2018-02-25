package com.springui.ui;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Stephan Grundner
 */
public class Upload extends AbstractComponent {

    public interface UploadHandler {

        void receive(WebRequest request, MultipartFile multipartFile);
    }

    private UploadHandler handler;

    public UploadHandler getHandler() {
        return handler;
    }

    public void setHandler(UploadHandler handler) {
        this.handler = handler;
    }
}
