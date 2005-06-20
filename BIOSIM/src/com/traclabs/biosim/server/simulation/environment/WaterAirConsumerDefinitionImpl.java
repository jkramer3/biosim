package com.traclabs.biosim.server.simulation.environment;

import com.traclabs.biosim.idl.simulation.environment.SimEnvironment;
import com.traclabs.biosim.idl.simulation.environment.WaterAirConsumerDefinition;
import com.traclabs.biosim.idl.simulation.environment.WaterAirConsumerDefinitionHelper;
import com.traclabs.biosim.idl.simulation.environment.WaterAirConsumerDefinitionOperations;
import com.traclabs.biosim.idl.simulation.environment.WaterAirConsumerDefinitionPOATie;
import com.traclabs.biosim.idl.simulation.water.WaterStore;
import com.traclabs.biosim.util.OrbUtils;

/**
 * @author Scott Bell
 */

public class WaterAirConsumerDefinitionImpl extends
        StoreEnvironmentFlowRateControllableImpl implements
        WaterAirConsumerDefinitionOperations {
    private WaterAirConsumerDefinition myWaterAirConsumerDefinition;

    public WaterAirConsumerDefinitionImpl() {
        myWaterAirConsumerDefinition = WaterAirConsumerDefinitionHelper
                .narrow(OrbUtils
                        .poaToCorbaObj(new WaterAirConsumerDefinitionPOATie(
                                this)));
    }

    public WaterAirConsumerDefinition getCorbaObject() {
        return myWaterAirConsumerDefinition;
    }

    public void setWaterAirEnvironmentInputs(SimEnvironment[] pEnvironments,
            float[] pMaxFlowRates, float[] pDesiredFlowRates) {
        setEnvironments(pEnvironments);
        setEnvironmentMaxFlowRates(pMaxFlowRates);
        setEnvironmentDesiredFlowRates(pDesiredFlowRates);
    }

    public void setWaterAirStoreInputs(WaterStore[] pStores,
            float[] pMaxFlowRates, float[] pDesiredFlowRates) {
        setStores(pStores);
        setStoreMaxFlowRates(pMaxFlowRates);
        setStoreDesiredFlowRates(pDesiredFlowRates);
    }

    /**
     * Grabs as much Water as it can (i.e., the maxFlowRate) from environments.
     * 
     * @return The total amount of Water grabbed from the environments
     */
    public float getMostWaterFromEnvironment() {
        if (getEnvironments() == null)
            return 0f;
        float gatheredWaterAir = 0f;
        for (int i = 0; i < getEnvironments().length; i++) {
            float amountToTake = Math.min(getEnvironmentMaxFlowRate(i),
                    getEnvironmentDesiredFlowRate(i));
            getEnvironmentActualFlowRates()[i] = getEnvironments()[i]
                    .takeWaterMoles(amountToTake);
            gatheredWaterAir += getEnvironmentActualFlowRate(i);
        }
        return gatheredWaterAir;
    }

}