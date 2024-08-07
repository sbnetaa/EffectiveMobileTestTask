package ru.terentyev.EffectiveMobileTestTask.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Модель успешного ответа")
public class TaskResponse implements Response {
	
	@Schema(description = "Список задач")
	private List<Task> tasks;
	
	public TaskResponse(){}
}
