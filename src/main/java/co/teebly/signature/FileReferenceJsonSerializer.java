package co.teebly.signature;

import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import co.teebly.utils.files.FileReference;

public class FileReferenceJsonSerializer implements JsonSerializer<FileReference> {

  @Override
  public JsonElement serialize(FileReference src, Type typeOfSrc,
      JsonSerializationContext context) {
    if (src == null) {
      return null;
    }
    return new JsonPrimitive(src.toString());
  }
}
