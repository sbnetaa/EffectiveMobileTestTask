package ru.terentyev.EffectiveMobileTestTask.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import ru.terentyev.EffectiveMobileTestTask.entities.AuthRequest;
import ru.terentyev.EffectiveMobileTestTask.entities.AuthResponse;
import ru.terentyev.EffectiveMobileTestTask.entities.Person;
import ru.terentyev.EffectiveMobileTestTask.entities.Task;
import ru.terentyev.EffectiveMobileTestTask.exceptions.InvalidCredentialsException;
import ru.terentyev.EffectiveMobileTestTask.exceptions.MismatchPasswordsException;
import ru.terentyev.EffectiveMobileTestTask.exceptions.PersonAlreadyExistsException;
import ru.terentyev.EffectiveMobileTestTask.repositories.PersonRepository;
import ru.terentyev.EffectiveMobileTestTask.security.JwtUtil;
import ru.terentyev.EffectiveMobileTestTask.security.PersonDetails;

@Service
@Transactional(readOnly = true)
public class PersonDetailsService implements UserDetailsService {
	
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private PersonRepository personRepository;
    private JwtUtil jwtUtil;
    
    @Autowired
    public PersonDetailsService(@Lazy BCryptPasswordEncoder bCryptPasswordEncoder,
			PersonRepository personRepository, JwtUtil jwtUtil) {
		super();	
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.personRepository = personRepository;
		this.jwtUtil = jwtUtil;
	}

    public PersonDetailsService(){}
    
	@Override
    public UserDetails loadUserByUsername(String email) {    	
        Optional<Person> person = personRepository.findByEmail(email); 
            return new PersonDetails(person.orElseThrow(() -> new UsernameNotFoundException("Email " + email + " не найден.")));
  
    }
	
    public AuthResponse authenticateUser(AuthRequest request) {
        Person person = personRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Email " + request.getEmail() + " не найден."));
        if (person != null && request.getPassword() != null && new BCryptPasswordEncoder().matches(request.getPassword(), person.getPassword())) {
            String token = jwtUtil.generateToken(person.getEmail());
            return new AuthResponse(token);
        }
        throw new InvalidCredentialsException();
    }
    
    @Transactional(readOnly = false)
    public Person registerNewPerson(AuthRequest authRequest) {
    	validateAuthRequest(authRequest);
    	Person newPerson = new Person();
    	newPerson.setEmail(authRequest.getEmail());
    	newPerson.setPassword(authRequest.getPassword());
    	validatePerson(newPerson);
    	newPerson.setPassword(bCryptPasswordEncoder.encode(authRequest.getPassword()));   	
    	return personRepository.save(newPerson);
    }
    
    private void validateAuthRequest(AuthRequest authRequest) {
    	if (!authRequest.getPassword().equals(authRequest.getPasswordConfirm()))
    		throw new MismatchPasswordsException();
    	if (findByEmail(authRequest.getEmail()) != null)
    		throw new PersonAlreadyExistsException(authRequest.getEmail());
    }
    
    private void validatePerson(Person person) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Person>> violations = validator.validate(person);

        if (!violations.isEmpty()) 
            throw new ConstraintViolationException(violations);
    }
    
    public Person findById(Integer Id) {
    	Optional<Person> oPerson = personRepository.findById(Id); 
        return oPerson.orElse(null);    
    }

    
    public Person findByEmail(String email) {
       	Optional<Person> oPerson = personRepository.findByEmail(email);
    	return oPerson.orElse(null);
    }
    
    public List<Person> findAll(){
    	return personRepository.findAll();
    }
}
