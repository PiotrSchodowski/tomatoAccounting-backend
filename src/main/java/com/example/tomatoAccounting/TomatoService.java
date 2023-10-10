package com.example.tomatoAccounting;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TomatoService {

    TomatoRepository tomatoRepository;

    public TomatoService(TomatoRepository tomatoRepository) {
        this.tomatoRepository = tomatoRepository;
    }

    public List<Tomato> getTomatoes() {
        return tomatoRepository.findAll();
    }

    public Tomato createTomato(String name, float quantity) {
        Optional<Tomato> optionalTomato = Optional.ofNullable(tomatoRepository.findByName(name));
        Tomato tomato;
        if (optionalTomato.isPresent()) {
            tomato = optionalTomato.get();
            tomato.setQuantity(quantity);
            tomato.setWeightPerStrain(tomato.getWeight() / tomato.getQuantity());
        } else {
            tomato = new Tomato();
            tomato.setName(name);
            tomato.setQuantity(quantity);
        }
        return tomatoRepository.save(tomato);
    }
}

