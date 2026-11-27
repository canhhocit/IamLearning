package com.iamlearning.modules.tasks.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateTaskCompletionRequest(
        @NotNull Boolean completed
) {
}
