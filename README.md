# HTTPSocketServer
This package can be used as a HTTP Server (Socket) which needs only the argument containing the Port number where you need to start the server and your handlers or APIs to define how you want to handle your requests in URL.
Basic Defintions of the Classes are below:
1. HTTPServer has the maping class function which will map your handlers/ APIs to the Requests sepcified and will create a seperate thread for each request using ThreadPool
2. ServiceHanlder class provides an interface which contains one public abstract method which needs to be defined by your handler APIs and returns an apropriate HTTPServerResponse
3. HTTPServerResponse creates the Response header and body to be sent back to client via Sockets.

