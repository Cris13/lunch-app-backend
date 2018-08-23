package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response
{
	List<Items> items;


	public List<Items> getItems()
	{
		return items;
	}

	public void setItems(List<Items> items)
	{
		this.items = items;
	}

}
