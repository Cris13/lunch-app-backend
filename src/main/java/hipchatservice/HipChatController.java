package hipchatservice;

import model.Message;
import model.Order;
import model.PublishResponse;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
public class HipChatController
{
	@Autowired HipChatService hipChatService;

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/orders")
	public List<Order> getOrders() throws URISyntaxException, JSONException
	{
		return hipChatService.getOrders();
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/menu")
	public PublishResponse publishMenu(@RequestBody Message message) throws URISyntaxException
	{
		return hipChatService.publishMenu(message);
	}

}
