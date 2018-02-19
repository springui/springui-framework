package com.springui.web;

import com.springui.ui.UI;
import com.springui.ui.Upload;
import com.springui.util.WebRequestUtils;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stephan Grundner
 */
public class UIUploadController extends AbstractUIController {

    public String handleRequest(WebRequest request, MultipartRequest files) throws Exception {
        UI ui = UI.forRequest(request);

        bindUi(ui, request);

        MultiValueMap<String, String> params = WebRequestUtils.getQueryParams(request);
        String componentId = params.getFirst("component");
        String token = params.getFirst("token");


        Upload upload = (Upload) ui.getComponent(componentId);

        MultipartFile file = files.getFile(token);
        if (!file.isEmpty()) {
            Upload.UploadHandler handler = upload.getHandler();
            if (handler != null) {
                handler.receive(request, file);
            }
        }

        String redirectUrl = ui.getRedirectUrl(request);
        if (!StringUtils.isEmpty(redirectUrl)) {
            return redirectUrl;
        } else {
            String path = WebRequestUtils.getPath(request);
            path = path.substring(0, path.length() - "/upload".length());
            ViewRegistry viewRegistry = ui.getViewRegistry();
            ViewRegistry.Registration registration = viewRegistry.findRegistration(path);
            Assert.notNull(registration);
            String requestUrl = registration.getOriginalRequestUrl();
            Assert.hasText(requestUrl);
            return requestUrl;
        }
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        MultipartRequest files = (MultipartRequest) request;
        String redirectUrl = handleRequest(new ServletWebRequest(request, response), files);
        return new ModelAndView(String.format("redirect:%s", redirectUrl));
    }
}
