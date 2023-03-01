package com.example;
import org.junit.jupiter.api.Test;

import com.example.service.scheduler.Job;
import com.example.service.scheduler.Job.JobPriority;
import com.example.service.scheduler.PriorityJobScheduler;

public class TestPriorityScheduler {

  @Test
  public void whenMultiplePriorityJobsQueued_thenHighestPriorityJobIsPicked() {
    Job job1 = new Job("Job1", JobPriority.LOW);
    Job job2 = new Job("Job2", JobPriority.MEDIUM);
    Job job3 = new Job("Job3", JobPriority.HIGH);
    Job job4 = new Job("Job4", JobPriority.MEDIUM);
    Job job5 = new Job("Job5", JobPriority.LOW);
    Job job6 = new Job("Job6", JobPriority.HIGH);

    PriorityJobScheduler pjs = new PriorityJobScheduler(
        1, 10);

    pjs.scheduleJob(job1);
    pjs.scheduleJob(job2);
    pjs.scheduleJob(job3);
    pjs.scheduleJob(job4);
    pjs.scheduleJob(job5);
    pjs.scheduleJob(job6);
  }
}
