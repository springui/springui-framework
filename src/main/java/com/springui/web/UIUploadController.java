package com.springui.web;

import com.springui.ui.UI;
import com.springui.ui.Upload;
import com.springui.util.WebRequestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stephan Grundner
 */
public class UIUploadController extends AbstractUIController {

    public ModelAndView handleRequest(WebRequest request, MultipartRequest files) throws Exception {
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

        ModelAndView modelAndView = new ModelAndView("redirect:");

        return modelAndView;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        MultipartRequest files = (MultipartRequest) request;
        return handleRequest(new ServletWebRequest(request, response), files);
    }
}
