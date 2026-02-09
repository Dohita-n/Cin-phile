package com.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonWithFilmsDTO {
    private Long id;
    private String name;
    private String profilePath;
    private List<Map<String, Object>> films;
}
