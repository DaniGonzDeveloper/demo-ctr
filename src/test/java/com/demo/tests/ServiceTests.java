package com.demo.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.demo.dto.RequestModelDto;
import com.demo.exceptions.NotFoundInRedisException;
import com.demo.service.DemoService;

@ExtendWith(MockitoExtension.class)
public class ServiceTests {

	@Mock
	private RedisTemplate<String, Double> redisTemplate;

	@Mock
	private HashOperations<String, Object, Object> hashOperations;

	@InjectMocks
	private DemoService demoService;

	private static final String MODEL_KEY = "model";
	private static final String BIAS_HASH_KEY = "bias";

	@Test
	void testNotFoundInDB() throws Exception {
		when(redisTemplate.opsForHash()).thenReturn(hashOperations);
		RequestModelDto requestModel = new RequestModelDto("Firefox", "300x250", "cs", "car");
		when(hashOperations.multiGet(MODEL_KEY, requestModel.getRedisHashKey()))
				.thenReturn(Arrays.asList("", "", "", null));
		Exception e = assertThrows(NotFoundInRedisException.class, () -> demoService.predictCTR(requestModel));
		assertThat(e.getMessage().contains("Eeey!! Some parameters were not found in DB"), is(Boolean.TRUE));
	}

	@Test
	void testCorrectValue() throws Exception {
		when(redisTemplate.opsForHash()).thenReturn(hashOperations);
		RequestModelDto requestModel = new RequestModelDto("Firefox", "300x250", "cs", "car");
		when(hashOperations.multiGet(MODEL_KEY, requestModel.getRedisHashKey()))
				.thenReturn(Arrays.asList(-0.1131013246, -0.6282185905, -0.1935418172, 0.7294739471));
		when(hashOperations.get(MODEL_KEY, BIAS_HASH_KEY)).thenReturn(0.1);
		double result = demoService.predictCTR(requestModel);
		assertTrue(result<1&&result>0);
	}
}
