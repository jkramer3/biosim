package com.traclabs.biosim.server.simulation.water;

import com.traclabs.biosim.idl.simulation.water.DirtyWaterProducerDefinition;
import com.traclabs.biosim.idl.simulation.water.DirtyWaterProducerDefinitionHelper;
import com.traclabs.biosim.idl.simulation.water.DirtyWaterProducerDefinitionOperations;
import com.traclabs.biosim.idl.simulation.water.DirtyWaterProducerDefinitionPOATie;
import com.traclabs.biosim.idl.simulation.water.DirtyWaterStore;
import com.traclabs.biosim.server.simulation.framework.StoreFlowRateControllableImpl;
import com.traclabs.biosim.util.OrbUtils;

/**
 * @author Scott Bell
 */

public class DirtyWaterProducerDefinitionImpl extends
        StoreFlowRateControllableImpl implements
        DirtyWaterProducerDefinitionOperations {
    private DirtyWaterProducerDefinition myDirtyWaterProducerDefinition;

    public DirtyWaterProducerDefinitionImpl() {
        myDirtyWaterProducerDefinition = DirtyWaterProducerDefinitionHelper
                .narrow(OrbUtils
                        .poaToCorbaObj(new DirtyWaterProducerDefinitionPOATie(
                                this)));
    }

    public DirtyWaterProducerDefinition getCorbaObject() {
        return myDirtyWaterProducerDefinition;
    }

    public void setDirtyWaterOutputs(DirtyWaterStore[] pStores,
            float[] pMaxFlowRates, float[] pDesiredFlowRates) {
        setStores(pStores);
        setMaxFlowRates(pMaxFlowRates);
        setDesiredFlowRates(pDesiredFlowRates);
    }
}