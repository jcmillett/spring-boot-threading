package com.jcm.threading;

import com.jcm.threading.domain.Item;
import com.jcm.threading.domain.Job;
import com.jcm.threading.domain.JobStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class JobService {
    private final JobDao jobDao;

    @Autowired
    public JobService(JobDao jobDao) {
        this.jobDao = jobDao;
    }

    public List<Job> getJobs(JobStatus status) {
        return jobDao.getJobs(status);
    }

    public List<Item> getItemsForJob(UUID jobId) {
        return jobDao.getItemsForJob(jobId);
    }

    public void createJob(int numItems) {
        UUID jobId = UUID.randomUUID();
        Job job = new Job(jobId, JobStatus.UNPROCESSED);
        List<Item> items = new ArrayList<>();

        for (int i = 0; i < numItems; ++i) {
            items.add(new Item(jobId, i, false));
        }

        jobDao.createJob(job, items);
    }

    public void updateJobStatus(UUID jobId, JobStatus status) {
        jobDao.setJobStatus(jobId, status);
    }

    public void processItem(UUID jobId, int itemId) {
        try {
            TimeUnit.SECONDS.sleep(1);
            jobDao.setItemProcessed(jobId, itemId);
            // log.info("Processed item with id: {}", itemId);
        }
        catch (Exception ex) {
            log.warn("Unable to sleep thread", ex);
        }
    }
}
