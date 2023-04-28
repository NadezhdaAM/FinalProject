package com.example.finalproject.util;

import com.example.finalproject.models.Person;
import com.example.finalproject.services.PersonService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PersonService personService;

    public PersonValidator(PersonService personService) {this.personService = personService;
    }
    // В данном методе указываем для какой модели предназначен данный валидатор
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);

    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if(personService.getPersonFindByLogin(person) != null){
            errors.rejectValue("login", "", "Данный логин уже зарегистрирован в системе");
        }
    }

    public void findUser(Object target, Errors errors) {
        Person person = (Person) target;
        // Если метод по поиску пользователя по логину равен 0 тогда такой логин не найден
        if(personService.getPersonFindByLogin(person) == null){
            errors.rejectValue("login", "", "Пользователь c таким логином не найден.");
        }
    }


}



