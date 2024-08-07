package ru.terentyev.EffectiveMobileTestTask.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.terentyev.EffectiveMobileTestTask.exceptions.PriorityNotFoundException;
import ru.terentyev.EffectiveMobileTestTask.exceptions.StatusNotFoundException;

@Getter
@Setter
@Entity
@Table(name = "tasks")
@Schema(description = "Модель задачи")
public class Task extends AbstractEntity {
	
		@NotBlank(message = "Заголовок не может быть пустым")
		@Schema(description = "Название (заголовок) задачи")
		private String title;
		@Schema(description = "Описание задачи")
		private String description;
		@NotNull(message = "У задачи должен быть статус")
		@Enumerated(EnumType.ORDINAL)
		@Schema(description = "Статус (enum) задачи")
		private Status status;
		@NotNull(message = "У задачи должен быть приоритет")
		@Enumerated(EnumType.ORDINAL)
		@Schema(description = "Приоритет (enum) задачи")
		private Priority priority;
		@ManyToOne
		@JoinColumn(name = "author_id", referencedColumnName = "id")
		@JsonIdentityReference(alwaysAsId = true)
		@Schema(description = "Автор задачи")
		private Person author;
		@NotNull(message = "У задачи должен быть исполнитель")
		@ManyToOne
		@JoinColumn(name = "executor_id", referencedColumnName = "id")
		@JsonIdentityReference(alwaysAsId = true)
		@Schema(description = "Исполнитель задачи")
		private Person executor;
		@OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
		@Schema(description = "Комментарии к задаче")
		private List<Comment> comments;
		
		public Task(){}
		
		@Schema(description = "Статусы задачи")
		public enum Status {
			AWAITING("В ожидании"), PROCESSING("Выполняется"), COMPLETED("Завершена");
			
			private String description;
			
			private Status(String description) {
				this.description = description;
			}
			
			public static Status getStatusByName(String name) {
				for (Status status : Status.values()) {
					if (status.name().equals(name))
					return status;
				}
				throw new StatusNotFoundException(name);
			}
			
			public static boolean existsByName(String name) {
		        try {
		            Status.valueOf(name);
		            return true;
		        } catch (IllegalArgumentException e) {
		            throw new StatusNotFoundException(name);
		        }
		    }
		}
		
		@Schema(description = "Приоритеты задачи")
		public enum Priority {
			LOW("Низкий"), MEDIUM("Средний"), HIGH("Высокий");
			
			private String description;
			
			private Priority(String description) {
				this.description = description;
			}
			
			public static Priority getPriorityByName(String name) {
				for (Priority priority : Priority.values()) {
					if (priority.name().equals(name))
					return priority;
				}
				throw new PriorityNotFoundException(name);
			}
			
			public static boolean existsByName(String name) {
		        try {
		            Priority.valueOf(name);
		            return true;
		        } catch (IllegalArgumentException e) {
		            throw new PriorityNotFoundException(name);
		        }
		    }
		}
}
