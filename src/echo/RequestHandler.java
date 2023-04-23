package echo;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler extends Correspondent implements Runnable {
    protected boolean active; // response can set to false to terminate thread

    protected PrintWriter stdout;
    public RequestHandler(Socket s) {
        super(s);
        stdout = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(System.out)), true);
        active = true;
    }
    public RequestHandler() {
        super();
        stdout = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(System.out)), true);
        active = true;
    }
    // override in a subclass:
    protected String response(String msg) throws Exception {
        return "echo: " + msg;
    }
    // any housekeeping can be done by an override of this:
    protected void shutDown() {
        if (Server.DEBUG) System.out.println("handler shutting down");
    }
    public void run() {
        while(active) {
            try {
                String request = receive();
                stdout.println("received: " + request);
                if(request.equals("quit")) {
                    shutDown();
                    break;
                }
                stdout.println("sending: " + response(request));
                send(response(request));
                Thread.sleep(69);
                // send response
                // sleep
            } catch(Exception e) {
                send(e.getMessage() + "... ending session");
                break;
            }
        }
        // close
    }
}