package ru.terentyev.EffectiveMobileTestTask.exceptions;

public class PriorityNotFoundException extends CustomException {
	
	public PriorityNotFoundException(String name) {
		super("Приоритет " + name + " не найден");
	}

}
