package com.example.service.scheduler;

import java.util.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Job implements Runnable {
	public enum JobPriority {
		HIGH,
		MEDIUM,
		LOW
	}
	private Logger logger = Logger.getLogger(Job.class.getName());
	public String name;
	public JobPriority jobPriority;

	@Override
	public void run() {
		logger.info(">>> " + jobPriority + name);
	};

	public Job(String name, JobPriority jobPriority){
		this.name = name;
		this.jobPriority = jobPriority;
	}
}
