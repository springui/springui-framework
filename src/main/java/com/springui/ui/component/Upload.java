package com.springui.ui.component;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Stephan Grundner
 */
public class Upload extends Component {

    public interface UploadHandler {

        void receive(WebRequest request, MultipartFile multipartFile);
    }

    public static final String DEFAULT = "ui/upload";

    {setTemplate(DEFAULT);}

    private UploadHandler handler;

    public UploadHandler getHandler() {
        return handler;
    }

    public void setHandler(UploadHandler handler) {
        this.handler = handler;
    }
}
