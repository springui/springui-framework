package com.springui.view;

import com.springui.ui.UI;

/**
 * @author Stephan Grundner
 */
public interface ViewMappingRegistryFactory {

    ViewMappingRegistry createViewMappingRegistry(UI ui);
}
