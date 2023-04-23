package math;

import echo.RequestHandler;

import java.net.Socket;

public class MathHandler extends RequestHandler {

    public MathHandler(Socket sock) {
        super(sock);
    }

    public MathHandler() {
        super();
    }

    protected String response(String request) throws Exception {

        String result = "";
        String[] tokens = request.split(" ");

        if(tokens.length > 2) {
            switch (tokens[0]) {
                case "mul":
                    result = "" + 1;
                    for (int i = 1; i < tokens.length; i++) {
                        result = String.valueOf(Integer.parseInt(result) * Integer.parseInt(tokens[i]));
                    }
                    break;
                case "add":
                    result = "" + 0;
                    for (int i = 1; i < tokens.length; i++) {
                        result = String.valueOf(Integer.parseInt(result) + Integer.parseInt(tokens[i]));
                    }
                    break;
                case "sub":
                    result = "" + 0;
                    result = String.valueOf(Integer.parseInt(tokens[1]));
                    if(tokens.length > 3) {
                        for (int i = 2; i < tokens.length; i++) {
                            result = String.valueOf(Integer.parseInt(result) - Integer.parseInt(tokens[i]));
                        }
                    }
                    break;
                default:
                    result = "Unrecognized command: " + request;
                    break;
            }
        } else {
            result = "Unrecognized command: " + request;
        }

        return result;

    }

}
