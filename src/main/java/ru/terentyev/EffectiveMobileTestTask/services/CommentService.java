package ru.terentyev.EffectiveMobileTestTask.services;

import java.util.List;

import ru.terentyev.EffectiveMobileTestTask.entities.Comment;
import ru.terentyev.EffectiveMobileTestTask.entities.CommentRequest;
import ru.terentyev.EffectiveMobileTestTask.entities.Task;
import ru.terentyev.EffectiveMobileTestTask.security.PersonDetails;

public interface CommentService {

	List<Comment> findAll(Integer page, String sortBy);
	List<Comment> findByTaskId(Integer taskId, Integer page, String sortBy);
	Task postComment(CommentRequest commentRequest, PersonDetails pd);
}
