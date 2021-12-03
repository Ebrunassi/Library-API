package br.com.study.libraryapi.exception;

import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public class ApiErrors {
    private List<String> errors;
    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<String>();
        bindingResult.getAllErrors().forEach(e -> this.errors.add(e.getDefaultMessage()));
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
