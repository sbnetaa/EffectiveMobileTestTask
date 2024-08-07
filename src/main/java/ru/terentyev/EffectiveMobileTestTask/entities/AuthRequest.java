package ru.terentyev.EffectiveMobileTestTask.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Модель запроса на регистрацию/аутентификацию")
public class AuthRequest implements Request {
	@Schema(description = "Email пользователя", example = "testuser@mail.ru")
    private String email;
	@Schema(description = "Пароль пользователя (не меньше 5 символов)", example = "qwerty")
    private String password;
	@Schema(description = "Подтверждение пароля пользователя (должен совпадать с паролем)", example = "qwerty")
    private String passwordConfirm;
    
    public AuthRequest(){}
}