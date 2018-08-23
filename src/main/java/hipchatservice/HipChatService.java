package hipchatservice;

import model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.Set;

@Service
public class HipChatService
{
	private final static String HIPCHAT_HOST = "https://accela.hipchat.com/";
	private final static String READ_TOKEN = "Bearer wpuC75HaCqPi6tZeAKskTw29gyRSbAbFzvNAF1xS";
	private final static String WRITE_TOKEN = "Bearer 8HPiYoghIFAe70QPAILR95JDj2tWU8iT2bvqc3EN";


	@Autowired
	private RestTemplate restTemplate;

	public List<Order> getOrders() throws URISyntaxException, JSONException
	{
		URI uri = new URI(HIPCHAT_HOST + "v2/room/LunchApp/history");
		ResponseEntity<String> response = restTemplate.exchange(
				uri,
				HttpMethod.GET,
				getHeaders(null, READ_TOKEN),
				new ParameterizedTypeReference<String>() {});
		return buildOrders(response.getBody());
	}

	public PublishResponse publishMenu(Message message) throws URISyntaxException
	{

		URI uri = new URI(HIPCHAT_HOST + "v2/room/LunchApp/message");
		restTemplate.optionsForAllow(uri);
		ResponseEntity<PublishResponse> response= restTemplate.exchange(
				uri.toString(),
				HttpMethod.POST,
				getHeaders(message, WRITE_TOKEN),
				PublishResponse.class);

		return response.getBody();
	}

	private List<Order> buildOrders(String response) throws JSONException
	{
		List<Order> orders = new ArrayList<>();
		JSONObject json =  new JSONObject(response);
		JSONArray jsonArray = json.getJSONArray("items");
		for (int i = 0; i<jsonArray.length() ; i++){
			JSONObject item = jsonArray.getJSONObject(i);
			String dish = (String) item.get("message");
			if(dish.toLowerCase().startsWith("@lunch")){
				JSONObject from = (JSONObject)item.get("from");
				Order order = new Order();
				order.setDish(dish.substring(6));
				order.setHungry((String)from.get("name"));
				orders.add(order);
			}
		}
		return orders;
	}

	private HttpEntity getHeaders(Message body, String token){
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", token);

		return new HttpEntity<>(body, headers);
	}
}
