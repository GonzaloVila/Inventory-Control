package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class OrderDTO {

    private Integer id;
    private Date date;
    private String state;
    private Double total;
    private Integer employeeId;
    private Integer providerId;

    public OrderDTO(){}

    public OrderDTO(Integer id, Date date, String state, Double total, Integer employeeId, Integer providerId) {
        this.id = id;
        this.date = date;
        this.state = state;
        this.total = total;
        this.employeeId = employeeId;
        this.providerId = providerId;
    }
}
