package ru.maxima.springrest.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.maxima.springrest.dto.PersonDTO;
import ru.maxima.springrest.models.Person;
import ru.maxima.springrest.service.PeopleService;
import ru.maxima.springrest.util.JWTUtil;
import ru.maxima.springrest.validation.PersonValidator;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PeopleService peopleService;
    private final JWTUtil jwtUtil;
    private final ModelMapper mapper;
    private final PersonValidator personValidator;


    @Autowired
    public AuthController(PeopleService peopleService, JWTUtil jwtUtil, ModelMapper mapper, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
        this.personValidator = personValidator;
    }

    @GetMapping("/login")
    public String login() {
        return "successfully login";
    }

    @PostMapping("/registration")
    public Map<String, String> registration(@RequestBody @Valid  PersonDTO personDTO,
                            BindingResult bindingResult) {

        Person person = peopleService.convertFromDTOToPerson(personDTO);

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return Map.of("message", "error body");
        }

        peopleService.save(person);

        String token = jwtUtil.generateToken(person.getName());

        return Map.of("jwt-token", token);
    }
}
