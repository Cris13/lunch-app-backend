package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response
{
	List<Items> items;
	Integer startIndex;
	Integer maxResults;
	Links links;

	public List<Items> getItems()
	{
		return items;
	}

	public void setItems(List<Items> items)
	{
		this.items = items;
	}

	public Integer getStartIndex()
	{
		return startIndex;
	}

	public void setStartIndex(Integer startIndex)
	{
		this.startIndex = startIndex;
	}

	public Integer getMaxResults()
	{
		return maxResults;
	}

	public void setMaxResults(Integer maxResults)
	{
		this.maxResults = maxResults;
	}

	public Links getLinks()
	{
		return links;
	}

	public void setLinks(Links links)
	{
		this.links = links;
	}
}
