package echo;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.io.*;
import java.net.*;

public class Server {

    protected ServerSocket mySocket;
    protected int myPort;
    public static boolean DEBUG = true;

    protected Class<?> handlerType;

    public Server(int port, String handlerType) {
        try {
            myPort = port;
            mySocket = new ServerSocket(myPort);
            this.handlerType = (Class.forName(handlerType));
        } catch(Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } // catch
    }


    public void listen() {

        while(true) {
            // do try catch block here instead of the exceptions up top
            try {
                Socket socket = mySocket.accept();
                RequestHandler handler = makeHandler(socket);
                Thread t = new Thread(handler); // put element in thread
                t.start(); // start calls the run method of the element since its elements are runnable
                System.out.println("Server listening at port " + myPort);
            } catch(Exception e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
            // accept a connection
            // make handler
            // start handler in its own thread
        } // while

    }

    public RequestHandler makeHandler(Socket s) {

        RequestHandler handler = new RequestHandler(s);;
        try {
            handler = (RequestHandler) handlerType.newInstance();
            handler.setSocket(s);
        } catch(Exception e) {
            System.err.println(e.getMessage() + "Not possible");
        }
        return handler;

        // handler = a new instance of handlerType
        //    use: try { handlerType.getDeclaredConstructor().newInstance() } catch ...
        // set handler's socket to s
        // return handler
    }



    public static void main(String[] args) {
        int port = 4646;
        String service = "echo.RequestHandler";

        if (1 <= args.length) {
            service = args[0];
        }
        if (2 <= args.length) {
            port = Integer.parseInt(args[1]);
        }

        Server server = new Server(port, service);
        server.listen();
    }
}