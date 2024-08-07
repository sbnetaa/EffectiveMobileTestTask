package ru.terentyev.EffectiveMobileTestTask.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidCredentialsException extends CustomException {

	public InvalidCredentialsException() {
		super("Неверные данные для аутентификации");
	}
}
