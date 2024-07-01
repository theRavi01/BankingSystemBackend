package com.banking.system.ImgCloud;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class ProjectConfig {

	@Bean
	public Cloudinary getCloudinary() {
		Map config = new HashMap();
		
		config.put("cloud_name","dmwxfbi");
		config.put("api_key","9992817265942");
		config.put("api_secret","uRIcbrWWGoC5cQGmdbGh9QKlY");
		config.put("secure",true);
		return new Cloudinary(config);
	}
}
