package ru.terentyev.EffectiveMobileTestTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import ru.terentyev.EffectiveMobileTestTask.exceptions.CommonExceptionHandler;

@SpringBootApplication
public class EffectiveMobileTestTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(EffectiveMobileTestTaskApplication.class, args);
	}

}
