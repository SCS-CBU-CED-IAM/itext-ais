package co.teebly.signature;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import javax.xml.bind.DatatypeConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateJsonSerializer implements JsonSerializer<Date> {

  @Override
  public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
    if (src == null) {
      return null;
    }
    Calendar val = Calendar.getInstance();
    val.setTime(src);
    return new JsonPrimitive(DatatypeConverter.printDateTime(val));
  }
}
