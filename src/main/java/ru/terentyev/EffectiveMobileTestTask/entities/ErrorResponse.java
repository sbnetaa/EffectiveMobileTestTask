package ru.terentyev.EffectiveMobileTestTask.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Модель ответа об ошибках")
public class ErrorResponse implements Response {
	
	@Schema(description = "Список ошибок")
	@JsonProperty(value = "ошибки", index = 1)
	private List<String> errors = new ArrayList<>();
	@Schema(description = "Статус ответа", example = "BAD REQUEST")
	@JsonProperty(value = "статус", index = 2)
	private String status;
	@Schema(description = "Код статуса ответа", example = "400")
	@JsonProperty(value = "код", index = 3)
	private Integer statusCode;
	@Schema(description = "Время ошибки")
	@JsonProperty(value = "время", index = 4)
	@JsonFormat(pattern="dd-MM-yyyy HH:mm")
	private LocalDateTime timestamp = LocalDateTime.now();
	
	public ErrorResponse(){}

	public ErrorResponse(List<String> errors, String status, Integer statusCode) {
		super();
		this.errors = errors;
		this.status = status;
		this.statusCode = statusCode;
	}
	
}
