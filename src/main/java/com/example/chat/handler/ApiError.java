package com.example.chat.handler;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ApiError {
    private String status;
    private String reason;
    private String message;
    private String timestamp;
}
