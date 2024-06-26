package com.example.student.vo;

import lombok.Data;

import java.util.List;

/**柱形图返回结果集对象*/
@Data
public class BarEchartsSeriesModel {
    private List<Double> data;
    private String type;
    private String name;
}
