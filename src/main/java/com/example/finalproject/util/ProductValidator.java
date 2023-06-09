package com.example.finalproject.util;


import com.example.finalproject.models.Product;
import com.example.finalproject.services.ProductService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductValidator implements Validator {

    private final ProductService productService;

    public ProductValidator(ProductService productService){
        this.productService = productService;
    }

    // В данном методе указываем для какой модели предназначен данные валидатор
    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;
        if(productService.getProductFindByTitle(product) != null){
            errors.rejectValue("title", "","Данное наименование товара уже используется");
        }
    }
}
