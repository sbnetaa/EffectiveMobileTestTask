package ru.terentyev.EffectiveMobileTestTask.services;

import java.util.List;

import ru.terentyev.EffectiveMobileTestTask.entities.Task;
import ru.terentyev.EffectiveMobileTestTask.entities.TaskRequest;
import ru.terentyev.EffectiveMobileTestTask.security.PersonDetails;

public interface TaskService {
	List<Task> showTasks(Integer id, Integer page, String sortBy);
	Task saveTask(TaskRequest taskRequest, Task taskToChange, PersonDetails pd);
	Task findById(Integer id);
	void deleteById(Integer id, PersonDetails pd);
	List<Task> searchByCriteria(TaskRequest taskRequest);
}
