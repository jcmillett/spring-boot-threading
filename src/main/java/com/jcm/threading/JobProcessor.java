package com.jcm.threading;

import com.jcm.threading.domain.Item;
import com.jcm.threading.domain.Job;
import com.jcm.threading.domain.JobStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class JobProcessor {
    private final JobService jobService;
    private final ExecutorService threadPool;

    public JobProcessor(JobService jobService) {
        this.jobService = jobService;
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    public void scheduler() {
        processJobs();
    }

    public void processJobs() {
        List<Job> unprocessedJobs = jobService.getJobs(JobStatus.UNPROCESSED);
        List<Job> inProgressJobs = jobService.getJobs(JobStatus.IN_PROGRESS);

        startProcessing(unprocessedJobs);
        checkInProgressStatus(inProgressJobs);
    }

    private void startProcessing(List<Job> unprocessedJobs) {
        log.info("Found {} jobs to process", unprocessedJobs.size());

        for (Job job : unprocessedJobs) {
            List<Item> items = jobService.getItemsForJob(job.jobId());
            jobService.updateJobStatus(job.jobId(), JobStatus.IN_PROGRESS);

            for (Item item : items) {
                threadPool.execute(() -> jobService.processItem(item.jobId(), item.itemId()));
            }
        }
    }

    private void checkInProgressStatus(List<Job> inProgressJobs) {
        log.info("Checking progress of {} jobs", inProgressJobs.size());

        for (Job job : inProgressJobs) {
            List<Item> items = jobService.getItemsForJob(job.jobId());
            boolean allProcessed = items.stream().allMatch(Item::processed);

            if (allProcessed) {
                log.info("Processing complete for job with id {}", job.jobId());
                jobService.updateJobStatus(job.jobId(), JobStatus.PROCESSED);
            }
        }
    }
}
