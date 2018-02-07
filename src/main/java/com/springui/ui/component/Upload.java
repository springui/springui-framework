package com.springui.ui.component;

import com.springui.ui.Template;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Stephan Grundner
 */
@Template(Upload.DEFAULT)
public class Upload extends Component {

    public interface UploadHandler {

        void receive(WebRequest request, MultipartFile multipartFile);
    }

    public static final String DEFAULT = "{theme}/ui/upload";

    private UploadHandler handler;

    public UploadHandler getHandler() {
        return handler;
    }

    public void setHandler(UploadHandler handler) {
        this.handler = handler;
    }
}
