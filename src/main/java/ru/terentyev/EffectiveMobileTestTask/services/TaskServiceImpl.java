package ru.terentyev.EffectiveMobileTestTask.services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import ru.terentyev.EffectiveMobileTestTask.entities.Task;
import ru.terentyev.EffectiveMobileTestTask.entities.TaskRequest;
import ru.terentyev.EffectiveMobileTestTask.exceptions.AlienTaskException;
import ru.terentyev.EffectiveMobileTestTask.exceptions.MissingIdentifierException;
import ru.terentyev.EffectiveMobileTestTask.exceptions.SortFieldNotFoundException;
import ru.terentyev.EffectiveMobileTestTask.exceptions.TaskNotFoundException;
import ru.terentyev.EffectiveMobileTestTask.repositories.TaskRepository;
import ru.terentyev.EffectiveMobileTestTask.security.PersonDetails;

@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {
		
	private TaskRepository taskRepository;
	private CommentService commentService;
	private PersonDetailsService personDetailsService;
	private EntityManager entityManager;

	@Autowired
	public TaskServiceImpl(TaskRepository taskRepository, CommentService commentService
			, PersonDetailsService personDetailsService, EntityManager entityManager) {
		super();
		this.taskRepository = taskRepository;
		this.commentService = commentService;
		this.personDetailsService = personDetailsService;
		this.entityManager = entityManager;
	}

	@Override
	public List<Task> showTasks(Integer id, Integer page, String sortBy) {
		if (id != null) {
			Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
			task.setComments(commentService.findByTaskId(task.getId(), page, sortBy));						
			return List.of(task);
		}
		if (sortBy != null && !isFieldExists(Task.class, sortBy)) throw new SortFieldNotFoundException(sortBy);
		return taskRepository.findAll(PageRequest.of(page == null ? 0 : page - 1, 10, Sort.by(Sort.Direction.ASC, sortBy == null ? "id" : sortBy))).getContent();
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
	public Task saveTask(TaskRequest taskRequest, Task task, PersonDetails pd) {
		if (task.getAuthor() != null && taskRequest.getId() == null) throw new MissingIdentifierException();
		if (task.getAuthor() != null && pd.getPerson().getId() != task.getAuthor().getId()) {			
			if (!pd.getPerson().equals(task.getExecutor()) || taskRequest.getPriority() != null
					|| taskRequest.getTitle() != null || taskRequest.getDescription() != null
					|| taskRequest.getExecutorId() != null) {
				throw new AlienTaskException();
			} else if (taskRequest.getStatus() != null && Task.Status.existsByName(taskRequest.getStatus())) {
				task.setStatus(Task.Status.getStatusByName(taskRequest.getStatus()));
				return taskRepository.save(task);
			}
		}			

		if (taskRequest.getTitle() != null)
		task.setTitle(taskRequest.getTitle());
		if (taskRequest.getDescription() != null)
		task.setDescription(taskRequest.getDescription());
		if (taskRequest.getStatus() != null && Task.Status.existsByName(taskRequest.getStatus()))
			task.setStatus(Task.Status.getStatusByName(taskRequest.getStatus()));
		if (taskRequest.getPriority() != null && Task.Priority.existsByName(taskRequest.getPriority()))
			task.setPriority(Task.Priority.getPriorityByName(taskRequest.getPriority()));
		if (taskRequest.getExecutorId() == null)
			task.setExecutor(pd.getPerson());
		else
			task.setExecutor(personDetailsService.findById(taskRequest.getExecutorId()));
		task.setAuthor((pd.getPerson()));
		validateTask(task);
		return taskRepository.save(task);
	}
	
	@Transactional(readOnly = false)
    private void validateTask(Task task) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        if (!violations.isEmpty()) 
            throw new ConstraintViolationException(violations);
    }
	

	@Override
	public Task findById(Integer id) {
		Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
		return task;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteById(Integer id, PersonDetails pd) {
		if (!taskRepository.existsById(id)) throw new TaskNotFoundException(id);
		if (pd.getPerson().getId() != taskRepository.findById(id).get().getAuthor().getId())
			throw new AlienTaskException();
		taskRepository.deleteById(id);
	}
	
	@Override
	public List<Task> searchByCriteria(TaskRequest request) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Task> cq = cb.createQuery(Task.class);
		Root<Task> root = cq.from(Task.class);
		List<Predicate> predicates = new ArrayList<>();

		
		if (request.getExecutorId() != null) 					
			predicates.add(cb.equal(root.get("executor").get("id"), request.getExecutorId()));
		
		if (request.getAuthorId() != null) 
			predicates.add(cb.equal(root.get("author").get("id"), request.getAuthorId()));
		
		if (request.getTitle() != null) 
			predicates.add(cb.like(root.get("title"), "%" + request.getTitle() + "%"));
		
		if (request.getDescription() != null) 
			predicates.add(cb.like(root.get("description"), "%" + request.getDescription() + "%"));

		if (request.getStatus() != null) 
			predicates.add(cb.equal(root.get("status"), Task.Status.getStatusByName(request.getStatus())));
		
		if (request.getPriority() != null) 
			predicates.add(cb.equal(root.get("priority"), Task.Status.getStatusByName(request.getPriority())));
		
	
		String order = request.getSortBy();
		
		if (order != null) {
			if (order.contains("DESC"))
				cq.orderBy(cb.desc(root.get(order.replace("DESC", ""))));
			else
				cq.orderBy(cb.asc(root.get(order)));
		} else {
			cq.orderBy(cb.asc(root.get("id")));
		}
		
		if (!predicates.isEmpty()) 
		cq.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
		TypedQuery<Task> tq = entityManager.createQuery(cq);
		return tq.getResultList();
	}
}
