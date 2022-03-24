package com.toushika.sqslearning.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMultipleMessageRequest {
    private String queueName;
    private List<MessageInfo> messageInfos;
}
