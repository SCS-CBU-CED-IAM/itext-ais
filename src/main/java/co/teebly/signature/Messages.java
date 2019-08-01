package co.teebly.signature;

import java.util.Date;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import co.teebly.utils.files.FileReference;

public class Messages {

  public static final Gson GSON = createGson();

  private static Gson createGson() {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(FileReference.class, new FileReferenceJsonSerializer());
    builder.registerTypeAdapter(FileReference.class, new FileReferenceJsonDeserializer());
    builder.registerTypeAdapter(Date.class, new DateJsonSerializer());
    builder.registerTypeAdapter(Date.class, new DateJsonDeserializer());
    return builder.create();
  }
}
