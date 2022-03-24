package com.toushika.sqslearning.controller;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.Message;
import com.toushika.sqslearning.requests.QueueInfo;
import com.toushika.sqslearning.requests.SendMessageQueue;
import com.toushika.sqslearning.requests.SendMultipleMessageRequest;
import com.toushika.sqslearning.service.SqsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SqsController {
    private final AmazonSQSAsync amazonSQSAsync;
    private final SqsService sqsService;

    private static String queueUrl;

    @PostMapping("/create-queue")
    public String createQueue(@RequestBody QueueInfo queueInfo) {
        log.info("SqsController :: createQueue :: queueInfo :: {}", queueInfo);
        return sqsService.createQueue(queueInfo);
    }

    @PostMapping("/list-queues")
    public List<String> listQueues() {
        return sqsService.listQueues();
    }

    @PostMapping("/send-message")
    public String sendMessage(@RequestBody SendMessageQueue sendMessageQueue) {
        return sqsService.sendMessage(sendMessageQueue);
    }

    @PostMapping("/receive-message-without-delete")
    public String deleteMessage(@RequestBody QueueInfo queueInfo) {
        return sqsService.receiveMessagesWithoutDelete(queueInfo);
    }

    @PostMapping("/receive-message-with-delete")
    public String receiveMessageWithDelete(@RequestBody QueueInfo queueInfo) {
        return sqsService.receiveMessagesWithDelete(queueInfo);
    }

    @PostMapping("/send-multiple-msg")
    public String sendMultipleMessage(@RequestBody SendMultipleMessageRequest sendMultipleMessageRequest) {
        return sqsService.sendMultipleMessages(sendMultipleMessageRequest);
    }

    @PostMapping("/receive-multiple-msg")
    public List<Message> receiveMultipleMessage(@RequestBody QueueInfo queueInfo) {
        return sqsService.receiveMultipleMessages(queueInfo);
    }

    @PostMapping("/delete-queue")
    public String deleteQueue(@RequestBody QueueInfo queueInfo) {
        return sqsService.deleteQueue(queueInfo);
    }


}
