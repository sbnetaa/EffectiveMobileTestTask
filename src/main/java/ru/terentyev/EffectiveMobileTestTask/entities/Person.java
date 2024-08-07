package ru.terentyev.EffectiveMobileTestTask.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "people")
@Schema(description = "Модель пользователя")
public class Person extends AbstractEntity {

		@Email(message = "Email должен иметь формат 'name@host.com'")
		@NotBlank(message = "Email не должен быть пустым")
		@Schema(description = "Email пользователя")
		private String email;
		@Size(min = 5, message = "Пароль должен содержать минимум 5 символов")
		@Schema(description = "Пароль пользователя")
		private String password;
		@JsonBackReference
		@Schema(description = "Подтверждение пароля пользователя")
		private transient String passwordConfirm;
		@OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
		@Schema(description = "Созданные пользователем задачи")
		private List<Task> createdTasks;
		@OneToMany(mappedBy = "executor", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
		@Schema(description = "Исполняемые пользователем задачи")
		private List<Task> executableTasks;
		@OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
		@Schema(description = "Комментарии пользователя")
		private List<Comment> comments;
		
		public Person(){}
}
