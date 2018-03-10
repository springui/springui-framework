package com.springui.tests;

import com.springui.ui.UI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Stephan Grundner
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = UITests.Config.class)
public class UITests {

    @Configuration
    public static class Config { }

    @Test
    public void testComponentHierarchy() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        UI ui = UI.forSession(request.getSession(true));
    }
}
