package com.traclabs.biosim.server.sensor.crew;

import com.traclabs.biosim.idl.framework.BioModule;
import com.traclabs.biosim.idl.sensor.crew.CrewGroupDeathSensorOperations;

public class CrewGroupDeathSensorImpl extends CrewGroupSensorImpl implements
        CrewGroupDeathSensorOperations {
    public CrewGroupDeathSensorImpl(int pID, String pName) {
        super(pID, pName);
    }

    protected void gatherData() {
        boolean preFilteredBooleanValue = getInput().isDead();
        if (preFilteredBooleanValue)
            myValue = getStochasticFilter().randomFilter(1);
        else
            myValue = getStochasticFilter().randomFilter(0);
    }

    public float getMax() {
        return 1f;
    }

    protected void notifyListeners() {
    }

    public BioModule getInputModule() {
        return getInput();
    }
}