package com.ejercicio2.estancias.controladores;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MultipartException.class)
    public String handleError(MultipartException e, RedirectAttributes r){
        
        r.addFlashAttribute("error", "El archivo supera los 4MB");
        return "redirect:/casa/guardarCasa/{id}";
        
    }
}
