package io.quarkiverse.quinoa.openapi.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkiverse.quinoa.deployment.testing.QuinoaQuarkusUnitTest;
import io.quarkus.test.QuarkusExtensionTest;

/**
 * Without a stored schema, the Web UI build must keep running in parallel with the OpenAPI build.
 */
public class QuinoaNoStoredOpenApiSchemaTest {

    private static final String NAME = "openapi-no-store-schema";

    @RegisterExtension
    static final QuarkusExtensionTest config = QuinoaQuarkusUnitTest.create(NAME)
            .noLockfile()
            .toQuarkusExtensionTest()
            .assertLogRecords(l -> assertThat(l)
                    .noneMatch(s -> s.getMessage().equals("Quinoa waited for: %s")));

    @Test
    public void testWebUIBuildDoesNotWait() {
        assertThat(Path.of("target/quinoa/build/index.html")).isRegularFile().hasContent("test");
    }
}
