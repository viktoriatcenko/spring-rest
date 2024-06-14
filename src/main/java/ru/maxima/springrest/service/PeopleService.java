package ru.maxima.springrest.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.maxima.springrest.exceptions.IdMoreThanTenThousandsException;
import ru.maxima.springrest.exceptions.PersonNotFoundException;
import ru.maxima.springrest.models.Person;
import ru.maxima.springrest.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {

    private final PeopleRepository repository;

    @Autowired
    public PeopleService(PeopleRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<Person> getAllPeople() {
        List<Person> people = repository.findAll();


        test();

        return people;
    }

    @Transactional
    public Person findById(Long id) {
        if (id > 10000) {
            throw new IdMoreThanTenThousandsException("Id more than 10000. Person with this id is not exist");
        }

        Optional<Person> byId = repository.findById(id);

        return byId.orElseThrow(() -> new PersonNotFoundException("Person is not found from DB Maxima"));
    }

    @Transactional
    public void save(Person person) {

        person.setRole("ROLE_USER");

        repository.save(person);
    }

    @Transactional
    public void update(Person person, Integer id) {
        person.setId(Long.valueOf(id));
        repository.save(person);
    }

    @Transactional
    public void delete(Integer id) {
        repository.deleteById(Long.valueOf(id));
    }

    @Transactional
    public void test() {

        List<Person> v = repository.findPeopleByNameLike("V%");

        System.out.println(v);
    }
}
