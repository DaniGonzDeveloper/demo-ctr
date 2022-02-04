package com.demo.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.demo.controller.DemoController;
import com.demo.dto.RequestModelDto;
import com.demo.exceptions.NotFoundInRedisException;
import com.demo.service.DemoService;

@WebMvcTest(DemoController.class)
public class ControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RedisTemplate<String, Double> redisTemplate;

	@MockBean
	private DemoService demoServiceMock;

	@Test
	public void givenBadArguments_thenBadRequest() throws Exception{
		String exceptionParam = "{" + "\"deviceExtBrowser\": \"Firefox\"," + "\"bannerExtSize\": \"300x250\","
				+ "\"deviceLanguage\": \"cs\"," + "\"test\": \"tablet\"" + "}";
		mockMvc.perform(post("/predictCTR").contentType(MediaType.APPLICATION_JSON).content(exceptionParam))
				.andExpect(status().isBadRequest()).andExpect(
						result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
	}

	@Test
	public void givenGoodArguments_thenNotExistInDB() throws Exception {
		String exceptionParam = "{" + "\"deviceExtBrowser\": \"Firefox\"," + "\"bannerExtSize\": \"300x250\","
				+ "\"deviceLanguage\": \"cs\"," + "\"deviceExtType\": \"car\"" + "}";
		when(demoServiceMock.predictCTR(any(RequestModelDto.class))).thenThrow(NotFoundInRedisException.class);
		mockMvc.perform(post("/predictCTR").contentType(MediaType.APPLICATION_JSON).content(exceptionParam))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundInRedisException));
	}
	
	@Test
	public void givenGoodArguments() throws Exception {
		String exceptionParam = "{" + "\"deviceExtBrowser\": \"Firefox\"," + "\"bannerExtSize\": \"300x250\","
				+ "\"deviceLanguage\": \"cs\"," + "\"deviceExtType\": \"tablet\"" + "}";
		when(demoServiceMock.predictCTR(any(RequestModelDto.class))).thenReturn(0.0);
		mockMvc.perform(post("/predictCTR").contentType(MediaType.APPLICATION_JSON).content(exceptionParam))
				.andExpect(status().isOk());
	}
}
