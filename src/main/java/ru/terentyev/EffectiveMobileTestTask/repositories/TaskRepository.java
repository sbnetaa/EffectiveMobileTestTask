package ru.terentyev.EffectiveMobileTestTask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.terentyev.EffectiveMobileTestTask.entities.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

}
