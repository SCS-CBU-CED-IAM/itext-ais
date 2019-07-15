package co.teebly.signature;

import java.util.HashMap;
import java.util.Map;

// TODO these are callbacks that will eventually talk to the Teebly UI
public class WorkQueue {
    private static Map<String, SignatureRequest> id2request;
    private static Map<String, String> id2url;

    synchronized public static void init() {
        id2request = new HashMap<>();
        id2url = new HashMap<>();
    }

    synchronized public static void register(SignatureRequest sr) {
        if (id2request == null) init();
        id2request.put(sr.getId(), sr);
        id2url.put(sr.getId(), "");
    }

    synchronized public static void addConsentUrl(String id, String url) {
        id2url.put(id, url);
    }

    synchronized public static void setGotSignature(String id) {
        System.out.println("Got signature for" + id);
    }

    synchronized public static void setAppliedSignature(String id) {
        System.out.println("Applied signature for" + id);
    }
}
