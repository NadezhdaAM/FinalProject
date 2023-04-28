package com.example.finalproject.controllers;

import com.example.finalproject.enumm.Status;
import com.example.finalproject.models.*;
import com.example.finalproject.repositories.CategoryRepository;
import com.example.finalproject.repositories.OrderRepository;
import com.example.finalproject.repositories.PersonRepository;
import com.example.finalproject.security.PersonDetails;
import com.example.finalproject.services.OrderService;
import com.example.finalproject.services.PersonService;
import com.example.finalproject.services.ProductService;
import com.example.finalproject.util.ProductValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController<OrderPerson> {

    @Value("${upload.path}")
    private String uploadPath;

    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final PersonService personService;
    private final OrderRepository orderRepository;
    private final ProductValidator productValidator;
    private final OrderService orderService;
    private final PersonRepository personRepository;

    @Autowired
    public AdminController(ProductService productService, CategoryRepository categoryRepository, PersonService personService, OrderRepository orderRepository, ProductValidator productValidator, OrderService orderService, PersonRepository personRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.personService = personService;
        this.orderRepository = orderRepository;
        this.productValidator = productValidator;
        this.orderService = orderService;
        this.personRepository = personRepository;

    }

    // Метод по отображению главной страницы администратора с выводом товаров
    @GetMapping()
    public String admin(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        String role = personDetails.getPerson().getRole();
        if(role.equals("ROLE_USER")){
            return "redirect:/index";
        }
        model.addAttribute("products", productService.getAllProduct());
        model.addAttribute("persons", personService.getAllPersons());
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/admin";
    }

    // Метод по отображению формы добавления
    @GetMapping("/product/add")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("category", categoryRepository.findAll());
        return "product/addProduct";
    }

    // Метод по добавлению объекта с формы в таблицу product
    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("file_one") MultipartFile file_one, @RequestParam("file_two") MultipartFile file_two, @RequestParam("file_three") MultipartFile file_three, @RequestParam("file_four") MultipartFile file_four, @RequestParam("file_five") MultipartFile file_five, @RequestParam("category") int category, Model model) throws IOException {
        Category category_db = (Category) categoryRepository.findById(category)
                        .orElseThrow();
        System.out.println(category_db.getName());
        productValidator.validate(product,bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", categoryRepository.findAll());
            return "product/addProduct";
        }

        // Проверка на пустоту файла
        if (file_one != null) {
       //  Дирректория по сохранению файла
            File uploadDir = new File(uploadPath);
            // Если данной дирректории по пути не сущетсвует
            if (!uploadDir.exists()) {
                // Создаем данную дирректорию
                uploadDir.mkdir();
            }
            // Создаем уникальное имя файла
            // UUID представляет неизменный универсальный уникальный идентификатор
            String uuidFile = UUID.randomUUID().toString();
            // file_one.getOriginalFilename() - наименование файла с формы
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();
            // Загружаем файл по указаннопу пути
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));
            com.example.finalproject.models.Image image = new com.example.finalproject.models.Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);

        }

        if (file_two != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));
            com.example.finalproject.models.Image image = new com.example.finalproject.models.Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if (file_three != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));
            com.example.finalproject.models.Image image = new com.example.finalproject.models.Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if (file_four != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();
            file_four.transferTo(new File(uploadPath + "/" + resultFileName));
            com.example.finalproject.models.Image image = new com.example.finalproject.models.Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        if (file_five != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_five.getOriginalFilename();
            file_five.transferTo(new File(uploadPath + "/" + resultFileName));
            com.example.finalproject.models.Image image = new com.example.finalproject.models.Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        productService.saveProduct(product);
        return "redirect:/admin";
    }

    // Метод по удалению товара по id
    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        productService.deleteProduct(id);
        return "redirect:/admin";
    }

    // Метод по получению товара по id и отображение шаблона редактирования
    @GetMapping("/product/edit/{id}")
    public String editProduct(Model model, @PathVariable("id") int id) {
        model.addAttribute("product", productService.getProductId(id));
        model.addAttribute("category", categoryRepository.findAll());
        return "product/editProduct";
    }

    @PostMapping("/product/edit/{id}")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @PathVariable("id") int id, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", categoryRepository.findAll());
            return "product/editProduct";
        }
        productService.updateProduct(id, product);
        return "redirect:/admin";
    }

    // Метод возвращает страницу с выводом пользователей и кладет объект пользователя в модель
    @GetMapping("/person")
    public String person(Model model) {
        model.addAttribute("person", personService.getAllPerson());
        return "person/person";
    }


    // Метод возвращает страницу с подробной информацией о пользователе
    @GetMapping("/person/info/{id}")
    public String infoPerson(@PathVariable("id") int id, Model model){
        model.addAttribute("person", personService.getPersonById(id));
        return "person/personInfo";
    }

    // Метод возвращает страницу с формой редактирования пользователя и помещает в модель объект редактируемого пользователя по id
    @GetMapping("/person/edit/{id}")
    public String editPerson(@PathVariable("id")int id, Model model){
        model.addAttribute("editPerson", personService.getPersonById(id));
        return "person/editPerson";
    }

    // Метод принимает объект с формы и обновляет пользователя
    @PostMapping("/person/edit/{id}")
    public String editPerson(@ModelAttribute("editPerson") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id){
        if(bindingResult.hasErrors()){
            return "person/editPerson";
        }

        personService.updatePerson(id, person);
        return "redirect:/admin/person";
    }

    // Метод возвращает страницу с выводом заказов кладет объект заказов в модель
    @GetMapping("/orders")
    public String order(Model model){;
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }

    //   Метод возвращает страницу с формой редактирования заказа и помещает в модель объект редактируемого заказа по id
    @GetMapping("/orders/{id}")
    public String editOrder(@PathVariable("id")int id, Model model){
        model.addAttribute("orders", orderService.getOrderById(id));
        return "/admin/orders";
    }

    // Метод принимает объект с формы и обновляет заказы
    @PostMapping("/orders/{id}")
    public String changeStatus(@PathVariable("id") int id, @RequestParam("status") Status status){
        Order order_status=orderService.getOrderById(id);
        order_status.setStatus(status);
        orderService.updateOrderStatus(order_status);
        return "redirect:/admin/orders/";
    }

    // вывод всех пользователей в кабинет админа
    @GetMapping("/editPerson/{id}")
    public String changeRoleById(@PathVariable("id") int id, Model model){
        Person person = personService.getPersonId(id); //получаем id пользователя
        String role_person = person.getRole();
        if(!person.getLogin().equals("admin")) {
            if(!role_person.equals("ROLE_USER")) {
                role_person = "ROLE_USER";
            }else {
                role_person = "ROLE_ADMIN";
            }
        }
        person.setRole(role_person);
        personRepository.save(person);
        model.addAttribute("person");
        return "redirect:/admin";

    }
    // Метод по удалению пользователя по id
    @GetMapping ("/person/delete/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        personService.deletePerson(id);
        return "redirect:/admin";
    }




    // Поиск по последним символам номера заказа
    @PostMapping("/orders/search")
    public String searchOrderByLastSymbols(@RequestParam("value") String value, Model model) {
        model.addAttribute("search_order",orderRepository.findByNumberEndingWith(value));
        return "/admin/orders";
    }

}