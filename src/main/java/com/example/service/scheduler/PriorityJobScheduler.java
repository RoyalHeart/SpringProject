package com.example.service.scheduler;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.example.service.scheduler.Job.JobPriority;

public class PriorityJobScheduler {

    private static Logger logger = Logger.getLogger(PriorityJobScheduler.class.getName());
    private ExecutorService priorityJobPoolExecutor;
    private ExecutorService priorityJobScheduler = Executors.newSingleThreadExecutor();
    private PriorityBlockingQueue<Job> priorityQueue;

    public PriorityJobScheduler(Integer poolSize, Integer queueSize) {
        priorityJobPoolExecutor = Executors.newFixedThreadPool(poolSize);
        priorityQueue = new PriorityBlockingQueue<Job>(
                queueSize,
                Comparator.comparing(Job::getJobPriority));
        priorityJobScheduler.execute(() -> {
            while (true) {
                priorityJobPoolExecutor.execute(priorityQueue.poll());
            }
        });
    }

    public void scheduleJob(Job job) {
        priorityQueue.add(job);
    }

    public static void test() throws InterruptedException {
        Job job1 = new Job("Job1", JobPriority.LOW);
        Job job2 = new Job("Job2", JobPriority.MEDIUM);
        Job job3 = new Job("Job3", JobPriority.HIGH);
        Job job4 = new Job("Job4", JobPriority.MEDIUM);
        Job job5 = new Job("Job5", JobPriority.LOW);
        Job job6 = new Job("Job6", JobPriority.HIGH);
        Job job7 = new Job("Job7", JobPriority.MEDIUM);
        Job job8 = new Job("Job8", JobPriority.HIGH);

        PriorityJobScheduler pjs = new PriorityJobScheduler(
                1, 10);

        pjs.scheduleJob(job1);
        pjs.scheduleJob(job2);
        pjs.scheduleJob(job3);
        pjs.scheduleJob(job4);
        pjs.scheduleJob(job5);
        pjs.scheduleJob(job6);
        pjs.scheduleJob(job7);
        pjs.scheduleJob(job8);
    }
}
