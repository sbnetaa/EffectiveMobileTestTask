package ru.terentyev.EffectiveMobileTestTask.services;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.terentyev.EffectiveMobileTestTask.entities.Comment;
import ru.terentyev.EffectiveMobileTestTask.entities.CommentRequest;
import ru.terentyev.EffectiveMobileTestTask.entities.Task;
import ru.terentyev.EffectiveMobileTestTask.exceptions.SortFieldNotFoundException;
import ru.terentyev.EffectiveMobileTestTask.repositories.CommentRepository;
import ru.terentyev.EffectiveMobileTestTask.security.PersonDetails;

@Service
@Transactional(readOnly = false)
public class CommentServiceImpl implements CommentService {

	private CommentRepository commentRepository;
	private TaskService taskService;

	
	@Autowired
	public CommentServiceImpl(CommentRepository commentRepository, @Lazy TaskService taskService) {
		super();
		this.commentRepository = commentRepository;
		this.taskService = taskService;
	}
	
	public List<Comment> findAll(Integer page, String sortBy) {
		return commentRepository.findAll(PageRequest.of(page == null ? 0 : page - 1, 10, Sort.by(Sort.Direction.ASC, sortBy == null ? "id" : sortBy))).getContent();
	}

	@Override
	public List<Comment> findByTaskId(Integer taskId, Integer page, String sortBy) {
		if (sortBy != null && !isFieldExists(Comment.class, sortBy)) throw new SortFieldNotFoundException(sortBy);
		return commentRepository.findByTaskId(taskId, PageRequest.of(page == null ? 0 : page - 1, 10, Sort.by(Sort.Direction.ASC, sortBy == null ? "id" : sortBy))).getContent();
	}
	
	public boolean isFieldExists(Class<?> clazz, String fieldName) {
	    while (clazz != null) {
	        for (Field field : clazz.getDeclaredFields()) {
	            if (field.getName().equals(fieldName)) {
	                return true;
	            }
	        }
	        clazz = clazz.getSuperclass();
	    }
	    return false;
	}


	@Override
	@Transactional(readOnly = false)
	public Task postComment(CommentRequest commentRequest, PersonDetails pd) {
		Comment comment = new Comment();
		comment.setBody(commentRequest.getBody());
		Task task = taskService.findById(commentRequest.getTaskId());
		comment.setTask(task);
		comment.setAuthor(pd.getPerson());
		commentRepository.save(comment);
		task.setComments(findByTaskId(commentRequest.getTaskId(), 1, "id"));
		return task;
	}
}
