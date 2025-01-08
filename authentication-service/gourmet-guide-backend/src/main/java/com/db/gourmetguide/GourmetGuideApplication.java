package com.db.gourmetguide;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GourmetGuideApplication {

	public static void main(String[] args) {
		SpringApplication.run(GourmetGuideApplication.class, args);
	}

}
