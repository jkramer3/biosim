package com.traclabs.biosim.server.sensor.water;

import com.traclabs.biosim.idl.framework.BioModule;
import com.traclabs.biosim.idl.sensor.water.DirtyWaterOutFlowRateSensorOperations;
import com.traclabs.biosim.idl.simulation.framework.DirtyWaterProducer;
import com.traclabs.biosim.server.sensor.framework.GenericSensorImpl;

public class DirtyWaterOutFlowRateSensorImpl extends GenericSensorImpl
        implements DirtyWaterOutFlowRateSensorOperations {
    private DirtyWaterProducer myProducer;

    private int myIndex;

    public DirtyWaterOutFlowRateSensorImpl(int pID, String pName) {
        super(pID, pName);
    }

    protected void gatherData() {
        float preFilteredValue = getInput().getDirtyWaterOutputActualFlowRate(
                myIndex);
        myValue = randomFilter(preFilteredValue);
    }

    protected void notifyListeners() {
        //does nothing right now
    }

    public void setInput(DirtyWaterProducer pProducer, int pIndex) {
        myProducer = pProducer;
        myIndex = pIndex;
    }

    public DirtyWaterProducer getInput() {
        return myProducer;
    }

    public float getMax() {
        return myProducer.getDirtyWaterOutputMaxFlowRate(myIndex);
    }

    public int getIndex() {
        return myIndex;
    }

    public BioModule getInputModule() {
        return (BioModule) (myProducer);
    }
}