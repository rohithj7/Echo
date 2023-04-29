package echo;

import java.net.Socket;

public class CacheHandler extends ProxyHandler {

    public CacheHandler() {
        super();
    }

    public CacheHandler(Socket s) {
        super(s);
    }

    protected String response(String request) throws Exception {
        String string = SafeTable.search(request);
        if (string != null) {
            System.out.println("Found in cache");
            return string;
        }
        else {
            peer.send(request);
            String result = peer.receive();
            SafeTable.update(request, result);
            return result;
        }
    }
}