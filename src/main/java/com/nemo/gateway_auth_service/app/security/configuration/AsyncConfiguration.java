package com.nemo.gateway_auth_service.app.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfiguration implements AsyncConfigurer {

    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {

        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(5);
        threadPoolExecutor.setMaxPoolSize(10);
        threadPoolExecutor.setQueueCapacity(25);
        threadPoolExecutor.initialize();
        return threadPoolExecutor;
    }

    @Bean(name = "mailTaskExecutor")
    public Executor mailTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);  // Поменьше, чтобы не спамить
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(500); // Очередь побольше, письма могут подождать
        executor.setThreadNamePrefix("MailAsync-");
        executor.initialize();
        return executor;
    }
}
