package ru.terentyev.EffectiveMobileTestTask.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import ru.terentyev.EffectiveMobileTestTask.controllers.AuthController;
import ru.terentyev.EffectiveMobileTestTask.controllers.CommentController;
import ru.terentyev.EffectiveMobileTestTask.controllers.TaskController;
import ru.terentyev.EffectiveMobileTestTask.entities.ErrorResponse;
import ru.terentyev.EffectiveMobileTestTask.services.CommentServiceImpl;
import ru.terentyev.EffectiveMobileTestTask.services.PersonDetailsService;
import ru.terentyev.EffectiveMobileTestTask.services.TaskServiceImpl;


@RestControllerAdvice(assignableTypes = {TaskController.class, TaskServiceImpl.class
		 , AuthController.class, PersonDetailsService.class
		 , CommentController.class, CommentServiceImpl.class})
public class CommonExceptionHandler {

	public HttpHeaders putHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) throws JsonProcessingException {
		e.printStackTrace();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		int statusCode = 400;
		if (e instanceof PriorityNotFoundException || e instanceof SortFieldNotFoundException
				|| e instanceof StatusNotFoundException || e instanceof TaskNotFoundException) {
			status = HttpStatus.NOT_FOUND;
			statusCode = 404;
		}
		return new ResponseEntity<>(new ErrorResponse(List.of(e.getMessage()), status.name(), statusCode), putHeaders(), status);
	}
	
	@ExceptionHandler(JsonProcessingException.class)
	public ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException e) throws JsonProcessingException {
		e.printStackTrace();
		return new ResponseEntity<>(new ErrorResponse(List.of("Некорректно составлен JSON"), "BAD REQUEST", 400), putHeaders(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException cve) throws JsonProcessingException {
		cve.printStackTrace();
		List<String> errors = new ArrayList<>();
		for (ConstraintViolation<?> cv : cve.getConstraintViolations()) errors.add(cv.getMessage());
		return new ResponseEntity<>(new ErrorResponse(errors, "BAD REQUEST", 400), putHeaders(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleCommonException(Exception e) throws JsonProcessingException {
		e.printStackTrace();
		return new ResponseEntity<>(new ErrorResponse(List.of(e.getMessage()), "BAD REQUEST", 400), putHeaders(), HttpStatus.BAD_REQUEST);
	}
}
