package net.swedz.miextended.machines.components.farmer;

public final class FarmerTaskOperations
{
	private final int max;
	
	private int operations;
	
	public FarmerTaskOperations(int max)
	{
		this.max = max;
	}
	
	public boolean operate()
	{
		return ++operations >= max;
	}
	
	public boolean didOperate()
	{
		return operations > 0;
	}
}