package com.tickety.reservation.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseWrapper<T> {

    private boolean success;
    private T data;
    private String message;
}
