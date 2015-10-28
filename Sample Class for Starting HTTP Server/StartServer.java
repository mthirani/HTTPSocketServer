/**
 * Here you can build your databases/ datastructures (if any) and map your handlers to the server
 * Also start the server by passing the port number of the server where the server socket is listening
 */
public class StartServer 
{	
	public static void main(String[] args) 
	{
		if(args.length==1)
		{
			Integer port=Integer.parseInt(args[0]);
			HTTPServer httpServer=new HTTPServer();
			httpServer.addClass("/businesses", BusinessHandler.class); //Mapping of BusinessHanlder to /business url
			httpServer.addClass("/rating", RatingHandler.class); //Mapping of RatingHanlder to /rating url
			httpServer.addClass("/reviewers", ReviewerHandler.class);	//Mapping of ReviewerHandler to /reviewers url
			httpServer.start(port);
		}
		else
		{
			System.err.println("Please Pass Two Parameters in the Command Line: <Input-Path> and <Server-Port>");
		}
	}
}