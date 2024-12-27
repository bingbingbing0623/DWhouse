package com.mysql.dwbackened.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorRelationCountDto {
    private int firstPersonId;
    private int secondPersonId;
    private int reviewCount;
    private int cooperationCount;
}
