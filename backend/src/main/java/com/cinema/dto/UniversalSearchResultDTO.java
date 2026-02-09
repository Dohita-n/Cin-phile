package com.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversalSearchResultDTO {
    private Map<String, Object> films;
    private List<PersonWithFilmsDTO> actors;
    private List<PersonWithFilmsDTO> directors;
}
