package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.lang.Nullable;
@JsonIgnoreProperties(ignoreUnknown = true)
public class From
{

	@Nullable
	private String name;


	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
