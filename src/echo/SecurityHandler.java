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

        String result = "";

        if(loggedIn) {
            result = super.response(request);
        } else {
            String[] tokens = request.split("\\s+");
            if(tokens[0].equalsIgnoreCase("new")) {
                if(search(tokens[1]) != null) {
                    result = "User name taken";
                } else {
                    update(tokens[1], tokens[2]);
                    result = "Account created";
                }
            } else if(tokens[0].equalsIgnoreCase("login")) {
                if(search(tokens[1]).equals(tokens[2])) {
                    loggedIn = true;
                    result = "Login successful";
                } else {
                    result = "Login failed";
                }
            } else {
                result = "Please log in";
            }
        }
        return result;

    }

}
