package fr.jervill.springbatch.batchutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BatchJobListener implements JobExecutionListener {
    Logger logger = LoggerFactory.getLogger(BatchJobListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("Begining of job  " + jobExecution.getJobInstance().getJobName() + " at " + jobExecution.getStartTime());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String exitCode = jobExecution.getExitStatus().getExitCode();
        logger.info("End of job  " + jobExecution.getJobInstance().getJobName() + " at " + jobExecution.getEndTime() + " with status " + exitCode);
    }
}
