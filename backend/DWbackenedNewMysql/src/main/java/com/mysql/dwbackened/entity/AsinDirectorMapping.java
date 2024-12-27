package com.mysql.dwbackened.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "asin_director_mapping")
public class AsinDirectorMapping {

    @Id
    @Column(name = "ASIN")
    private String movieId;

    @Column(name = "director_id")
    private String directorId;
}
