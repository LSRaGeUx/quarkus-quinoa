package io.quarkiverse.quinoa.openapi.deployment;

import java.util.function.BooleanSupplier;

import io.quarkiverse.quinoa.deployment.items.QuinoaBuildPrerequisiteBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.Consume;
import io.quarkus.smallrye.openapi.common.deployment.SmallRyeOpenApiConfig;
import io.quarkus.smallrye.openapi.deployment.spi.OpenApiDocumentBuildItem;

class QuinoaOpenApiProcessor {

    /**
     * SmallRye OpenAPI writes the stored schema files before producing {@link OpenApiDocumentBuildItem},
     * so once it is produced the Web UI build (e.g. a codegen tool like Orval) can safely read them.
     * <p>
     * Only enabled when a schema is stored: otherwise the Web UI build keeps running in parallel with the Java build.
     */
    @BuildStep(onlyIf = StoresOpenApiSchema.class)
    @Consume(OpenApiDocumentBuildItem.class)
    QuinoaBuildPrerequisiteBuildItem waitForStoredOpenApiSchema() {
        return new QuinoaBuildPrerequisiteBuildItem("SmallRye OpenAPI stored schema");
    }

    static class StoresOpenApiSchema implements BooleanSupplier {
        SmallRyeOpenApiConfig openApiConfig;

        @Override
        public boolean getAsBoolean() {
            return openApiConfig.documents().values().stream()
                    .anyMatch(document -> document.storeSchemaDirectory().isPresent());
        }
    }
}
