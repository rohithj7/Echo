package echo;

import java.util.HashMap;

public class SafeTable extends HashMap<String, String> {

    private static SafeTable safeTable = new SafeTable();

    public static String search(String request) {
        synchronized (safeTable) {
            return safeTable.get(request);
        }
    }

    public static void update(String request, String response) {
        synchronized (safeTable) {
            safeTable.put(request, response);
        }
    }
}