package com.mysql.dwbackened.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorActor {
    private String movieId;
    private int firstPersonId;
    private int secondPersonId;
}
