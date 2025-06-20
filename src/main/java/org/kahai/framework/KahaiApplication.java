package org.kahai.framework;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

public class KahaiApplication {
	private static final Logger log = LoggerFactory.getLogger(KahaiApplication.class);
	
	public static void run(Class<?> primarySource, String[] args) {
		SpringApplication.run(primarySource, args);
		log.info("Applicação iniciada!");
	};
};