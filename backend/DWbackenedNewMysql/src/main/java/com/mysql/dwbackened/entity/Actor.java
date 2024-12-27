package com.mysql.dwbackened.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wyx20
 * @version 1.0
 * @title Actor
 * @description
 * @create 2023/12/25 13:42
 */

@Entity
@Table(name = "actor")
public class Actor {

    @Id
    @Column(name = "actor_id")
    private Integer actorId;

    @Column(name = "actor_name")
    private String actorName;
}
