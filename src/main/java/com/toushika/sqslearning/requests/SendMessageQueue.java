package com.toushika.sqslearning.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageQueue {
    private String queueName;
    private String msg;
}
