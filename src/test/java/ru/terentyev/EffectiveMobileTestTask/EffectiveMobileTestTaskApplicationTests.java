package ru.terentyev.EffectiveMobileTestTask;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.hasItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.terentyev.EffectiveMobileTestTask.entities.AuthRequest;
import ru.terentyev.EffectiveMobileTestTask.entities.CommentRequest;
import ru.terentyev.EffectiveMobileTestTask.entities.Person;
import ru.terentyev.EffectiveMobileTestTask.entities.Task;
import ru.terentyev.EffectiveMobileTestTask.entities.TaskRequest;
import ru.terentyev.EffectiveMobileTestTask.security.PersonDetails;
import ru.terentyev.EffectiveMobileTestTask.services.CommentService;
import ru.terentyev.EffectiveMobileTestTask.services.PersonDetailsService;
import ru.terentyev.EffectiveMobileTestTask.services.TaskService;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback
@SpringBootTest
class EffectiveMobileTestTaskApplicationTests {

 		@Autowired
 		private PersonDetailsService personDetailsService;

 		@Autowired
 		private TaskService taskService;
 		
 		@Autowired
 		private CommentService commentService;
    
 		@Autowired
 		private MockMvc mockMvc;
 		
 		@Autowired
 		private ObjectMapper objectMapper;
    
 		private List<Task> controlTasks;
    
 		private List<Person> people;
 		
 		private String token1;
    
 		private String token2;
 		
 		
 		@BeforeEach
 		public void fillTasksList() {
 			controlTasks = new ArrayList<>();
 			people = new ArrayList<>();
 			setupPeople();
 			
 			PersonDetails personDetails1 = new PersonDetails(people.get(0));
 			PersonDetails personDetails2 = new PersonDetails(people.get(1));
 			
 			TaskRequest taskRequest1 = new TaskRequest();
 			
 			taskRequest1.setTitle("IntegrationTestTask-1");
 			taskRequest1.setDescription("Task №1 for integration tests description");
 			taskRequest1.setPriority("LOW");
 			taskRequest1.setStatus("AWAITING");
 			taskRequest1.setAuthorId(people.get(0).getId());
 			taskRequest1.setExecutorId(people.get(0).getId());
 			controlTasks.add(taskService.saveTask(taskRequest1, new Task(), personDetails1));
	
 			TaskRequest taskRequest2 = new TaskRequest();
 			taskRequest2.setTitle("Random Title");
 			taskRequest2.setDescription("Random Description");
 			taskRequest2.setAuthorId(people.get(0).getId());
 			taskRequest2.setExecutorId(people.get(0).getId());
 			taskRequest2.setPriority("MEDIUM");
 			taskRequest2.setStatus("PROCESSING");
 			controlTasks.add(taskService.saveTask(taskRequest2, new Task(), personDetails1));
	
 			TaskRequest taskRequest3 = new TaskRequest();
 			taskRequest3.setTitle("Название для тестовой задачи");
 			taskRequest3.setDescription("Описание для тестовой задачи");
 			taskRequest3.setAuthorId(people.get(1).getId());
 			taskRequest2.setExecutorId(people.get(1).getId());
 			taskRequest3.setPriority("HIGH");
 			taskRequest3.setStatus("COMPLETED");
 			controlTasks.add(taskService.saveTask(taskRequest3, new Task(), personDetails2));
 			
 			//postTestComments();
 		}
 		
 		public void postTestComments() {
 			int taskId = controlTasks.get(0).getId();
 			PersonDetails personDetails = new PersonDetails(people.get(taskId));
 			for (int i = 1; i <= 20; i++) {
 			CommentRequest commentRequest = new CommentRequest();
 			commentRequest.setBody("TestComment" + i);
 			commentRequest.setTaskId(taskId);
 			commentService.postComment(commentRequest, personDetails);
 			}
 		}

 		public void setupPeople() {
 			AuthRequest registerRequest1 = new AuthRequest();
 			String email1 = "userForTests1@mail.ru";
 			registerRequest1.setEmail(email1);
 			String password1 = "testPassword1";
 			registerRequest1.setPassword(password1);
 			registerRequest1.setPasswordConfirm(password1);
 			Person person1 = personDetailsService.registerNewPerson(registerRequest1);
 			people.add(person1);
 			AuthRequest loginRequest1 = new AuthRequest(email1, password1, password1);
 			token1 = personDetailsService.authenticateUser(loginRequest1).getToken();
 			
 			AuthRequest registerRequest2 = new AuthRequest();
 			String email2 = "userForTests2@mail.ru";
 			registerRequest2.setEmail(email2);
 			String password2 = "testPassword2";
 			registerRequest2.setPassword(password2);
 			registerRequest2.setPasswordConfirm(password2);
 			Person person2 = personDetailsService.registerNewPerson(registerRequest2);
 			people.add(person2);
 			AuthRequest loginRequest2 = new AuthRequest(email2, password2, password2);
 			token2 = personDetailsService.authenticateUser(loginRequest2).getToken();
 		}
 		
 		
 		@Test
 		public void correctGetRequestOfSingleTaskMustSuccess() throws JsonProcessingException, Exception {
 			  int id = controlTasks.get(0).getId();
 			  		 
 			  mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + id).header("Authorization", "Bearer " + token1)
 					    .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
 			  			//.andDo(print())
 						.andExpect(status().isOk())
 						.andExpect(jsonPath("$.tasks[0].id", Matchers.equalTo(id)))
 						.andExpect(jsonPath("$.tasks[0].author", Matchers.equalTo(people.get(0).getId())));
 		}
 		
 		@Test
 		public void correctGetRequestOfTasksWithSortAndPagingMustSuccess() throws JsonProcessingException, Exception {
 			 
 			  mockMvc.perform(MockMvcRequestBuilders.get("/tasks?page=1&sortBy=priority").header("Authorization", "Bearer " + token1)
 					    .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
 			  			//.andDo(print())
 						.andExpect(status().isOk())
 						.andExpect(jsonPath("$.tasks[0].priority", Matchers.equalTo("LOW")))
 						.andExpect(jsonPath("$.tasks[2].priority", Matchers.equalTo("HIGH")));
 		}
 		
 		@Test
 		public void correctGetRequestOfSingleTaskWithSortAndPagingOfCommentsMustSuccess() throws Exception {
			int taskId = controlTasks.get(0).getId();
 			PersonDetails personDetails = new PersonDetails(people.get(0));
 			for (int i = 0; i < 20; i++) {
 			CommentRequest commentRequest = new CommentRequest();
 			commentRequest.setBody("TestComment" + (20 - i));
 			commentRequest.setTaskId(taskId);
 			commentService.postComment(commentRequest, personDetails);
 			}
 			
 			
			  mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + taskId + "?page=2&sortBy=body").header("Authorization", "Bearer " + token1)
					    .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
			  			//.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.tasks[0].comments.size()").value(10));
						//.andExpect(jsonPath("$.tasks[0].comments[0].body", Matchers.equalTo("TestComment10")));
 		}
 		
 		@Test
 		public void correctPostTaskRequestMustSuccess() throws JsonProcessingException, Exception {
 			Map<String, String> requestMap = new HashMap<>();
 			String title = "Задача из теста";
 			requestMap.put("title", title);
 			String description = "Описание задачи из теста";
 			requestMap.put("description", description);
 			Integer executorId = people.get(0).getId();
 			requestMap.put("executorId", String.valueOf(executorId));
 			String status = "AWAITING";
 			String priority = "LOW";
 			requestMap.put("status", status);
 			requestMap.put("priority", priority);
 			
 			
 			  mockMvc.perform(MockMvcRequestBuilders.post("/tasks").header("Authorization", "Bearer " + token1)
 					    .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE)
 			  			.content(objectMapper.writeValueAsString(requestMap)))
 			  			//.andDo(print())
 						.andExpect(status().isCreated())
 						.andExpect(jsonPath("$.tasks[0].status", Matchers.equalTo(status)))
 						.andExpect(jsonPath("$.tasks[0].priority", Matchers.equalTo(priority)))
 						.andExpect(jsonPath("$.tasks[0].executor", Matchers.equalTo(executorId)))
 						.andExpect(jsonPath("$.tasks[0].title", Matchers.equalTo(title)))
 						.andExpect(jsonPath("$.tasks[0].description", Matchers.equalTo(description)));
 		}
 		
 		@Test
 		public void correctGetTasksRequestByAuthorMustSuccess() throws JsonProcessingException, Exception {
 			  Integer authorId = people.get(0).getId();
 			  Map<String, String> requestMap = Map.of("authorId", String.valueOf(authorId));
		  		 
 			  mockMvc.perform(MockMvcRequestBuilders.post("/tasks/search").header("Authorization", "Bearer " + token1)
 					    .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE)
 					    .content(objectMapper.writeValueAsString(requestMap)))
 			  			//.andDo(print())
 						.andExpect(status().isOk())
 						.andExpect(jsonPath("$.tasks.size()", Matchers.equalTo(2)))
 						.andExpect(jsonPath("$.tasks[0].author", Matchers.equalTo(authorId)))
 						.andExpect(jsonPath("$.tasks[1].author", Matchers.equalTo(authorId)));
 		}
 		
 		@Test
 		public void correctGetTasksRequestByExecutorMustSuccess() throws JsonProcessingException, Exception {
 			  Integer executorId = people.get(0).getId();
 			  Map<String, String> requestMap = Map.of("executorId", String.valueOf(executorId));
		  		 
 			  mockMvc.perform(MockMvcRequestBuilders.post("/tasks/search").header("Authorization", "Bearer " + token1)
 					    .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE)
 					    .content(objectMapper.writeValueAsString(requestMap)))
 			  			//.andDo(print())
 						.andExpect(status().isOk())
 						.andExpect(jsonPath("$.tasks.size()", Matchers.equalTo(2)))
 						.andExpect(jsonPath("$.tasks[0].author", Matchers.equalTo(executorId)))
 						.andExpect(jsonPath("$.tasks[1].author", Matchers.equalTo(executorId)));
 		}
 		
 		@Test
 		public void correctPostCommentRequestMustSuccess() throws JsonProcessingException, Exception {
 			  String body = "Тестовый комментарий";
 			  Map<String, String> requestMap = Map.of("body", body, "taskId", String.valueOf(controlTasks.get(0).getId()));
		  		 
 			  mockMvc.perform(MockMvcRequestBuilders.post("/comments").header("Authorization", "Bearer " + token1)
 					    .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE)
 					    .content(objectMapper.writeValueAsString(requestMap)))
 			  			//.andDo(print())
 						.andExpect(status().isCreated())
 						.andExpect(jsonPath("$.tasks[0].comments.size()", Matchers.equalTo(1)))
 						.andExpect(jsonPath("$.tasks[0].comments[0].body", Matchers.equalTo(body)));
 		}
 		
 		@Test
 		public void correctPostAuthRequestMustSuccess() throws JsonProcessingException, Exception {
 			Map<String, String> requestMap = new HashMap<>();
 			String email = "otherNewUser@mail.ru";
 			requestMap.put("email", email);
 			String password = "otherPassword";
 			requestMap.put("password", password);
 			requestMap.put("passwordConfirm", password);
 			
 			mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
					  .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE)
					  .content(objectMapper.writeValueAsString(requestMap)))
			  		  //.andDo(print())
					  .andExpect(status().isCreated())
					  .andExpect(jsonPath("$", Matchers.equalTo("Пользователь успешно зарегистрирован")));
 			
 			requestMap.remove("passwordConfirm");
 			
 			ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
					  .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE)
					  .content(objectMapper.writeValueAsString(requestMap)))
			  		  //.andDo(print())
					  .andExpect(status().isOk())
					  .andExpect(jsonPath("$.token", Matchers.notNullValue()));
 			
 			String registerResponseString = resultActions.andReturn().getResponse().getContentAsString();
 			String otherToken = objectMapper.readValue(registerResponseString, new TypeReference<Map<String, String>>(){}).get("token");
 			
 			mockMvc.perform(MockMvcRequestBuilders.get("/tasks").header("Authorization", "Bearer " + otherToken)
					  .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
			  		  //.andDo(print())
					  .andExpect(status().isOk());
 			
		}
 		
 		@Test
 		public void incorrectRegisterRequestWithBadCredentialsMustFail() throws JsonProcessingException, Exception {
 			Map<String, String> requestMap = new HashMap<>();
 			String email = "";
 			requestMap.put("email", email);
 			String password = "321";
 			requestMap.put("password", password);
 			requestMap.put("passwordConfirm", "notEqualPassword");
 			
 			MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/auth/register")
					  .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE);
 			
 			mockMvc.perform(request.content(objectMapper.writeValueAsString(requestMap)))
			  		  //.andDo(print())
					  .andExpect(status().isBadRequest())
					  .andExpect(jsonPath("$.ошибки", hasItem("Пароль и его подтверждение не совпадают")));
 			
 			requestMap.put("passwordConfirm", password);	
 			
 			mockMvc.perform(request.content(objectMapper.writeValueAsString(requestMap)))
			  		  //.andDo(print())
					  .andExpect(status().isBadRequest())
					  .andExpect(jsonPath("$.ошибки", hasItem("Пароль должен содержать минимум 5 символов")))
					  .andExpect(jsonPath("$.ошибки", hasItem("Email не должен быть пустым")));
 			
 			requestMap.put("email", "notEmail");
 			
 			mockMvc.perform(request.content(objectMapper.writeValueAsString(requestMap)))
	  		  //.andDo(print())
			  .andExpect(status().isBadRequest())
			  .andExpect(jsonPath("$.ошибки", hasItem("Email должен иметь формат 'name@host.com'")));
 			
 			email = "userForTests1@mail.ru";
 			requestMap.put("email", email);
 			
 			mockMvc.perform(request.content(objectMapper.writeValueAsString(requestMap)))
	  		  .andDo(print())
			  .andExpect(status().isBadRequest())
			  .andExpect(jsonPath("$.ошибки", hasItem("Пользователь с email " + email + " уже существует")));
			
 		}
 		
 		@Test
 		public void incorrectLoginRequestWithBadCredentialsMustFail() throws JsonProcessingException, Exception {
 			String email = "notexists@mail.ru";
 			Map<String, String> requestMap = Map.of("email", email, "password", "randompassword");
 			
 			MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/auth/login")
					  .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE);
 			
 			mockMvc.perform(request.content(objectMapper.writeValueAsString(requestMap)))
			  		  //.andDo(print())
					  .andExpect(status().isBadRequest())
					  .andExpect(jsonPath("$.ошибки", hasItem("Email " + email + " не найден.")));			
 		}			
 		
 		@Test
 		public void correctPostRequestWithoutAuthMustFail() throws Exception {
 			MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/tasks/" + controlTasks.get(0).getId())
					  .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE);
			
			mockMvc.perform(request)
			  		  //.andDo(print())
					  .andExpect(status().isForbidden());		
 		}
 		
 		
 		@Test
 		public void incorrectPostTaskRequestWithMissingKeysMustFail() throws JsonProcessingException, Exception {
 			Map<String, String> requestMap = new HashMap<>();
 			requestMap.put("title", "");
 			
 			MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/tasks").header("Authorization", "Bearer " + token1)
					  .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE);
 			
 			mockMvc.perform(request.content(objectMapper.writeValueAsString(requestMap)))
			  		  //.andDo(print())
					  .andExpect(status().isBadRequest())
					  .andExpect(jsonPath("$.ошибки", hasItem("Заголовок не может быть пустым")))
					  .andExpect(jsonPath("$.ошибки", hasItem("У задачи должен быть статус")))
					  .andExpect(jsonPath("$.ошибки", hasItem("У задачи должен быть приоритет")));
 			
 			String status = "notExistingStatus";
 			requestMap.put("title", "NotEmptyTitle");			
 			requestMap.put("status", status);
 			
 			mockMvc.perform(request.content(objectMapper.writeValueAsString(requestMap)))
	  		  .andDo(print())
			  .andExpect(status().isNotFound())
			  .andExpect(jsonPath("$.ошибки", hasItem("Статус " + status + " не найден")));
 			
 			requestMap.put("status", "AWAITING");
 			String priority = "notExistingPriority";
 			requestMap.put("priority", priority);
 			
 			mockMvc.perform(request.content(objectMapper.writeValueAsString(requestMap)))
	  		  .andDo(print())
			  .andExpect(status().isNotFound())
			  .andExpect(jsonPath("$.ошибки", hasItem("Приоритет " + priority + " не найден")));
 		}	
 		
 		@Test
 		public void correctPatchRequestMustSuccess() throws JsonProcessingException, Exception {
			Map<String, String> requestMap = new HashMap<>();
			requestMap.put("id", String.valueOf(controlTasks.get(0).getId()));
			String patchedTitle = "patchedTitle";
 			requestMap.put("title", patchedTitle);
 			String patchedDescription = "patchedDescription";
 			requestMap.put("description", patchedDescription);
 			String patchedStatus = "PROCESSING";
 			requestMap.put("status", patchedStatus);
 			String patchedPriority = "MEDIUM";
 			requestMap.put("priority", patchedPriority);
 			Integer executorId = people.get(1).getId();
 			requestMap.put("executorId", String.valueOf(executorId));
 			
 			MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/tasks").header("Authorization", "Bearer " + token1)
					  .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE);
 			
 			mockMvc.perform(request.content(objectMapper.writeValueAsString(requestMap)))
			  		  //.andDo(print())
					  .andExpect(status().isOk())
					  .andExpect(jsonPath("$.tasks[0].title", Matchers.equalTo(patchedTitle)))
					  .andExpect(jsonPath("$.tasks[0].description", Matchers.equalTo(patchedDescription)))
					  .andExpect(jsonPath("$.tasks[0].status", Matchers.equalTo(patchedStatus)))
					  .andExpect(jsonPath("$.tasks[0].priority", Matchers.equalTo(patchedPriority)))
					  .andExpect(jsonPath("$.tasks[0].executor", Matchers.equalTo(executorId)));
 		}
 		
 		@Test
 		public void correctDeleteRequestMustSuccess() throws Exception {
 			MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/tasks/" + controlTasks.get(0).getId()).header("Authorization", "Bearer " + token1)
					  .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE);
			
			mockMvc.perform(request)
			  		  //.andDo(print())
					  .andExpect(status().isOk());
 		}
 		
 		@Test
 		public void incorrectDeleteAlienTaskRequestMustFail() throws Exception {
 			MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/tasks/" + controlTasks.get(2).getId()).header("Authorization", "Bearer " + token1)
					  .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE);
			
			mockMvc.perform(request)
			  		  //.andDo(print())
					  .andExpect(status().isBadRequest())
					  .andExpect(jsonPath("$.ошибки", hasItem("Вы не являетесь автором задачи")));
 		}
 		
 		@Test
 		public void incorrectPatchAlienTaskRequestMustFail() throws Exception {
 			Map<String, String> requestMap = Map.of("id", String.valueOf(controlTasks.get(2).getId()), "title", "rejectedTitle");
 			MockHttpServletRequestBuilder request = MockMvcRequestBuilders.patch("/tasks").header("Authorization", "Bearer " + token1)
					  .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE);
			
			mockMvc.perform(request.content(objectMapper.writeValueAsString(requestMap)))
			  		  .andDo(print())
					  .andExpect(status().isBadRequest())
					  .andExpect(jsonPath("$.ошибки", hasItem("Вы не являетесь автором задачи")));
 		}
	}
