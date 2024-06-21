package ru.maxima.springrest.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.maxima.springrest.models.Person;
import ru.maxima.springrest.security.PersonDetailsService;

@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService service;

    @Autowired
    public PersonValidator(PersonDetailsService service) {
        this.service = service;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Person person = (Person) target;

        try {
            service.loadUserByUsername(person.getName());
        } catch (UsernameNotFoundException e) {
            return;
        }

        errors.rejectValue("name", "User with this nickname already exist");

    }
}
