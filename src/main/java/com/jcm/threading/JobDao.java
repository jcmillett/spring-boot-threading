package com.jcm.threading;

import com.jcm.threading.domain.Item;
import com.jcm.threading.domain.Job;
import com.jcm.threading.domain.JobStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@Slf4j
public class JobDao {
    private final JdbcTemplate jdbcTemplate;

    public JobDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void createJob(Job job, List<Item> items) {
        String jobSql = """
                insert into jobs (jobId, status)
                values (?, ?)
                """;

        String itemSql = """
                insert into items (jobId, itemId, processed)
                values (?, ?, ?)
                """;

        List<Object[]> itemArgs = new ArrayList<>();

        items.forEach(item -> itemArgs.add(new Object[] { item.jobId(), item.itemId(), false }));

        try {
            jdbcTemplate.update(jobSql, job.jobId(), JobStatus.UNPROCESSED.toString());
            jdbcTemplate.batchUpdate(itemSql, itemArgs);
        }
        catch (Exception ex) {
            log.warn("Error creating job", ex);
        }
    }

    public List<Job> getJobs(JobStatus jobStatus) {
        String sql = """
                select *
                from jobs
                where status = ?
                """;

        try {
            return jdbcTemplate.query(sql, (rs, id) -> {
                UUID jobId = UUID.fromString(rs.getString("jobId"));
                JobStatus status = JobStatus.valueOf(rs.getString("status"));

                return new Job(jobId, status);
            }, jobStatus.toString());
        }
        catch (Exception ex) {
            log.warn("Unable to get jobs with status {}", jobStatus, ex);
            return List.of();
        }
    }

    public List<Item> getItemsForJob(UUID jobId) {
        String sql = """
                select *
                from items
                where jobId = ?
                """;

        try {
            return jdbcTemplate.query(sql, (rs, id) -> {
                UUID jobIdResult = UUID.fromString(rs.getString("jobId"));
                int itemId = rs.getInt("itemId");
                boolean processed = rs.getBoolean("processed");

                return new Item(jobIdResult, itemId, processed);
            }, jobId);
        }
        catch (Exception ex) {
            log.warn("Unable to get job items for job id {}", jobId, ex);
            return List.of();
        }
    }

    public void setJobStatus(UUID jobId, JobStatus status) {
        String sql = """
                update jobs
                set status = ?
                where jobId = ?
                """;

        try {
            jdbcTemplate.update(sql, status.toString(), jobId);
        }
        catch (Exception ex) {
            log.warn("Unable to update job {} status", jobId, ex);
        }
    }

    public void setItemProcessed(UUID jobId, int itemId) {
        String sql = """
                update items
                set processed = true
                where jobId = ? and itemId = ? 
                """;

        try {
            jdbcTemplate.update(sql, jobId, itemId);
        }
        catch (Exception ex) {
            log.warn("Unable to set item {} as processed", itemId, ex);
        }
    }
}
