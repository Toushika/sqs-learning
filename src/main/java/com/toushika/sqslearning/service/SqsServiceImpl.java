package com.toushika.sqslearning.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.*;
import com.toushika.sqslearning.requests.MessageInfo;
import com.toushika.sqslearning.requests.QueueInfo;
import com.toushika.sqslearning.requests.SendMessageQueue;
import com.toushika.sqslearning.requests.SendMultipleMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SqsServiceImpl implements SqsService {
    private final AmazonSQSAsync amazonSQSAsync;

    @Override
    public String createQueue(QueueInfo queueInfo) {
        amazonSQSAsync.createQueue(queueInfo.getQueueName());
        return queueInfo.getQueueName() + " - has been created Successfully";
    }

    @Override
    public List<String> listQueues() {
        return amazonSQSAsync.listQueues().getQueueUrls();
    }

    @Override
    public String sendMessage(SendMessageQueue sendMessageQueue) {
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(amazonSQSAsync.getQueueUrl(sendMessageQueue.getQueueName()).getQueueUrl())
                .withMessageBody(sendMessageQueue.getMsg())
                .withDelaySeconds(5);
        amazonSQSAsync.sendMessage(send_msg_request);
        return sendMessageQueue.getMsg() + " - Message has been sent successfully";
    }

    @Override
    public String receiveMessagesWithoutDelete(QueueInfo queueInfo) {
        List<Message> receivedMessages = getMessages(queueInfo);
        String messages = "";
        for (Message receivedMessage : receivedMessages) {
            messages += receivedMessage.getBody() + "\n";
        }
        return messages;
    }

    @Override
    public String receiveMessagesWithDelete(QueueInfo queueInfo) {
        List<Message> receivedMessages = getMessagesForDelete(queueInfo);
        for (Message m : receivedMessages) {
            amazonSQSAsync.deleteMessage(amazonSQSAsync.getQueueUrl(queueInfo.getQueueName()).getQueueUrl(), m.getReceiptHandle());
        }
        return "Msg has been deleted successfully.";
    }

    @Override
    public String sendMultipleMessages(SendMultipleMessageRequest sendMultipleMessageRequest) {
        List<SendMessageBatchRequestEntry> sendMessageBatchRequestEntries = new ArrayList<>();
        for (MessageInfo message : sendMultipleMessageRequest.getMessageInfos()) {
            SendMessageBatchRequestEntry sendMessageBatchRequestEntry = new SendMessageBatchRequestEntry(message.getId(), message.getMessageBody());
            sendMessageBatchRequestEntries.add(sendMessageBatchRequestEntry);
        }
        SendMessageBatchRequest send_batch_request = new SendMessageBatchRequest()
                .withQueueUrl(amazonSQSAsync.getQueueUrl(sendMultipleMessageRequest.getQueueName()).getQueueUrl())
                .withEntries(sendMessageBatchRequestEntries);
        amazonSQSAsync.sendMessageBatch(send_batch_request);
        return "Send Multiple Receive successfully";
    }

    @Override
    public List<Message> receiveMultipleMessages(QueueInfo queueInfo) {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(amazonSQSAsync.getQueueUrl(queueInfo.getQueueName()).getQueueUrl());
        receiveMessageRequest.setMaxNumberOfMessages(10);
        receiveMessageRequest.setWaitTimeSeconds(20);
        return amazonSQSAsync.receiveMessage(receiveMessageRequest).getMessages();
    }

    @Override
    public String deleteQueue(QueueInfo queueInfo) {
        DeleteQueueRequest deleteQueueRequest = new DeleteQueueRequest()
                .withQueueUrl(amazonSQSAsync.getQueueUrl(queueInfo.getQueueName()).getQueueUrl());
        return queueInfo.getQueueName() + " - has been deleted successfully";
    }

    ////////////////// private  methods ////////////////////////////////////////////////////

    private List<Message> getMessages(QueueInfo queueInfo) {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                .withQueueUrl(amazonSQSAsync.getQueueUrl(queueInfo.getQueueName()).getQueueUrl())
                .withVisibilityTimeout(2)
                .withWaitTimeSeconds(1);
        List<Message> receivedMessages = amazonSQSAsync.receiveMessage(receiveMessageRequest).getMessages();
        return receivedMessages;
    }

    private List<Message> getMessagesForDelete(QueueInfo queueInfo) {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                .withQueueUrl(amazonSQSAsync.getQueueUrl(queueInfo.getQueueName()).getQueueUrl());
        List<Message> receivedMessages = amazonSQSAsync.receiveMessage(receiveMessageRequest).getMessages();
        return receivedMessages;
    }
}
