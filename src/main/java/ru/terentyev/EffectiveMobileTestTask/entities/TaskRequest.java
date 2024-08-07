package ru.terentyev.EffectiveMobileTestTask.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Модель запроса задачи")
public class TaskRequest implements Request {
		@Schema(description = "Уникальный идентификатор задачи")
		private Integer id;
		@Schema(description = "Наименование задачи")
		private String title;
		@Schema(description = "Описание задачи")
		private String description;
		@Schema(description = "Статус задачи")
		private String status;
		@Schema(description = "Приоритет задачи")
		private String priority;
		@Schema(description = "ID автора задачи (используется только в /search запросах)")
		private Integer authorId;
		@Schema(description = "ID исполнителя задачи")
		private Integer executorId;
		@Schema(description = "Название поля для сортировки", example = "id")
		private String sortBy;
		@Schema(description = "Номер страницы (начинается с 1; если запрос одной задачи (по ее ID), то страница комментариев, а если многих, то страница задач", example = "1")
		private Integer page;
}
