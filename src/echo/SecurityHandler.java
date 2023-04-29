package echo;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SecurityHandler extends ProxyHandler {

    private static Map<String, String> users = new HashMap<>();

    private String search(String request) {
        synchronized (users) {
            return users.get(request);
        }
    }

    private void update (String request, String response) {
        synchronized (users) {
            users.put(request, response);
        }
    }

    private Boolean loggedIn;

    public SecurityHandler(Socket s) {
        super(s);
        loggedIn = false;
    }

    public SecurityHandler() {
        super();
        loggedIn = false;
    }

    protected String response(String request) throws Exception {

        if(loggedIn) {
            return super.response(request);
        } else {
            String[] tokens = request.split("\\s+");
            if(tokens[0].equalsIgnoreCase("new")) {
                loggedIn = false;
                if(search(tokens[1]) == null) {
                    update(tokens[1], tokens[2]);
                    return "Account created";
                } else {
                    return "User not found";
                }
            } else if(tokens[0].equalsIgnoreCase("login")) {
                if(search(tokens[1]).equals(tokens[2])) {
                    loggedIn = true;
                    return "Login successful";
                } else {
                    loggedIn = false;
                    return "Login failed";
                }
            } else {
                loggedIn = false;
                return "Please log in";
            }
        }

    }

}
