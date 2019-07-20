package co.teebly.signature;

import java.io.File;
import co.teebly.utils.internal.UnitTestSettings;
import co.teebly.utils.mongo.TeeblyMongoDatabase;
import co.teebly.utils.mongo.TeeblyMongoSubsystem;

/**
 * <b>IMPURTANT!!!</b>
 * <p>
 * <b>NEVER EVER STORE TEEBLY KEYS AND CERTIFICATES ON GIT!!!</b>
 * <p>
 * <b>IMPURTANT!!!</b>
 * 
 * <p>
 * To run the tests locally:
 * <ul>
 * <li>Set environment variable {@code TEEBLY_MONGODB_DB}</li>
 * <li>Set environment variable {@code TEEBLY_MONGODB_URI}</li>
 * <li>Have file {@code _mycert.crt} in project</li>
 * <li>Have file {@code _mycert.key} in project</li>
 * </ul>
 */
public class TestContext {

  public static final File CRT_FILE = new File("_mycert.crt");

  private static TestContext instance;

  public static final File KEY_FILE = new File("_mycert.key");

  public static synchronized TestContext instance() {
    if (instance == null) {
      instance = new TestContext();
    }
    return instance;
  }

  private TeeblyMongoDatabase teeblyDatabase;

  private TestContext() {

    teeblyDatabase = TeeblyMongoDatabase.create(TeeblyMongoSubsystem.TEEBLY, false);
    if (teeblyDatabase == null) {
      if (UnitTestSettings.instance().isTeeblyJunitForceDbTests()) {
        throw new IllegalStateException(
            "Environment variable '" + UnitTestSettings.TEEBLY_JUNIT_FORCE_DB_TESTS_ENV
                + "' is set to true but TEEBLY DB is not configured (see env-vars "
                + TeeblyMongoSubsystem.TEEBLY.getEnvDatabase() + " and "
                + TeeblyMongoSubsystem.TEEBLY.getEnvUri() + ")");
      }
    }
  }

  public TeeblyMongoDatabase getTeeblyDatabase() {
    return teeblyDatabase;
  }
}
