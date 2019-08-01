package co.teebly.signature;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import co.teebly.utils.files.FileReference;

public class FileReferenceJsonDeserializer implements JsonDeserializer<FileReference> {

  @Override
  public FileReference deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {
    if (json == null) {
      return null;
    }
    try {
      return FileReference.createFileReference(new URI(json.getAsString()));
    } catch (URISyntaxException e) {
      throw new JsonParseException(e.getMessage(), e);
    }
  }
}
