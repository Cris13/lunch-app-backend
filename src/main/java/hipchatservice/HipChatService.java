package hipchatservice;

import model.Response;
import model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class HipChatService
{
	private final static String HIPCHAT_HOST = "https://accela.hipchat.com/";
	private final static String READ_TOKEN = "Bearer wpuC75HaCqPi6tZeAKskTw29gyRSbAbFzvNAF1xS";
	;

	@Autowired
	private RestTemplate restTemplate;

	public Response getOrders() throws URISyntaxException
	{
		List<Order> orders = new ArrayList<>();
		URI uri = new URI(HIPCHAT_HOST + "v2/room/TestRoom/history");
		ResponseEntity<Response> response = restTemplate.exchange(
				uri,
				HttpMethod.GET,
				getHeaders(),
				new ParameterizedTypeReference<Response>() {});
		return response.getBody();
	}

	private HttpEntity getHeaders(){
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.set("Authorization",READ_TOKEN);

		return new HttpEntity<>("parameters", headers);
	}
}
