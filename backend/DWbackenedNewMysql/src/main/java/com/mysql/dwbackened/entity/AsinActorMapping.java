package com.mysql.dwbackened.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "asin_actor_mapping")
public class AsinActorMapping {

    @Id
    @Column(name = "ASIN")
    private String movieId;

    @Column(name = "actor_id")
    private String actorId;

    @Column(name = "is_main_actor")
    private Boolean isMainActor;
}
