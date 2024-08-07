package ru.terentyev.EffectiveMobileTestTask.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.terentyev.EffectiveMobileTestTask.entities.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

	Optional<Person> findByEmail(String email);
}
