package ru.terentyev.EffectiveMobileTestTask.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Модель ответа аутентификации")
public class AuthResponse implements Response {
	@Schema(description = "Json Web Token для вставки в заголовок 'Authorization' клиента", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmaXJzdHBlcnNvbjJAbWFpbC5ydSIsImlhdCI6MTcyMjkzNTA3MywiZXhwIjoxNzIzMDIxNDczfQ.crhVpNDQE_wNr4Sv7HDulrwSL_9B-mHqMviGb1S41I8")
	    private String token;
	    
}
