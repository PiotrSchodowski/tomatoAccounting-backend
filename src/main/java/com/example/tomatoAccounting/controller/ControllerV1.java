package com.example.tomatoAccounting.controller;


import com.example.tomatoAccounting.dto.TomatoDto;
import com.example.tomatoAccounting.entity.UserRepository;
import com.example.tomatoAccounting.service.TomatoService;
import com.example.tomatoAccounting.service.UserService;
import com.example.tomatoAccounting.dto.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
public class ControllerV1 {

    @Autowired
    TomatoService tomatoService;

    @PostMapping("/{userEmail}/createNewTomato")
    public ResponseEntity<?> createNewTomato(@RequestBody TomatoDto tomatoDto, @PathVariable String userEmail) {
        try{
            return tomatoService.createNewTomato(tomatoDto, userEmail);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping("/{userEmail}/addHarvestToTomato/{tomatoName}/{harvestWeight}")
    public ResponseEntity<?> addHarvestToTomato(@PathVariable String userEmail, @PathVariable String tomatoName, @PathVariable float harvestWeight) {
        try{
            return tomatoService.addHarvestToTomato(userEmail, tomatoName, harvestWeight);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping("/{userEmail}/deleteTomato/{tomatoName}")
    public ResponseEntity<?> deleteTomato(@PathVariable String userEmail, @PathVariable String tomatoName) {
        try{
            return tomatoService.deleteTomatoFromUserList(userEmail, tomatoName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/{userEmail}/getTomatoList")
    public ResponseEntity<?> getTomatoList(@PathVariable String userEmail) {
        try{
            return tomatoService.getTomatoList(userEmail);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

}







