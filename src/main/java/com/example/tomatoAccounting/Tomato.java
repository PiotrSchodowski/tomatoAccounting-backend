package com.example.tomatoAccounting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tomato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "STRAIN")
    private String name;

    @Column(name = "KG")
    private float weight;

    @Column(name = "QUANTITY")
    private float quantity;

    @Column(name = "RESULT")
    private float weightPerStrain;
}
