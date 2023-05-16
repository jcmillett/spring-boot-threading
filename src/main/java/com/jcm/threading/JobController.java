package com.jcm.threading;

import com.jcm.threading.domain.Item;
import com.jcm.threading.domain.Job;
import com.jcm.threading.domain.JobStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private final JobService service;

    @Autowired
    public JobController(JobService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<Job> getJobs(@RequestParam(required = false, defaultValue = "UNPROCESSED") JobStatus jobStatus) {
        return service.getJobs(jobStatus);
    }

    @GetMapping("/{jobId}/items")
    public List<Item> getItemsForJob(@PathVariable String jobId) {
        UUID jobIdUuid = UUID.fromString(jobId);
        return service.getItemsForJob(jobIdUuid);
    }

    @PostMapping("")
    public void createJob(@RequestParam(required = false, defaultValue = "100") int numItems) {
        service.createJob(numItems);
    }
}
