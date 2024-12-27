package com.mysql.dwbackened.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorTopRelationInfoDto {
    private String name1;
    private String name2;
    private float averageRating;
}
