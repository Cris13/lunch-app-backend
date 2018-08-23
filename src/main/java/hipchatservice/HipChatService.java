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
import java.rmi.MarshalException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class HipChatService
{
	private final static String HIPCHAT_HOST = "https://accela.hipchat.com/";
	private final static String READ_TOKEN = "Bearer wpuC75HaCqPi6tZeAKskTw29gyRSbAbFzvNAF1xS";
	private final static String WRITE_TOKEN = "Bearer 8HPiYoghIFAe70QPAILR95JDj2tWU8iT2bvqc3EN";
	private final static String VIEW_ROOM = "Bearer lzVq1yji3TADmSlODKCIrSI4kKqrEyX6aALuD6xe";
	private final static String MESSAGE = "Please remember to order your lunch";


	@Autowired
	private RestTemplate restTemplate;

	public List<Order> getOrders() throws URISyntaxException, JSONException
	{
		URI uri = new URI(HIPCHAT_HOST + "v2/room/LunchApp/history?reverse=false");
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

	public List<User> sendReminder() throws URISyntaxException, JSONException
	{
		List<Member> members = getRoomMemebers();
		Set<String> whoOrdered = getWhoOrdered();
		return sendReminder(members,  whoOrdered);
	}

	private List<User> sendReminder(List<Member> members, Set<String> whoOrdered)
			throws URISyntaxException
	{
		List<User> users = new ArrayList<>();

		for(Member member : members){
			if(!whoOrdered.contains(member.getName())){
				sendReminderPost(member.getId());
				users.add(new User(member.getName()));
			}
		}

		return users;
	}

	private void sendReminderPost(String member) throws URISyntaxException
	{
		URI uri = new URI(HIPCHAT_HOST + "v2/user/"+member+"/message");
		Message message = new Message();
		message.setMessage(MESSAGE);
		restTemplate.exchange(
				uri.toString(),
				HttpMethod.POST,
				getHeaders(message, WRITE_TOKEN),
				PublishResponse.class);

	}

	private Set<String> getWhoOrdered()
			throws URISyntaxException, JSONException
	{
		List<Order> orders = getOrders();
		Set<String> whoOrdered =  new HashSet<>();
		for(Order order: orders){
			whoOrdered.add(order.getHungry());
		}
		return whoOrdered;
	}

	private List<Member> getRoomMemebers() throws URISyntaxException
	{
		URI uri = new URI(HIPCHAT_HOST + "v2/room/LunchApp/member");

		ResponseEntity<Response> response= restTemplate.exchange(
				uri.toString(),
				HttpMethod.GET,
				getHeaders(null, VIEW_ROOM),
				new ParameterizedTypeReference<Response>() {});
		List<Items> items = response.getBody().getItems();
		List<Member> members =  new ArrayList<>();
		for (Items item: items){
			Member member = new Member();
			member.setId(item.getId());
			member.setName(item.getName());
			members.add(member);
		}
		return members;
	}

	private List<Order> buildOrders(String response) throws JSONException
	{
		List<Order> orders = new ArrayList<>();
		JSONObject json =  new JSONObject(response);
		JSONArray jsonArray = json.getJSONArray("items");
		for (int i = 0; i<jsonArray.length() ; i++){
			JSONObject item = jsonArray.getJSONObject(i);
			String dish = (String) item.get("message");
			if(!isTodayOrder(item)){
				break;
			}
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

	private boolean isTodayOrder(JSONObject item) throws JSONException
	{
		String orderDayString = (String) item.get("date");
		LocalDate orderDay = LocalDate.parse(orderDayString.substring(0, orderDayString.indexOf("T")));
		LocalDate localDate = LocalDate.now();
		return orderDay.isEqual(localDate);
	}

	private HttpEntity getHeaders(Message body, String token){
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", token);

		return new HttpEntity<>(body, headers);
	}
}
