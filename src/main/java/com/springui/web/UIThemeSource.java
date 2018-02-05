package com.springui.web;

import org.springframework.ui.context.ThemeSource;

/**
 * @author Stephan Grundner
 */
public interface UIThemeSource extends ThemeSource {

    UITheme getTheme(String themeName);
}
