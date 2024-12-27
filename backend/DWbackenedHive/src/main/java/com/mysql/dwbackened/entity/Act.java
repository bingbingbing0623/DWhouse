package com.mysql.dwbackened.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Act {
    private String movieId;
    private Integer actorId;
    private String isMainActor;
}
