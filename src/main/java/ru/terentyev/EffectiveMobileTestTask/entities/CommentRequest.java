package ru.terentyev.EffectiveMobileTestTask.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Модель запроса добавления комментария")
public class CommentRequest implements Request {
	@Schema(description = "ID задачи для добавления комментария", example = "1")
	private Integer taskId;
	@Schema(description = "Тело комментария", example = "Any text")
	private String body;
}
