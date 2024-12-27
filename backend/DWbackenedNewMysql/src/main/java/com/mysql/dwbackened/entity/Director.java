package com.mysql.dwbackened.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wyx20
 * @version 1.0
 * @title Director
 * @description
 * @create 2023/12/25 14:06
 */


@Entity
@Table(name = "director")
public class Director {

    @Id
    @Column(name = "director_id")
    private Integer directorId;

    @Column(name = "director_name")
    private String directorName;
}
