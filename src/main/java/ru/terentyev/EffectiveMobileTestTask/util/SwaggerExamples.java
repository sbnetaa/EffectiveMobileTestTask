package ru.terentyev.EffectiveMobileTestTask.util;

public class SwaggerExamples {
	public static final String REQUEST_GET_TASKS = "";
	public static final String GET_RESPONSE_TASKS = "{\r\n"
    		+ "    \"tasks\": [\r\n"
    		+ "        {\r\n"
    		+ "            \"id\": 3,\r\n"
    		+ "            \"title\": \"TaskTitle\",\r\n"
    		+ "            \"description\": \"TaskDescription\",\r\n"
    		+ "            \"status\": \"PROCESSING\",\r\n"
    		+ "            \"priority\": \"MEDIUM\",\r\n"
    		+ "            \"author\": 2,\r\n"
    		+ "            \"executor\": 2,\r\n"
    		+ "            \"comments\": []\r\n"
    		+ "        }\r\n"
    		+ "    ]\r\n"
    		+ "}";
	
	public static final String GET_404_RESPONSE_TASKS = "{\r\n"
    		+ "    \"ошибки\": [\r\n"
    		+ "        \"Задача с ID 2 не найдена.\"\r\n"
    		+ "    ],\r\n"
    		+ "    \"статус\": \"NOT_FOUND\",\r\n"
    		+ "    \"код\": 404,\r\n"
    		+ "    \"время\": \"06-08-2024 15:45\"\r\n"
    		+ "}";

	public static final String POST_RESPONSE_SEARCH_TASKS = "{\r\n"
    		+ "    \"tasks\": [\r\n"
    		+ "        {\r\n"
    		+ "            \"id\": 3,\r\n"
    		+ "            \"title\": \"TaskTitle\",\r\n"
    		+ "            \"description\": \"TaskDescription\",\r\n" // TODO
    		+ "            \"status\": \"PROCESSING\",\r\n"
    		+ "            \"priority\": \"MEDIUM\",\r\n"
    		+ "            \"author\": 2,\r\n"
    		+ "            \"executor\": 2,\r\n"
    		+ "            \"comments\": []\r\n"
    		+ "        }\r\n"
    		+ "    ]\r\n"
    		+ "}";
	
	public static final String POST_404_RESPONSE_SEARCH_TASKS = "{\r\n"
    		+ "    \"ошибки\": [\r\n"
    		+ "        \"Статус unnamed не найден\"\r\n"
    		+ "    ],\r\n"
    		+ "    \"статус\": \"NOT_FOUND\",\r\n"
    		+ "    \"код\": 404,\r\n"
    		+ "    \"время\": \"06-08-2024 15:48\"\r\n"
    		+ "}";
	
	public static final String POST_REQUEST_SEARCH_TASKS = "{\r\n"
    		+ "    \"authorId\" : 2\r\n"
    		+ "}";
	
	public static final String POST_RESPONSE_ADD_TASK = "{\r\n"
    		+ "    \"tasks\": [\r\n"
    		+ "        {\r\n"
    		+ "            \"id\": 4,\r\n"
    		+ "            \"title\": \"new Task\",\r\n"
    		+ "            \"description\": \"description of new Task\",\r\n"
    		+ "            \"status\": \"PROCESSING\",\r\n"
    		+ "            \"priority\": \"MEDIUM\",\r\n"
    		+ "            \"author\": 3,\r\n"
    		+ "            \"executor\": 3,\r\n"
    		+ "            \"comments\": null\r\n"
    		+ "        }\r\n"
    		+ "    ]\r\n"
    		+ "}";
	
	public static final String POST_400_RESPONSE_ADD_TASK = "{\r\n"
			+ "    \"ошибки\": [\r\n"
			+ "        \"Заголовок не может быть пустым\"\r\n"
			+ "    ],\r\n"
			+ "    \"статус\": \"BAD REQUEST\",\r\n"
			+ "    \"код\": 400,\r\n"
			+ "    \"время\": \"06-08-2024 16:52\"\r\n"
			+ "}";
	
	public static final String POST_REQUEST_ADD_TASK = "{\r\n"
    		+ "    \"title\" : \"new Task\",\r\n"
    		+ "    \"description\" : \"description of new Task\",\r\n"
    		+ "    \"status\" : \"PROCESSING\",\r\n"
    		+ "    \"priority\" : \"MEDIUM\",\r\n"
    		+ "    \"executorId\" : 3\r\n"
    		+ "}";
	
	public static final String PATCH_REQUEST_TASK = "{\r\n"
			+ "    \"id\" : 5,\r\n"
			+ "    \"title\" : \"patchedTitle\"\r\n"
			+ "} ";
	
	public static final String PATCH_RESPONSE_TASK = "{\r\n"
			+ "    \"tasks\": [\r\n"
			+ "        {\r\n"
			+ "            \"id\": 5,\r\n"
			+ "            \"title\": \"patchedTitle\",\r\n"
			+ "            \"description\": \"descriptionOfUser2\",\r\n"
			+ "            \"status\": \"PROCESSING\",\r\n"
			+ "            \"priority\": \"MEDIUM\",\r\n"
			+ "            \"author\": 3,\r\n"
			+ "            \"executor\": 3,\r\n"
			+ "            \"comments\": []\r\n"
			+ "        }\r\n"
			+ "    ]\r\n"
			+ "}";
	
	public static final String PATCH_400_RESPONSE_TASK = "{\r\n"
			+ "    \"ошибки\": [\r\n"
			+ "        \"Вы не являетесь автором задачи\"\r\n"
			+ "    ],\r\n"
			+ "    \"статус\": \"BAD_REQUEST\",\r\n"
			+ "    \"код\": 400,\r\n"
			+ "    \"время\": \"06-08-2024 17:02\"\r\n"
			+ "}";
	
	public static final String PATCH_404_RESPONSE_TASK = "{\r\n"
			+ "    \"ошибки\": [\r\n"
			+ "        \"Статус dsa не найден\"\r\n"
			+ "    ],\r\n"
			+ "    \"статус\": \"NOT_FOUND\",\r\n"
			+ "    \"код\": 404,\r\n"
			+ "    \"время\": \"06-08-2024 17:07\"\r\n"
			+ "}";
	
	public static final String POST_AUTH_REQUEST_REGISTER = "{\r\n"
			+ "  \"email\" : \"firstperson2@mail.ru\",\r\n"
			+ "  \"password\" : \"qwerty2\",\r\n"
			+ "  \"passwordConfirm\" : \"qwerty2\"\r\n"
			+ "}";
	
	public static final String POST_404_AUTH_RESPONSE_REGISTER = "{\r\n"
			+ "    \"ошибки\": [\r\n"
			+ "        \"Пароль и его подтверждение не совпадают\"\r\n"
			+ "    ],\r\n"
			+ "    \"статус\": \"BAD_REQUEST\",\r\n"
			+ "    \"код\": 400,\r\n"
			+ "    \"время\": \"06-08-2024 17:38\"\r\n"
			+ "}";
	
	public static final String POST_AUTH_REQUEST_LOGIN = "{\r\n"
			+ "  \"email\" : \"firstperson2@mail.ru\",\r\n"
			+ "  \"password\" : \"qwerty2\"\r\n"
			+ "}";
	
	public static final String POST_AUTH_RESPONSE_LOGIN = "{\r\n"
			+ "    \"token\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmaXJzdHBlcnNvbjJAbWFpbC5ydSIsImlhdCI6MTcyMjk1NTUxMywiZXhwIjoxNzIzMDQxOTEzfQ.O3zS9dr8O1aiJd5PIlEi-aarD0zknBZ9JfnNFk5ejzI\"\r\n"
			+ "}";
	
	public static final String POST_AUTH_404_RESPONSE_LOGIN = "{\r\n"
			+ "    \"ошибки\": [\r\n"
			+ "        \"Email firstperson22@mail.ru не найден.\"\r\n"
			+ "    ],\r\n"
			+ "    \"статус\": \"BAD REQUEST\",\r\n"
			+ "    \"код\": 400,\r\n"
			+ "    \"время\": \"06-08-2024 17:47\"\r\n"
			+ "}";
	
	public static final String POST_REQUEST_ADD_COMMENT = "{\r\n"
			+ "    \"taskId\" : 4,\r\n"
			+ "    \"body\" : \"new comment\"\r\n"
			+ "}";
	
	public static final String POST_RESPONSE_ADD_COMMENT = "{\r\n"
			+ "    \"tasks\": [\r\n"
			+ "        {\r\n"
			+ "            \"id\": 4,\r\n"
			+ "            \"title\": \"taskOfUser2\",\r\n"
			+ "            \"description\": \"descriptionOfUser2\",\r\n"
			+ "            \"status\": \"PROCESSING\",\r\n"
			+ "            \"priority\": \"MEDIUM\",\r\n"
			+ "            \"author\": 3,\r\n"
			+ "            \"executor\": 3,\r\n"
			+ "            \"comments\": [\r\n"
			+ "                {\r\n"
			+ "                    \"id\": 1,\r\n"
			+ "                    \"body\": \"new comment\",\r\n"
			+ "                    \"task\": 4,\r\n"
			+ "                    \"author\": 3\r\n"
			+ "                }\r\n"
			+ "            ]\r\n"
			+ "        }\r\n"
			+ "    ]\r\n"
			+ "}";
	
	public static final String POST_400_RESPONSE_ADD_COMMENT = "{\r\n"
			+ "    \"ошибки\": [\r\n"
			+ "        \"Комментарий не может быть пустым\"\r\n"
			+ "    ],\r\n"
			+ "    \"статус\": \"BAD REQUEST\",\r\n"
			+ "    \"код\": 400,\r\n"
			+ "    \"время\": \"06-08-2024 19:51\"\r\n"
			+ "}";
	
	public static final String POST_404_RESPONSE_ADD_COMMENT = "{\r\n"
			+ "    \"ошибки\": [\r\n"
			+ "        \"Задача с ID 5 не найдена.\"\r\n"
			+ "    ],\r\n"
			+ "    \"статус\": \"NOT_FOUND\",\r\n"
			+ "    \"код\": 404,\r\n"
			+ "    \"время\": \"06-08-2024 19:53\"\r\n"
			+ "}";
}
