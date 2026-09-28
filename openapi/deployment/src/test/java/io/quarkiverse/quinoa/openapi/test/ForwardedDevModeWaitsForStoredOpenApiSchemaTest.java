package io.quarkiverse.quinoa.openapi.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.test.QuarkusDevModeTest;
import io.restassured.RestAssured;

public class ForwardedDevModeWaitsForStoredOpenApiSchemaTest {
    @RegisterExtension
    final static QuarkusDevModeTest test = new QuarkusDevModeTest()
            .withApplicationRoot((jar) -> jar
                    .addClass(SlowModelReader.class)
                    .add(new StringAsset(
                            "quarkus.quinoa=true\n" +
                                    "quarkus.quinoa.ui-dir=src/main/webui\n" +
                                    "quarkus.quinoa.dev-server.port=3000\n" +
                                    "quarkus.smallrye-openapi.store-schema-directory=src/main/webui\n" +
                                    "mp.openapi.model.reader=" + SlowModelReader.class.getName() + "\n"),
                            "application.properties"))
            .setCodeGenSources("webui");

    @Test
    public void testDevServerStartsWithFreshSchema() {
        RestAssured.when().get("/").then()
                .statusCode(200)
                .body(containsString(SlowModelReader.PATH), not(containsString("stale")));
    }
}
