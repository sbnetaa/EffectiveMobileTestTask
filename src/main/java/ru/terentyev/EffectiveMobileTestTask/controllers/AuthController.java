package ru.terentyev.EffectiveMobileTestTask.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.terentyev.EffectiveMobileTestTask.entities.AuthRequest;
import ru.terentyev.EffectiveMobileTestTask.entities.AuthResponse;
import ru.terentyev.EffectiveMobileTestTask.entities.ErrorResponse;
import ru.terentyev.EffectiveMobileTestTask.entities.TaskResponse;
import ru.terentyev.EffectiveMobileTestTask.exceptions.CustomException;
import ru.terentyev.EffectiveMobileTestTask.services.PersonDetailsService;
import ru.terentyev.EffectiveMobileTestTask.util.SwaggerExamples;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE
, headers = "Accept=application/json")
@Tag(name = "Аутентификация", description = "Выполняет регистрацию и аутентификацию")
public class AuthController extends AbstractController {

	private PersonDetailsService personDetailsService;

	
	@Autowired
	public AuthController(PersonDetailsService personDetailsService) {
		super();
		this.personDetailsService = personDetailsService;
	}
	
	@Operation(summary = "Регистрация" , description = "Осуществляет регистрацию пользователя", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(examples = 
		{ @ExampleObject(SwaggerExamples.POST_AUTH_REQUEST_REGISTER)})))
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Запрос выполнен", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = "Пользователь успешно зарегистрирован."))),
	        @ApiResponse(responseCode = "400", description = "Некорректно составлен запрос, либо ошибки валидации", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = SwaggerExamples.POST_404_AUTH_RESPONSE_REGISTER)))
	    })	
    @PostMapping("/register")
    public ResponseEntity<String> register(@Parameter(description = "Данные для регистрации", example = SwaggerExamples.POST_AUTH_REQUEST_REGISTER) @RequestBody AuthRequest authRequest) {
    		personDetailsService.registerNewPerson(authRequest);
    		return new ResponseEntity<>("Пользователь успешно зарегистрирован", HttpStatus.CREATED);
    }
	@Operation(summary = "Аутентификация" , description = "Осуществляет аутентификацию пользователя", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(examples = 
		{ @ExampleObject(SwaggerExamples.POST_AUTH_REQUEST_LOGIN)})))
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Запрос выполнен, получен токен", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = SwaggerExamples.POST_AUTH_RESPONSE_LOGIN))),
	        @ApiResponse(responseCode = "400", description = "Некорректно составлен запрос, либо ошибки валидации", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = SwaggerExamples.POST_AUTH_404_RESPONSE_LOGIN))),
	        @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = SwaggerExamples.POST_AUTH_404_RESPONSE_LOGIN)))
	    })	
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Parameter(description = "Данные для аутентификации", example = SwaggerExamples.POST_AUTH_REQUEST_LOGIN) @RequestBody AuthRequest authRequest) {    
        	AuthResponse authResponse = personDetailsService.authenticateUser(authRequest);
        	return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
