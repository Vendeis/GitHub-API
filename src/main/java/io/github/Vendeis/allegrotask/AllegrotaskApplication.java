package io.github.Vendeis.allegrotask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestOperations;

@SpringBootApplication
public class AllegrotaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(AllegrotaskApplication.class, args);
	}

}
