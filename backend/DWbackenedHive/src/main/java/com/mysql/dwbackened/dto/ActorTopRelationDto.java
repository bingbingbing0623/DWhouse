package com.mysql.dwbackened.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorTopRelationDto {
    private int firstPersonId;
    private int secondPersonId;
    private int averageRating;
}
