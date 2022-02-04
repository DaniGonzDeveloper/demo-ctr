package com.demo.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

@Getter
public class RequestModelDto {
	
	private static final String DEVICE_EXT_BROWSER_PARAM = "deviceExtBrowser=";
	private static final String BANNER_EXT_SIZE_PARAM = "bannerExtSize=";
	private static final String DEVICE_LANGUAGE_PARAM = "deviceLanguage=";
	private static final String DEVICE_EXT_TYPE_PARAM = "deviceExtType=";
	
	@NotBlank(message = "Device´s Browser is mandatory")
	private String deviceExtBrowser;
	
	@NotBlank(message = "Banner´s size is mandatory")
	private String bannerExtSize;
	
	@NotBlank(message = "Device´s language is mandatory")
	private String deviceLanguage;
	
	@NotBlank(message = "Device´s type is mandatory")
	private String deviceExtType;
	
	@JsonIgnore
	private List<Object> redisHashKey;

	public RequestModelDto(String deviceExtBrowser, String bannerExtSize, String deviceLanguage,
			String deviceExtType) {
		this.deviceExtBrowser = deviceExtBrowser;
		this.bannerExtSize = bannerExtSize;
		this.deviceLanguage = deviceLanguage;
		this.deviceExtType = deviceExtType;
		
		redisHashKey = new ArrayList<>();
		redisHashKey.add(DEVICE_EXT_BROWSER_PARAM + deviceExtBrowser);
		redisHashKey.add(BANNER_EXT_SIZE_PARAM + bannerExtSize);
		redisHashKey.add(DEVICE_LANGUAGE_PARAM + deviceLanguage);
		redisHashKey.add(DEVICE_EXT_TYPE_PARAM + deviceExtType);
	}

}
