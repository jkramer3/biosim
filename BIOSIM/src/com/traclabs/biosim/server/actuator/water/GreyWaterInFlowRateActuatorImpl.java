package com.traclabs.biosim.server.actuator.water;

import com.traclabs.biosim.idl.actuator.water.GreyWaterInFlowRateActuatorOperations;
import com.traclabs.biosim.idl.framework.BioModule;
import com.traclabs.biosim.idl.simulation.framework.GreyWaterConsumer;
import com.traclabs.biosim.server.actuator.framework.GenericActuatorImpl;

public class GreyWaterInFlowRateActuatorImpl extends GenericActuatorImpl
        implements GreyWaterInFlowRateActuatorOperations {
    private GreyWaterConsumer myConsumer;

    private int myIndex;

    public GreyWaterInFlowRateActuatorImpl(int pID, String pName) {
        super(pID, pName);
    }

    protected void processData() {
        float myFilteredValue = randomFilter(myValue);
        getOutput().setGreyWaterInputDesiredFlowRate(myFilteredValue, myIndex);
    }

    protected void notifyListeners() {
        //does nothing right now
    }

    public void setOutput(GreyWaterConsumer pConsumer, int pIndex) {
        myConsumer = pConsumer;
        myIndex = pIndex;
    }

    public BioModule getOutputModule() {
        return (BioModule) (myConsumer);
    }

    public GreyWaterConsumer getOutput() {
        return myConsumer;
    }

    public int getIndex() {
        return myIndex;
    }

    public float getMax() {
        return myConsumer.getGreyWaterInputMaxFlowRate(myIndex);
    }
}