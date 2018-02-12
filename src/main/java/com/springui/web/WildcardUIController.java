package com.springui.web;

import com.springui.ui.UI;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Stephan Grundner
 */
@Controller
@RequestMapping(path = "/")
public class WildcardUIController extends UIController {

    @Override
    protected void init(UI ui) { }
}
