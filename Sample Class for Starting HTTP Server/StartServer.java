/**
 * Here you can build your databases/ datastructures (if any) and map your handlers to the server
 * Also start the server by passing the port number of the server where the server socket is listening
 */
public class StartServer 
{	
	private static MyStore store;
	
	public StartServer(String inputPath)
	{
		store=StoreBuilder.concurrentBuild(inputPath);
	}
	
	public static MyStore getStore()
	{
		return store;
	}
	
	public static void main(String[] args) 
	{
		if(args.length==1)
		{
			MyStore createMyStore=new MyStore(args[0]);
			Integer port=Integer.parseInt(args[1]);
			HTTPServer httpServer=new HTTPServer();
			httpServer.addClass("/businesses", BusinessHandler.class); //Mapping of BusinessHanlder to /business url(For e.g: http://localhost:8080/businesses)
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