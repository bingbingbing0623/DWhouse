package com.mysql.dwbackened.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseDate {
    @TableId
    private String movieId;
    private String year;
    private String month;
    private String day;
    private String season;
    private String weekday;
}
