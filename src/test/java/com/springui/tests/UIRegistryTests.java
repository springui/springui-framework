package com.springui.tests;

import com.springui.ui.UI;
import com.springui.util.BeanFactoryUtils;
import com.springui.web.UIContext;
import com.springui.web.UIMappingRegistry;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Stephan Grundner
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = UIRegistryTests.Config.class)
public class UIRegistryTests {

    @Configuration
    public static class Config {

        @Bean
        @Scope("prototype")
        protected TestUI ui() {
            return new TestUI();
        }

        @Bean
        @ConditionalOnMissingBean(UIMappingRegistry.class)
        public UIMappingRegistry uiMappingRegistry() {
            return new UIMappingRegistry();
        }

        @Bean
        @Scope("prototype")
        public UIContext uiContext(ApplicationContext applicationContext,
                                   UIMappingRegistry uiMappingRegistry) {
            return new UIContext(applicationContext, uiMappingRegistry);
        }
    }

    private static class TestUI extends UI {

    }

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testUiMappings() {

        MockServletContext servletContext = new MockServletContext();
        servletContext.setContextPath("/server1");
        MockHttpSession session = new MockHttpSession(servletContext);

        UIContext context = BeanFactoryUtils.getPrototypeBean(applicationContext, UIContext.class);
        session.setAttribute(UIContext.class.getName(), context);

        MockHttpServletRequest request;
        UIMappingRegistry registry;

        // Request 1
        request = new MockHttpServletRequest("GET", "/");
        request.setSession(session);

        context = UIContext.forSession(request);
        registry = context.getUiMappingRegistry();

        registry.registerUiClass("/**", TestUI.class);
        registry.registerUiClass("/myapp/one/two/**", TestUI.class);
        registry.registerUiClass("/bar", TestUI.class);
        registry.registerUiClass("/myapp/first", TestUI.class);
        registry.registerUiClass("/myapp", TestUI.class);
        registry.registerUiClass("/myapp/one**", TestUI.class);

        UIMappingRegistry.Mapping mapping;

        // Request 2
        request = new MockHttpServletRequest("GET", "/myapp/one/two/a/b/c");
        request.setSession(session);

        context = UIContext.forSession(request);
        registry = context.getUiMappingRegistry();

        mapping = registry.findMapping(request);
        Assert.assertNotNull(mapping);
        Assert.assertEquals(mapping.getPattern(), "/myapp/one/two/**");
        UI ui = context.getUi(request);
        Assert.assertNotNull(ui);

        // Request 3
        request = new MockHttpServletRequest("GET", "/myapp/one/a/b/c");
        request.setSession(session);

        context = UIContext.forSession(request);
        registry = context.getUiMappingRegistry();
        mapping = registry.findMapping(request);
        Assert.assertNotNull(mapping);
//        Assert.assertEquals(mapping.getPattern(), "/myapp/one/**");
        UI ui2 = context.getUi(request);

    }
}
