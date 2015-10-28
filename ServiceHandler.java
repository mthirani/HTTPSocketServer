import java.util.HashMap;

/**
 * This provides the abstract method for the handler classes
 */
public interface ServiceHandler
{
	public HTTPServerResponse doGet(HashMap<String, String> nameValue);
}