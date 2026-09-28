package io.quarkiverse.quinoa.deployment.items;

import java.util.Objects;

import io.quarkus.builder.item.MultiBuildItem;

/**
 * Makes the Web UI build (and the dev server start) wait for another build step.
 * <p>
 * Produce it from a step that writes files the Web UI build reads, such as a generated OpenAPI schema,
 * so that the package manager never runs before those files are on disk.
 */
public final class QuinoaBuildPrerequisiteBuildItem extends MultiBuildItem {

    private final String description;

    /**
     * @param description what the Web UI build waits for, shown in the Quinoa logs
     */
    public QuinoaBuildPrerequisiteBuildItem(String description) {
        this.description = Objects.requireNonNull(description, "description is required");
    }

    public String getDescription() {
        return description;
    }
}
