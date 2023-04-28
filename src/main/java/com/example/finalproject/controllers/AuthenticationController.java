package com.example.finalproject.controllers;

import com.example.finalproject.models.Person;
import com.example.finalproject.services.PersonService;
import com.example.finalproject.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private final PersonValidator personValidator;

    private final PersonService personService;

    @Autowired
    public AuthenticationController(PersonValidator personValidator, PersonService personService) {
        this.personValidator = personValidator;
        this.personService = personService;
    }

    @GetMapping("/password/change")
    public String changePassword(Model model){
        model.addAttribute("person", new Person());
        return "password";
    }

    @PostMapping("/password/change")
    public String changePassword(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        Person person_db = personService
                .getPersonFindByLogin(person);
        int id = person_db.getId();
        String password = person.getPassword();
        personService.changePassword(id, password);

        return "redirect:/index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("person", new Person());
        return "registration";
    }

    @PostMapping("/registration")
    public String resultRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            return "registration";
        }
        personService.register(person);
        return "redirect:/index";
    }
}
