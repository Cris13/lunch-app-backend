package hipchatservice;

import model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
public class HipChatController
{
	@Autowired
	HipChatService hipChatService;

	@GetMapping("/getOrders")
	public Response getOrders() throws URISyntaxException
	{
		return hipChatService.getOrders();
	}

}
