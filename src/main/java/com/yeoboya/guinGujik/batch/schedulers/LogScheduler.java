package com.yeoboya.guinGujik.batch.schedulers;

import com.yeoboya.guinGujik.batch.job.TaskletJob;
import com.yeoboya.guinGujik.batch.job.TaskletJob2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableScheduling // 스케쥴러 기능 활성화
@RequiredArgsConstructor
@Component
public class LogScheduler {

//	private final Job job;
	private final JobLauncher jobLauncher;
	private final TaskletJob taskletJob;
	private final TaskletJob2 taskletJo2;

	@Scheduled(fixedDelay = 30000)
	public void startJob() {
		JobParameters parameters = getJobParameters();

		try {

			JobExecution jobExecution = jobLauncher.run(taskletJob.orderJob(), parameters);

			while (jobExecution.isRunning()) {
				log.info("isRunning...");
			}
//			log.info("Job Execution: " + jobExecution.getStatus());
//			log.info("Job getJobConfigurationName: " + jobExecution.getJobConfigurationName());
//			log.info("Job getJobId: " + jobExecution.getJobId());
//			log.info("Job getExitStatus: " + jobExecution.getExitStatus());
//			log.info("Job getJobInstance: " + jobExecution.getJobInstance());
//			log.info("Job getStepExecutions: " + jobExecution.getStepExecutions());
//			log.info("Job getLastUpdated: " + jobExecution.getLastUpdated());
//			log.info("Job getFailureExceptions: " + jobExecution.getFailureExceptions());

		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
				 JobParametersInvalidException e) {
			e.printStackTrace();
		}
	}

	@Scheduled(fixedDelay = 30000)
	public void startJob2() {
		JobParameters parameters = getJobParameters();

		try {

			JobExecution jobExecution = jobLauncher.run(taskletJo2.orderJob2(), parameters);

			while (jobExecution.isRunning()) {
				log.info("isRunning...");
			}
//			log.info("Job Execution: " + jobExecution.getStatus());
//			log.info("Job getJobConfigurationName: " + jobExecution.getJobConfigurationName());
//			log.info("Job getJobId: " + jobExecution.getJobId());
//			log.info("Job getExitStatus: " + jobExecution.getExitStatus());
//			log.info("Job getJobInstance: " + jobExecution.getJobInstance());
//			log.info("Job getStepExecutions: " + jobExecution.getStepExecutions());
//			log.info("Job getLastUpdated: " + jobExecution.getLastUpdated());
//			log.info("Job getFailureExceptions: " + jobExecution.getFailureExceptions());

		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
				 JobParametersInvalidException e) {
			e.printStackTrace();
		}
	}

	private static JobParameters getJobParameters() {
		Map<String, JobParameter> jobParametersMap = new HashMap<>();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = new Date();
		String time1 = format1.format(time);
		jobParametersMap.put("requestDate", new JobParameter(time1));
		JobParameters parameters = new JobParameters(jobParametersMap);
		return parameters;
	}
}
