/*
 * Created on May 12, 2005
 *
 */
package com.traclabs.biosim.client.simulation.power.schematic.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.traclabs.biosim.client.simulation.power.schematic.graph.FigStoreNode;
import com.traclabs.biosim.client.simulation.power.schematic.graph.ModuleNode;
import com.traclabs.biosim.idl.simulation.framework.Store;

/**
 * @author scott
 *
 */
public class StorePropertiesFrame extends JFrame {
    private JTextField myNameField;

    private JFormattedTextField myCapacityField;

    private JFormattedTextField myLevelField;
    
    private JButton myOKButton;

    private Store myStore;
    
    private FigStoreNode myFigStoreNode;

    public StorePropertiesFrame(FigStoreNode pNode) {
        myFigStoreNode = pNode;
        ModuleNode owner = (ModuleNode) pNode.getOwner();
        myStore = (Store) owner.getSimBioModule();
        myNameField = new JTextField(myStore.getModuleName());
        myCapacityField = new JFormattedTextField(NumberFormat.getNumberInstance());
        myCapacityField.setValue(new Float(myStore.getCurrentCapacity()));
        myLevelField = new JFormattedTextField(NumberFormat.getNumberInstance());
        myLevelField.setValue(new Float(myStore.getCurrentLevel()));
        myOKButton = new JButton(new OKAction());

        setLayout(new GridLayout(3, 2));
        add(new JLabel("Capacity"));
        add(myCapacityField);
        add(new JLabel("Level"));
        add(myLevelField);
        add(myOKButton);
        setTitle(pNode.getText() + " Properties");
    }
    
    /**
     * Action that auto-arranges internal menus
     */
    private class OKAction extends AbstractAction {
        public OKAction() {
            super("OK");
        }

        public void actionPerformed(ActionEvent ae) {
            float capacity = ((Number)myCapacityField.getValue()).floatValue();
            float level = ((Number)myCapacityField.getValue()).floatValue();
            myStore.setCurrentCapacity(capacity);
            myStore.setCurrentLevel(level);
            myFigStoreNode.damage();
            dispose();
        }
    }

}
