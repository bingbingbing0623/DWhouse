package com.mysql.dwbackened.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectorActor {
    private int movieId;
    private int directorId;
    private int actorId;
}
