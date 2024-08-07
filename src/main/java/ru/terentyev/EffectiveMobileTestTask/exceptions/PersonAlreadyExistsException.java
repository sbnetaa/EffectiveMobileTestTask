package ru.terentyev.EffectiveMobileTestTask.exceptions;

public class PersonAlreadyExistsException extends CustomException {

	public PersonAlreadyExistsException(String email) {
		super("Пользователь с email " + email + " уже существует");
	}

	
}
