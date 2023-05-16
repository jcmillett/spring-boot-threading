package com.jcm.threading.domain;

import java.util.UUID;

public record Item(UUID jobId, int itemId, boolean processed) {
}
