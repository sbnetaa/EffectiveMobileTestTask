package ru.terentyev.EffectiveMobileTestTask.exceptions;

public class StatusNotFoundException extends CustomException {
	
	public StatusNotFoundException(String name) {
		super("Статус " + name + " не найден");
	}

}
