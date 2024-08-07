package ru.terentyev.EffectiveMobileTestTask.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortFieldNotFoundException extends CustomException {

		
		public SortFieldNotFoundException(String field) {
			super("Поле" + field + " для сортировки не найдено.");
		}
}
