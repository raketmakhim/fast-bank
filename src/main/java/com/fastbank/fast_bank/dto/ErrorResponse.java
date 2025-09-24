package com.fastbank.fast_bank.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
public class ErrorResponse {
    private String message;
    private String errorCode;
    private LocalDateTime timestamp;

}
