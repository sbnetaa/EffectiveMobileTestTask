package ru.terentyev.EffectiveMobileTestTask.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlienTaskException extends CustomException {

	public AlienTaskException() {
		super("Вы не являетесь автором задачи");
	}
}
