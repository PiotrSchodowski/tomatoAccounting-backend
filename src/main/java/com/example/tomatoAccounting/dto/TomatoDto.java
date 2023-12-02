package com.example.tomatoAccounting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TomatoDto {
    private String name;
    private float weight;
    private float quantity;
    private float result;
}
