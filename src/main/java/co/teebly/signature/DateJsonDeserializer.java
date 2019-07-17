package co.teebly.signature;

import java.lang.reflect.Type;
import java.util.Date;
import javax.xml.bind.DatatypeConverter;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class DateJsonDeserializer implements JsonDeserializer<Date> {

  @Override
  public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    if (json == null) {
      return null;
    }
    try {
      return DatatypeConverter.parseDateTime(json.getAsString()).getTime();
    } catch (IllegalArgumentException e) {
      throw new JsonParseException(e.getMessage(), e);
    }
  }
}
