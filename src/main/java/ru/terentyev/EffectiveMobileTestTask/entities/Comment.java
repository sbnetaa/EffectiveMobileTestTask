package ru.terentyev.EffectiveMobileTestTask.entities;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comments")
@Schema(description = "Модель комментария")
public class Comment extends AbstractEntity {

		@NotBlank(message = "Комментарий не может быть пустым")
		@Schema(description = "Тело (содержимое) комментария")
		private String body;
		@ManyToOne
		@JoinColumn(name = "task_id", referencedColumnName = "id")
		@JsonIdentityReference(alwaysAsId = true)
		@Schema(description = "Задача, которой принадлежит комментарий")
		private Task task;
		@ManyToOne
		@JoinColumn(name = "author_id", referencedColumnName = "id")
		@JsonIdentityReference(alwaysAsId = true)
		@Schema(description = "Автор комментария")
		private Person author;

		public Comment(){}
}
