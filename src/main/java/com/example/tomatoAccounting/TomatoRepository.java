package com.example.tomatoAccounting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TomatoRepository extends JpaRepository<Tomato, Long> {

    Tomato findByName(String name);
}
