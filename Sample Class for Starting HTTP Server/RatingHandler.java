import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.json.simple.JSONObject;
import httpserver.HTTPServerResponse;
import httpserver.ServiceHandler;

/**
 * Provides a Handler for Rating
 */
public class RatingHandler implements ServiceHandler
{	
	/**
	 * Returns the String containing the rating for the required businessID	
	 * @param store
	 * @param businessID
	 * @return
	 */
	public HTTPServerResponse doGet(HashMap<String, String> nameValue)
	{
		JSONObject obj=new JSONObject();
		HTTPServerResponse httpResponse=new HTTPServerResponse();
		if(nameValue.size()==1 && nameValue.containsKey("businessID"))
		{
			MyStore store=StartServer.getStore();
			ArrayList<String> busIDs=(ArrayList<String>) store.getBusinesses();
			String businessID=nameValue.get("businessID");
			if(busIDs.indexOf(businessID)>=0)
			{
				double avgRating=store.getRating(businessID);
				obj.put("success", true);
				obj.put("rating", avgRating);
				obj.put("businessID", businessID);
			}
			else
			{
				obj.put("success", false);
				obj.put("rating", "invalid_bus_id_provided_by_user");
			}
			httpResponse.setStatusCode(HTTPServerResponse.getValidStatus(), "OK");
			httpResponse.setDate(new Date());
			httpResponse.setContentType("application/json");
			httpResponse.setContentLength(obj.toJSONString().length());
			httpResponse.setBody(obj.toJSONString());
			
			return httpResponse;
		}
		else
		{	
			String message="The requested page is not found";
			httpResponse.setStatusCode(HTTPServerResponse.getPageNotFoundStatus(), "NOT FOUND");
			httpResponse.setDate(new Date());
			httpResponse.setContentType("text/html");
			httpResponse.setContentLength(message.length());
			httpResponse.setBody(message);
			
			return httpResponse;
		}
	}
}
