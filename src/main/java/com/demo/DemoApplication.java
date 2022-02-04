package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
@SpringBootApplication()
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

    @Bean
    public RedisTemplate<String, Double> byteTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Double> doubleTemplate = new RedisTemplate<>();
        doubleTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Double.class));
        doubleTemplate.setHashKeySerializer(new StringRedisSerializer());
        doubleTemplate.setKeySerializer(new StringRedisSerializer());
//        doubleTemplate.setDefaultSerializer(new StringRedisSerializer());
        
        doubleTemplate.setConnectionFactory(redisConnectionFactory);
        return doubleTemplate;
    }
}
