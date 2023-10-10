package com.example.tomatoAccounting;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TomatoController {

    TomatoService tomatoService;

    public TomatoController(TomatoService tomatoService) {
        this.tomatoService = tomatoService;
    }

    @GetMapping("/tomatoes")
    public List<Tomato> getTomatoes(){
        return tomatoService.getTomatoes();
    }

    @PostMapping("/tomatoes/{name}/{quantity}")
    public Tomato createTomato(String name, float quantity){
        return tomatoService.createTomato(name, quantity);
    }
}
