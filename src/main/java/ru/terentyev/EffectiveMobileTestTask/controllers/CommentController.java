package ru.terentyev.EffectiveMobileTestTask.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import ru.terentyev.EffectiveMobileTestTask.entities.CommentRequest;
import ru.terentyev.EffectiveMobileTestTask.entities.Response;
import ru.terentyev.EffectiveMobileTestTask.entities.TaskResponse;
import ru.terentyev.EffectiveMobileTestTask.security.PersonDetails;
import ru.terentyev.EffectiveMobileTestTask.services.CommentService;
import ru.terentyev.EffectiveMobileTestTask.util.SwaggerExamples;

@RestController
@RequestMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE
, headers = "Accept=application/json")
@Tag(name = "Комментарии", description = "Операции, связанные с комментариями.")
public class CommentController extends AbstractController {

	private CommentService commentService;

	@Autowired
	public CommentController(CommentService commentService) {
		super();
		this.commentService = commentService;
	}
	
	
	@Operation(summary = "Добавление комментария" , description = "Добавляет к задаче, указанной в ключе 'taskId', новый комментарий.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(examples = 
		{ @ExampleObject(SwaggerExamples.POST_REQUEST_ADD_COMMENT)})))
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "201", description = "Комментарий добавлен", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = SwaggerExamples.POST_RESPONSE_ADD_COMMENT))),
	        @ApiResponse(responseCode = "400", description = "Некорректно составлен запрос, либо пустое тело комментария", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = SwaggerExamples.POST_400_RESPONSE_ADD_COMMENT))),
	        @ApiResponse(responseCode = "404", description = "Задача не найдена", content = @Content(mediaType = "application/json", 
            schema = @Schema(example = SwaggerExamples.POST_404_RESPONSE_ADD_COMMENT)))
	    })	
	@PostMapping
	public ResponseEntity<Response> postComment(@Parameter(description = "Тело комментария и ID задачи") @RequestBody CommentRequest commentRequest, @AuthenticationPrincipal PersonDetails pd) {
		return new ResponseEntity<>(new TaskResponse(List.of(commentService.postComment(commentRequest, pd))), HttpStatus.CREATED);
	}
}
