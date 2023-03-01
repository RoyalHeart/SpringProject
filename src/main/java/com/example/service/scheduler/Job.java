package com.example.service.scheduler;

import java.util.logging.Logger;

import lombok.Data;

@Data
public class Job implements Runnable {
	public enum JobPriority {
		HIGH,
		MEDIUM,
		LOW
	}

	private static Logger logger = Logger.getLogger(Job.class.getName());
	public String name;
	public JobPriority jobPriority;

	@Override
	public void run() {
		logger.info(">>> " + jobPriority + name);
	};

	public Job(String name, JobPriority jobPriority) {
		this.name = name;
		this.jobPriority = jobPriority;
	}
}
