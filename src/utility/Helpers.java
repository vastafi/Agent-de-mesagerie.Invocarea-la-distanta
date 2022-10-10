package utility;

import com.google.gson.Gson;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.List;

public class Helpers {
    public static String formJSONMessage(String message, MessageTypes messageType, List<String> rec) throws ParserConfigurationException, TransformerException {
        Payload payload = new Payload(rec, messageType, message);
        return new Gson().toJson(payload);
    }
}
