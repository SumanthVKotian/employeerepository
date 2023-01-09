package com.instagramclone;

import com.cloudinary.Cloudinary;
import com.cloudinary.SingletonManager;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;


@SpringBootApplication
public class InstagramCloneApplication {

	@Value("${cloud_name}")
	private String CLOUD_NAME;

	@Value("${api_key}")
	private String API_KEY;

	@Value("${api_secret}")
	private String API_SECRET;

	@PostConstruct
	public void initCloudinary() {
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
				"cloud_name", CLOUD_NAME,
				"api_key", API_KEY,
				"api_secret", API_SECRET,
				"secure", true));

		SingletonManager manager = new SingletonManager();
		manager.setCloudinary(cloudinary);
		manager.init();
	}

	public static void main(String[] args) {
		SpringApplication.run(InstagramCloneApplication.class, args);
	}


}

