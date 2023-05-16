package com.jcm.threading.domain;

import java.util.UUID;

public record Job(UUID jobId, JobStatus status) {
}
