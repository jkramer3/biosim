/*
 * Created on Feb 14, 2005
 */
package com.traclabs.biosim.editor.graph;

import org.tigris.gef.presentation.Fig;

import com.traclabs.biosim.editor.presentation.FigPanel;

/**
 * Adds a panel for nodes that required more complex layout strategies.
 * 
 * @author kkusy
 */
public abstract class FigPanelNode extends ModuleFigNode {
    protected Fig createBgFig() {
        return new FigPanel();
    }

    public FigPanel getPanel() {
        return (FigPanel) _bgFig;
    }
}