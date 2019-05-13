package com.oruc.logconsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.annotation.KafkaListener;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@SpringBootApplication
@EnableAspectJAutoProxy
public class LogConsumerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LogConsumerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
	}
	
	@Autowired
	MeterRegistry registry;
	
	@Bean
    TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
	
	@Timed(longTask=true, percentiles= {0.5, 0.7, 0.9}, histogram=true, value="log_city_histogram")
	@KafkaListener(topics = "log", groupId = "group-monitoring")
    public void listen(ConsumerRecord<?, ?> cr) throws Exception {
        System.out.println(cr.value().toString());
        Counter cityCounter = registry.counter("log_city_counter", "city", cr.value().toString().split(" ")[3]);
        cityCounter.increment();
        
    }

}
