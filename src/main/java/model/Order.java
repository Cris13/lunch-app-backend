package model;

import java.util.List;

public class Order
{
	private String dish;
	private List<String> consumers;

	public String getDish()
	{
		return dish;
	}

	public void setDish(String dish)
	{
		this.dish = dish;
	}

	public List<String> getConsumers()
	{
		return consumers;
	}

	public void setConsumers(List<String> consumers)
	{
		this.consumers = consumers;
	}
}
