package ru.terentyev.EffectiveMobileTestTask.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.terentyev.EffectiveMobileTestTask.entities.Response;
import ru.terentyev.EffectiveMobileTestTask.entities.Task;
import ru.terentyev.EffectiveMobileTestTask.entities.TaskRequest;
import ru.terentyev.EffectiveMobileTestTask.entities.TaskResponse;
import ru.terentyev.EffectiveMobileTestTask.security.PersonDetails;
import ru.terentyev.EffectiveMobileTestTask.services.TaskService;
import ru.terentyev.EffectiveMobileTestTask.util.SwaggerExamples;

@RestController
@RequestMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE
, headers = "Accept=application/json")
@Tag(name = "Задачи", description = "Операции, связанные с задачами. В запросах вместо ключей 'author' и 'executor' используется 'authorId' и 'executorId'.")
public class TaskController extends AbstractController {

	private TaskService taskService;
	
	@Autowired
	public TaskController(TaskService taskService) {
		super();
		this.taskService = taskService;
	}
	
	@GetMapping(value = {"", "/{id}"})
	@Operation(summary = "Просмотр задач(и)" , description = "Отображает несколько задач без их комментариев или, передав в качестве переменной пути '/{id}' ID задачи, одну с комментариями к ней."
			+ " В запрос можно передать параметры 'page' (номер страницы) и 'sortBy' (название поля для сортировки). Например http://localhost:8080/tasks/1?page=1&sortBy=id")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Запрос выполнен", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = SwaggerExamples.GET_RESPONSE_TASKS))),
	        @ApiResponse(responseCode = "404", description = "Не найдена либо задача, либо поле для сортировки", content = @Content(mediaType = "application/json",
	        schema = @Schema(example = SwaggerExamples.GET_404_RESPONSE_TASKS)))
	    })	
	public ResponseEntity<Response> showTasks(@Parameter(description = "ID задачи (опционально). Если не указан, возвращаются все задачи.", example = "3") @PathVariable(required = false) Integer id
			, @Parameter(description = "Номер страницы (опционально, начиная с 1). Если ID указан, то применяется для пагинации комментариев, а если нет, то задач.", example = "1") @RequestParam(required = false) Integer page
			, @Parameter(description = "Название поля для сортировки (опционально, например 'author'). Если ID указан, то применяется для сортировки комментариев, а если нет, то задач.", example = "author") @RequestParam(required = false) String sortBy) {
			return new ResponseEntity<>(new TaskResponse(taskService.showTasks(id, page, sortBy)), HttpStatus.OK);
	}
	
	@Operation(summary = "Поиск" , description = "Использует Hibernate Criteria API для гибкого поиска по задачам (фильтрации).", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(examples = 
		{ @ExampleObject(SwaggerExamples.POST_REQUEST_SEARCH_TASKS)})))
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Поиск выполнен, выведены результаты", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = SwaggerExamples.POST_RESPONSE_SEARCH_TASKS))),
	        @ApiResponse(responseCode = "404", description = "Статус, приоритет или пользователь не найден", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = SwaggerExamples.POST_404_RESPONSE_SEARCH_TASKS)))
	    })	
	@PostMapping("/search")
	public ResponseEntity<Response> searchTasks(@Parameter(description = "Поля задачи для поиска") @RequestBody TaskRequest taskRequest) {
			return new ResponseEntity<>(new TaskResponse(taskService.searchByCriteria(taskRequest)), HttpStatus.OK);
	}
	
	@Operation(summary = "Создание задачи" , description = "Создает новую задачу", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(examples = 
		{ @ExampleObject(SwaggerExamples.POST_REQUEST_ADD_TASK)})))
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "201", description = "Задача создана и сохранена", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = SwaggerExamples.POST_RESPONSE_ADD_TASK))),
	        @ApiResponse(responseCode = "400", description = "Некорректно составлен запрос, либо ошибки валидации", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = SwaggerExamples.POST_400_RESPONSE_ADD_TASK)))})
	@PostMapping
	public ResponseEntity<Response> createTask(@Parameter(description = "Тело задачи для создания") 
		@RequestBody TaskRequest taskRequest, @AuthenticationPrincipal PersonDetails pd) {	
			return new ResponseEntity<>(new TaskResponse(List.of(taskService.saveTask(taskRequest, new Task(), pd))), HttpStatus.CREATED);
	}
	
	@Operation(summary = "Редактирование задачи" , description = "Редактирует существующую задачу по ID", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(examples = 
		{ @ExampleObject(SwaggerExamples.PATCH_REQUEST_TASK)})))
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Задача отредактирована и сохранена", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = SwaggerExamples.PATCH_RESPONSE_TASK))),
	        @ApiResponse(responseCode = "400", description = "Некорректно составлен запрос (например редактируется чужая задача), либо ошибки валидации", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = SwaggerExamples.PATCH_400_RESPONSE_TASK))),
	        @ApiResponse(responseCode = "404", description = "Задача, статус, приоритет или пользователь не найдены", content = @Content(mediaType = "application/json", 
                    schema = @Schema(example = SwaggerExamples.PATCH_404_RESPONSE_TASK)))
	    })	
	@PatchMapping
	public ResponseEntity<Response> updateTask(
			@Parameter(description = "Тело задачи для редактирования. Редактируется задача, найденная по ключу 'id'. "
					+ "Исполнитель может редактировать только статус. Остальные поля может редактировать только автор.")
			@RequestBody TaskRequest taskRequest, @AuthenticationPrincipal PersonDetails pd) {
			return new ResponseEntity<>(new TaskResponse(List.of(taskService.saveTask(taskRequest, taskService.findById(taskRequest.getId()), pd))), HttpStatus.OK);
	}
	
	@Operation(summary = "Удаление задачи" , description = "Удаляет существующую задачу")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Запрос выполнен"),
	        @ApiResponse(responseCode = "404", description = "Задача не найдена")
	    })	
	@DeleteMapping("/{id}")
	public ResponseEntity<Response> deleteTask(@Parameter(description = "ID задачи для удаления", example = "3") @PathVariable Integer id, @AuthenticationPrincipal PersonDetails pd) {
			taskService.deleteById(id, pd);
			return new ResponseEntity<>(new TaskResponse(), HttpStatus.OK);
	}
}
