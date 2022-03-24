package com.toushika.sqslearning.service;

import com.amazonaws.services.sqs.model.Message;
import com.toushika.sqslearning.requests.QueueInfo;
import com.toushika.sqslearning.requests.SendMessageQueue;
import com.toushika.sqslearning.requests.SendMultipleMessageRequest;

import java.util.List;

public interface SqsService {
    String createQueue(QueueInfo queueInfo);

    List<String> listQueues();

    String sendMessage(SendMessageQueue sendMessageQueue);

    String receiveMessagesWithoutDelete(QueueInfo queueInfo);

    String receiveMessagesWithDelete(QueueInfo queueInfo);

    String sendMultipleMessages(SendMultipleMessageRequest sendMultipleMessageRequest);

    List<Message> receiveMultipleMessages(QueueInfo queueInfo);

    String deleteQueue(QueueInfo queueInfo);

}
