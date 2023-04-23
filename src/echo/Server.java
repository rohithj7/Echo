package echo;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.io.*;
import java.net.*;

public class Server {

    protected ServerSocket mySocket;
    protected int myPort;
    public static boolean DEBUG = true;

    protected PrintWriter stdout;
    protected Class<?> handlerType;

    public Server(int port, String handlerType) {
        stdout = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(System.out)), true);
        try {
            myPort = port;
            mySocket = new ServerSocket(myPort);
            stdout.println("Server listening at port " + port);
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

        try {
            RequestHandler handler = (RequestHandler) handlerType.newInstance();
            try {
                handlerType.getDeclaredConstructor().newInstance();
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            handler.setSocket(s);
            return handler;
        } catch(Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
        // handler = a new instance of handlerType
        //    use: try { handlerType.getDeclaredConstructor().newInstance() } catch ...
        // set handler's socket to s
        // return handler
    }



    public static void main(String[] args) {
        int port = 5555;
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