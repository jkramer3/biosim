package com.traclabs.biosim.server.simulation.power;

import com.traclabs.biosim.idl.simulation.power.PowerProducerDefinition;
import com.traclabs.biosim.idl.simulation.power.PowerProducerDefinitionHelper;
import com.traclabs.biosim.idl.simulation.power.PowerProducerDefinitionOperations;
import com.traclabs.biosim.idl.simulation.power.PowerProducerDefinitionPOATie;
import com.traclabs.biosim.idl.simulation.power.PowerStore;
import com.traclabs.biosim.server.simulation.framework.StoreFlowRateControllableImpl;
import com.traclabs.biosim.server.util.OrbUtils;

/**
 * @author Scott Bell
 */

public class PowerProducerDefinitionImpl extends StoreFlowRateControllableImpl
        implements PowerProducerDefinitionOperations {
    private PowerProducerDefinition myPowerProducerDefinition;

    public PowerProducerDefinitionImpl() {
        myPowerProducerDefinition = PowerProducerDefinitionHelper
                .narrow(OrbUtils
                        .poaToCorbaObj(new PowerProducerDefinitionPOATie(this)));
    }

    public PowerProducerDefinition getCorbaObject() {
        return myPowerProducerDefinition;
    }

    public void setPowerOutputs(PowerStore[] pStores, float[] pMaxFlowRates,
            float[] pDesiredFlowRates) {
        setStores(pStores);
        setMaxFlowRates(pMaxFlowRates);
        setDesiredFlowRates(pDesiredFlowRates);
    }
}