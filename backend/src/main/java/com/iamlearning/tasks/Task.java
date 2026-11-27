package com.iamlearning.tasks;

import java.util.UUID;

public record Task(UUID id, String title, boolean completed) {
}
