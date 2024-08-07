package ru.terentyev.EffectiveMobileTestTask.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.terentyev.EffectiveMobileTestTask.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	Page<Comment> findByTaskId(Integer id, Pageable pageable);
}
