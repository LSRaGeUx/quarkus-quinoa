package io.quarkiverse.quinoa.openapi.test;

import static io.quarkiverse.quinoa.deployment.testing.QuinoaQuarkusUnitTest.getWebUITestDirPath;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkiverse.quinoa.deployment.testing.QuinoaQuarkusUnitTest;
import io.quarkus.test.QuarkusExtensionTest;

public class QuinoaWaitsForStoredOpenApiSchemaTest {

    private static final String NAME = "openapi-store-schema";

    @RegisterExtension
    static final QuarkusExtensionTest config = QuinoaQuarkusUnitTest.create(NAME)
            .noLockfile()
            .toQuarkusExtensionTest()
            .withApplicationRoot(jar -> jar.addClass(SlowModelReader.class))
            .overrideConfigKey("mp.openapi.model.reader", SlowModelReader.class.getName())
            .overrideConfigKey("quarkus.smallrye-openapi.store-schema-directory",
                    getWebUITestDirPath(NAME).toAbsolutePath().toString())
            .assertLogRecords(l -> assertThat(l)
                    .anyMatch(s -> s.getMessage().equals("Quinoa waited for: %s")
                            && s.getParameters()[0].equals("SmallRye OpenAPI stored schema")));

    @Test
    public void testWebUIBuildReadsFreshSchema() {
        assertThat(Path.of("target/quinoa/build/openapi.yaml")).isRegularFile()
                .content().contains(SlowModelReader.PATH).doesNotContain("stale");
    }
}
