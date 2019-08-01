package co.teebly.signature;

import static org.junit.Assert.assertEquals;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import co.teebly.utils.files.FileReference;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessagesTest {

  @SuppressWarnings("unused")
  private static final Logger LOG = LoggerFactory.getLogger(MessagesTest.class);

  private static SignatureRequest createSignatureRequest() throws URISyntaxException {
    FileReference fileRefIn =
        FileReference.createFileReference(new URI("file:///some/dir/some-file-in.pdf"));
    FileReference fileRefSigned =
        FileReference.createFileReference(new URI("file:///some/dir/some-file-out.pdf"));
    FileReference fileRefSignatureAppearance = FileReference
        .createFileReference(new URI("file:///some/dir/some-file-signature-appearance.png"));
    return new SignatureRequest( //
        "sig.request.54321", //
        "docId", //
        "Max Musterman", //
        "Maximilian Rudolph", //
        "Musterman", //
        "en", //
        "447708216475", //
        "GB", //
        "miro@teebly.co", //
        fileRefIn, //
        fileRefSigned, //
        fileRefSignatureAppearance, //
        "theUserId", //
        false //
    );
  }

  private static SignatureResponse createSignatureResponse() throws URISyntaxException {
    return new SignatureResponse(createSignatureRequest(), SignatureResponse.Result.OK,
        "resultMessage");
  }

  private static SignatureResultPollRequest createSignatureResultPollRequest()
      throws URISyntaxException {
    return new SignatureResultPollRequest(createSignatureRequest(),
        new Date(System.currentTimeMillis() + 300000), 5000, "reportHandle");
  }

  @Test
  public void testSignatureRequest() throws Exception {
    SignatureRequest fromScratch = createSignatureRequest();
    String fromScratchSerialized = fromScratch.toString();
    SignatureRequest deserialized = SignatureRequest.fromJson(fromScratchSerialized);
    String deserializedSerialized = deserialized.toString();
    assertEquals(fromScratchSerialized, deserializedSerialized);
  }

  @Test
  public void testSignatureResponse() throws Exception {
    SignatureResponse fromScratch = createSignatureResponse();
    String fromScratchSerialized = fromScratch.toString();
    SignatureResponse deserialized = SignatureResponse.fromJson(fromScratchSerialized);
    String deserializedSerialized = deserialized.toString();
    assertEquals(fromScratchSerialized, deserializedSerialized);
  }

  @Test
  public void testSignatureResultPollRequest() throws Exception {
    SignatureResultPollRequest fromScratch = createSignatureResultPollRequest();
    String fromScratchSerialized = fromScratch.toString();
    SignatureResultPollRequest deserialized =
        SignatureResultPollRequest.fromJson(fromScratchSerialized);
    String deserializedSerialized = deserialized.toString();
    assertEquals(fromScratchSerialized, deserializedSerialized);
  }
}
