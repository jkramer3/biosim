package com.traclabs.biosim.idl.simulation.food;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "FoodProducerDefinition"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */

public class FoodProducerDefinitionPOATie
	extends FoodProducerDefinitionPOA
{
	private FoodProducerDefinitionOperations _delegate;

	private POA _poa;
	public FoodProducerDefinitionPOATie(FoodProducerDefinitionOperations delegate)
	{
		_delegate = delegate;
	}
	public FoodProducerDefinitionPOATie(FoodProducerDefinitionOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public com.traclabs.biosim.idl.simulation.food.FoodProducerDefinition _this()
	{
		return com.traclabs.biosim.idl.simulation.food.FoodProducerDefinitionHelper.narrow(_this_object());
	}
	public com.traclabs.biosim.idl.simulation.food.FoodProducerDefinition _this(org.omg.CORBA.ORB orb)
	{
		return com.traclabs.biosim.idl.simulation.food.FoodProducerDefinitionHelper.narrow(_this_object(orb));
	}
	public FoodProducerDefinitionOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(FoodProducerDefinitionOperations delegate)
	{
		_delegate = delegate;
	}
	public POA _default_POA()
	{
		if (_poa != null)
		{
			return _poa;
		}
		else
		{
			return super._default_POA();
		}
	}
	public float getPercentageFull(int index)
	{
		return _delegate.getPercentageFull(index);
	}

	public void setStore(com.traclabs.biosim.idl.simulation.framework.Store pStore, int index)
	{
_delegate.setStore(pStore,index);
	}

	public void setInitialStores(com.traclabs.biosim.idl.simulation.framework.Store[] pStores)
	{
_delegate.setInitialStores(pStores);
	}

	public float[] getActualFlowRates()
	{
		return _delegate.getActualFlowRates();
	}

	public com.traclabs.biosim.idl.simulation.framework.Store[] getStores()
	{
		return _delegate.getStores();
	}

	public float[] getMaxFlowRates()
	{
		return _delegate.getMaxFlowRates();
	}

	public void reset()
	{
_delegate.reset();
	}

	public void setInitialDesiredFlowRates(float[] flowrates)
	{
_delegate.setInitialDesiredFlowRates(flowrates);
	}

	public void setFoodOutputs(com.traclabs.biosim.idl.simulation.food.FoodStore[] destinations, float[] maxFlowRates, float[] desiredFlowRates)
	{
_delegate.setFoodOutputs(destinations,maxFlowRates,desiredFlowRates);
	}

	public void setDesiredFlowRate(float value, int index)
	{
_delegate.setDesiredFlowRate(value,index);
	}

	public float getTotalActualFlowRate()
	{
		return _delegate.getTotalActualFlowRate();
	}

	public void setInitialActualFlowRates(float[] flowrates)
	{
_delegate.setInitialActualFlowRates(flowrates);
	}

	public float getActualFlowRate(int index)
	{
		return _delegate.getActualFlowRate(index);
	}

	public void setInitialMaxFlowRates(float[] flowrates)
	{
_delegate.setInitialMaxFlowRates(flowrates);
	}

	public float getMaxFlowRate(int index)
	{
		return _delegate.getMaxFlowRate(index);
	}

	public float getAveragePercentageFull()
	{
		return _delegate.getAveragePercentageFull();
	}

	public float[] getDesiredFlowRates()
	{
		return _delegate.getDesiredFlowRates();
	}

	public boolean connectsTo(com.traclabs.biosim.idl.simulation.framework.Store pStore)
	{
		return _delegate.connectsTo(pStore);
	}

	public float getTotalMaxFlowRate()
	{
		return _delegate.getTotalMaxFlowRate();
	}

	public void setMaxFlowRate(float value, int index)
	{
_delegate.setMaxFlowRate(value,index);
	}

	public float getDesiredFlowRate(int index)
	{
		return _delegate.getDesiredFlowRate(index);
	}

	public float getTotalDesiredFlowRate()
	{
		return _delegate.getTotalDesiredFlowRate();
	}

}