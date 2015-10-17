package cs601.httpserver;
import cs601.concurrent.WorkQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

/**
 * Provides a method to build the Socket server
 */
public class HTTPServer
{
	/**
	 * This method invokes the server startup and handles all the requests accordingly
	 */
	private boolean serverRunning=true;
	private final int threads=10;
	private static HashMap<String, Class> mapping=new HashMap<String, Class>();
	
	public void start(Integer port) 
	{	
		WorkQueue wrkQue=new WorkQueue(threads);
		try
		{
			ServerSocket serve=new ServerSocket(port);
			while(serverRunning)
			{
				Socket sock = serve.accept();
				HTTPServerHandler httpServerHandle=new HTTPServerHandler(sock);
				wrkQue.execute(new Thread(httpServerHandle));
			}
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void addClass(String path, Class cls)
	{
		mapping.put(path, cls);
	}
	
	public static HashMap<String, Class> getMapping()
	{
		return mapping;
	}
}

/**
 * Provides a thread handler for the Socket HTTPServer
 */
class HTTPServerHandler implements Runnable
{
	private Socket serverHandler;
	
	public HTTPServerHandler(Socket serveHandle)
	{
		this.serverHandler=serveHandle;
	}
	
	public void run()
	{
		try(BufferedReader instream=new BufferedReader(new InputStreamReader(serverHandler.getInputStream()));PrintWriter writer=new PrintWriter(new OutputStreamWriter(serverHandler.getOutputStream())))
		{
			HTTPServerResponse httpResponse;
			HashMap<String, String> nameValue=new HashMap<String, String>();
			String line=instream.readLine();
			String []splitHeaders=line.split("\\s");
			if(splitHeaders.length==3)
			{
				if(splitHeaders[0].equals("GET"))
				{
					String []splitHeaders1=splitHeaders[1].split("\\?");
					if(HTTPServer.getMapping().containsKey(splitHeaders1[0]))
					{
						ServiceHandler service=null;
						try
						{
							service=(ServiceHandler)HTTPServer.getMapping().get(splitHeaders1[0]).newInstance();
						}catch(Exception e)
						{
							e.printStackTrace();
						}
						nameValue=getNameValue(splitHeaders[1]);
						httpResponse=service.doGet(nameValue);
						responsePage(writer, httpResponse.getHeader(), httpResponse.getBody());
					}
					else
					{
						httpResponse=setResponse(HTTPServerResponse.getPageNotFoundStatus(), "NOT FOUND", "The requested page is not found", "text/html");
						responsePage(writer, httpResponse.getHeader(), httpResponse.getBody());
					}
				}
				else
				{
					httpResponse=setResponse(HTTPServerResponse.getMethodNotAllowedStatus(), "NOT ALLOWED", "The requested method is not allowed", "text/html");
					responsePage(writer, httpResponse.getHeader(), httpResponse.getBody());
				}
			}
			else
			{
				httpResponse=setResponse(HTTPServerResponse.getPageNotFoundStatus(), "NOT FOUND", "The requested page is not found", "text/html");
				responsePage(writer, httpResponse.getHeader(), httpResponse.getBody());
			}
		}catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	/**
	 * This method transfer the response bytes to the client
	 */
	public void responsePage(PrintWriter writer, String header, String body)
	{
		String headers=header + "\r\n";
		String page=body;		
		writer.write(headers);
		writer.write(page);
		writer.flush();
	}
	
	/**
	 * This method builds the response for each invalid type requests if the request is not directed to handlers
	 */
	public HTTPServerResponse setResponse(int statusCode, String message, String body, String content)
	{
		HTTPServerResponse response=new HTTPServerResponse();
		response.setStatusCode(statusCode, message);
		response.setDate(new Date());
		response.setContentType(content);
		response.setContentLength(body.length());
		response.setBody(body);
		
		return response;
	}
	
	/**
	 * This method extracts the name value pairs of each request
	 */
	public HashMap<String, String> getNameValue(String header)
	{
		HashMap<String, String> hMap=new HashMap<String, String>();
		String []splitHeaders=header.split("\\?");
		if(splitHeaders.length==1)
		{
			return hMap;
		}
		String []splitHeader=splitHeaders[1].split("&");
		for(String nameValue1: splitHeader)
		{
			String []nameValuePairs=nameValue1.split("=");
			if(nameValuePairs.length == 1)
			{
				hMap.put(nameValuePairs[0], "");
			}
			else
			{
				hMap.put(nameValuePairs[0], nameValuePairs[1]);
			}
		}
		return hMap;
	}
}