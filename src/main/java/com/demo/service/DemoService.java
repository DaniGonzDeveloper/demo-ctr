package com.demo.service;

import java.util.List;
import java.util.Objects;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.demo.dto.RequestModelDto;
import com.demo.exceptions.NotFoundInRedisException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DemoService {

	private final RedisTemplate<String, Double> redisTemplate;
	private static final String MODEL_KEY = "model";
	private static final String BIAS_HASH_KEY = "bias";

	public double predictCTR(RequestModelDto requestModelDto) throws NotFoundInRedisException{
		Double result;
		List<Object> list = redisTemplate.opsForHash().multiGet(MODEL_KEY, requestModelDto.getRedisHashKey());
		boolean containsNull = list.stream().anyMatch(Objects::isNull);
		if(containsNull) {
			throw new NotFoundInRedisException("Eeey!! Some parameters were not found in DB");
		}
		double totalSum = list.stream().mapToDouble(f -> (double)f).sum();
		totalSum += (Double) redisTemplate.opsForHash().get(MODEL_KEY, BIAS_HASH_KEY);
		double exp = Math.exp(totalSum);
		result = exp / (1 + exp);
		return result;
	}

}
