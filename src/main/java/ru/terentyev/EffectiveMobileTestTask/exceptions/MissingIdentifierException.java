package ru.terentyev.EffectiveMobileTestTask.exceptions;

public class MissingIdentifierException extends CustomException {

	public MissingIdentifierException() {
		super("Не указан идентификатор");
	}

	
}
