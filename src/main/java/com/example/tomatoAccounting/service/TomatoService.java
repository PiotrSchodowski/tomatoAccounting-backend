package com.example.tomatoAccounting.service;

import com.example.tomatoAccounting.MessageResponse;
import com.example.tomatoAccounting.dto.TomatoDto;
import com.example.tomatoAccounting.entity.TomatoEntity;
import com.example.tomatoAccounting.entity.TomatoRepository;
import com.example.tomatoAccounting.entity.UserEntity;
import com.example.tomatoAccounting.entity.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TomatoService {

    private final UserRepository userRepository;
    private final TomatoRepository tomatoRepository;
    private final UserService userService;

    public TomatoService(UserRepository userRepository, TomatoRepository tomatoRepository, UserService userService) {
        this.userRepository = userRepository;
        this.tomatoRepository = tomatoRepository;
        this.userService = userService;
    }

    public ResponseEntity<?> createNewTomato(TomatoDto tomatoDto, String userEmail) {

        Optional<UserEntity> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            TomatoEntity tomatoEntity = new TomatoEntity();
            tomatoEntity.setName(tomatoDto.getName());
            tomatoEntity.setWeight(tomatoDto.getWeight());
            tomatoEntity.setQuantity(tomatoDto.getQuantity());
            tomatoEntity.setResult(tomatoDto.getResult());

            if (checkIsTomatoNameExistInUserTomatoList(tomatoDto.getName(), userEntity.getTomatoEntityList())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Taki pomidor już istnieje");

            } else {
                userEntity.getTomatoEntityList().add(tomatoEntity);
                return ResponseEntity.ok(userRepository.save(userEntity));
            }

        } else {
            MessageResponse errorResponse = new MessageResponse("Taki użytkownik nie istnieje");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    }

    public ResponseEntity<?> addHarvestToTomato(String userEmail, String tomatoName, float harvestWeight) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            List<TomatoEntity> tomatoEntityList = userEntity.getTomatoEntityList();
            for (TomatoEntity tomatoEntity : tomatoEntityList) {
                if (tomatoEntity.getName().equals(tomatoName)) {
                    tomatoEntity.setWeight(tomatoEntity.getWeight() + harvestWeight);
                    tomatoEntity.setResult(tomatoEntity.getWeight() / tomatoEntity.getQuantity());
                    return ResponseEntity.ok(userRepository.save(userEntity));
                }
            }
            MessageResponse errorResponse = new MessageResponse("Taki pomidor nie istnieje");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } else {
            MessageResponse errorResponse = new MessageResponse("Taki użytkownik nie istnieje");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    public ResponseEntity<?> deleteTomatoFromUserList(String userEmail, String tomatoName) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            List<TomatoEntity> tomatoEntityList = userEntity.getTomatoEntityList();
            for (TomatoEntity tomatoEntity : tomatoEntityList) {
                if (tomatoEntity.getName().equals(tomatoName)) {
                    tomatoEntityList.remove(tomatoEntity);
                    tomatoRepository.delete(tomatoEntity);
                    return ResponseEntity.ok(userRepository.save(userEntity));
                }
            }
            MessageResponse errorResponse = new MessageResponse("Taki pomidor nie istnieje");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } else {
            MessageResponse errorResponse = new MessageResponse("Taki użytkownik nie istnieje");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    public ResponseEntity<?> getTomatoList(String userEmail) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            return ResponseEntity.ok(userEntity.getTomatoEntityList());
        } else {
            MessageResponse errorResponse = new MessageResponse("Taki użytkownik nie istnieje");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    private boolean checkIsTomatoNameExistInUserTomatoList(String name, List<TomatoEntity> tomatoEntityList) {
        for (TomatoEntity tomatoEntity : tomatoEntityList) {
            if (tomatoEntity.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

}




