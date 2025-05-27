package com.pioneerPixel.BankService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Value("${async.thread-pool.size:5}")
    private int threadPoolSize;

    @Value("${async.thread-pool.queue-capacity:50}")
    private int queueCapacity;

    @Bean(name = "balance")
    public Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolSize);
        executor.setMaxPoolSize(threadPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("Executor-Thread-");
        executor.initialize();
        return executor;
    }
}
