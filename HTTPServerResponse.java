package cs601.httpserver;
import java.util.Date;

/**
 * This data structure holds the HTTP Server response
 */
public class HTTPServerResponse
{
	private String headerFormat;
	private String bodyFormat;
	private static final int HTTP_VALID_RESPONSE=200;
	private static final int HTTP_INVALID_RES_PAGE_NOT_FOUND=404;
	private static final int HTTP_INVALID_RES_METHOD_NOT_ALLOWED=405;
	
	public HTTPServerResponse()
	{
		headerFormat=new String();
		bodyFormat=new String();
	}
	
	public void setStatusCode(int statusCode, String message)
	{
		headerFormat="HTTP/1.1 " + statusCode + " " + message + "\n";
	}
	
	public void setContentType(String content)
	{
		headerFormat=headerFormat + "Content-Type: " + content+"\n";
	}
	
	public void setContentLength(int length)
	{
		headerFormat=headerFormat + "Content-Length: " + length +"\n";
	}
	
	public void setDate(Date date)
	{
		headerFormat=headerFormat + "Date: " + date +"\n";
	}
	
	public void setBody(String body)
	{
		bodyFormat=body;
	}
	
	public String getHeader()
	{
		return headerFormat;
	}
	
	public String getBody()
	{
		return bodyFormat;
	}
	
	public static Integer getValidStatus()
	{
		return HTTP_VALID_RESPONSE;
	}
	
	public static Integer getPageNotFoundStatus()
	{
		return HTTP_INVALID_RES_PAGE_NOT_FOUND;
	}
	
	public static Integer getMethodNotAllowedStatus()
	{
		return HTTP_INVALID_RES_METHOD_NOT_ALLOWED;
	}
}
