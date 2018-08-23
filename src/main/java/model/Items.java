package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.lang.Nullable;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Items
{
	private String type;
	private String message;
	private String id;
	private Date date;
	@Nullable
	private From from;

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public From getFrom()
	{
		return from;
	}

	public void setFrom(From from)
	{
		this.from = from;
	}
}
