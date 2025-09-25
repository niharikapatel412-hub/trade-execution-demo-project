package com.example.fx.order.management.client;

import java.util.HashMap;
import java.util.Map;

public class FixMessage {
private final Map<String, String> fields = new HashMap<>();

public void setField(String tag, String value) {fields.put(tag, value);}

public String getField(String tag) {return fields.get(tag);}

public String getMsgType() {return getField("35");}

// Serialize to FIX string: tag=value|tag=value|...
public String serialize() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : fields.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("|");
        }
        return sb.toString();
}

// Parse from FIX string
public static FixMessage parse(String fixStr) {
        FixMessage msg = new FixMessage();
        String[] tokens = fixStr.split("\\|");
        for (String token : tokens) {
                if (token.isEmpty()) continue;
                String[] kv = token.split("=", 2);
                if (kv.length == 2) {
                        msg.setField(kv[0], kv[1]);
                }
        }
        return msg;
}

}
