package ru.terentyev.EffectiveMobileTestTask.exceptions;

public class MismatchPasswordsException extends CustomException {

	public MismatchPasswordsException() {
		super("Пароль и его подтверждение не совпадают");
	}
}
