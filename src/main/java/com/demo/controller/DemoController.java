package com.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.dto.RequestModelDto;
import com.demo.exceptions.NotFoundInRedisException;
import com.demo.service.DemoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DemoController {

	private final DemoService demoService;

	@PostMapping("/predictCTR")
	@ResponseBody
	public ResponseEntity<String> predictCTR(@Valid @RequestBody RequestModelDto requestModelDto) throws NotFoundInRedisException{
		double result = demoService.predictCTR(requestModelDto);
		return ResponseEntity.status(HttpStatus.OK).body("The service predict a CTR of: " + result);
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<List<String>> handleException(MethodArgumentNotValidException ex) {
		log.error("ERROR: {}", ex.getMessage());
		List<String> errors = ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage)
				.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
	
	@ExceptionHandler({ NotFoundInRedisException.class })
	public ResponseEntity<String> handleException(NotFoundInRedisException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
}
