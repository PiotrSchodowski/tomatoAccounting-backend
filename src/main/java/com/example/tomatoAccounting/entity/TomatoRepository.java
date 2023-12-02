package com.example.tomatoAccounting.entity;

import com.example.tomatoAccounting.entity.TomatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TomatoRepository extends JpaRepository<TomatoEntity, Long> {

    Optional<TomatoEntity> findByName(String name);

}
