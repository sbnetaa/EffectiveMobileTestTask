package ru.terentyev.EffectiveMobileTestTask.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskNotFoundException extends CustomException {
	
	private Integer id;
	
	public TaskNotFoundException(Integer id) {
		super("Задача с ID " + id + " не найдена.");
	}
}
