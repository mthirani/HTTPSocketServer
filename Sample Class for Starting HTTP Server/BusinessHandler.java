import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.json.simple.JSONObject;
import httpserver.HTTPServerResponse;
import httpserver.ServiceHandler;

/**
 * Provides a Handler for Business
 */
public class BusinessHandler implements ServiceHandler
{	
	/**
	 * Returns the String of all businesses
	 * @param store
	 * @return
	 */
	public HTTPServerResponse doGet(HashMap<String, String> nameValue)
	{
		HTTPServerResponse httpResponse=new HTTPServerResponse();
		JSONObject obj=new JSONObject();
		if(nameValue.size()==0)
		{
			MyStore store=StartServer.getStore();
			ArrayList<String> busIDs=(ArrayList<String>) store.getBusinesses();
			obj.put("success", true);
			obj.put("businesses", busIDs);
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
