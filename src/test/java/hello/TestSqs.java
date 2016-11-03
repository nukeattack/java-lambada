package hello;

import org.junit.Test;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

public class TestSqs {
  @Test
  public void testSqs(){
    AmazonSQSClient sqs = new AmazonSQSClient().withRegion(Region.getRegion(Regions.fromName("us-west-2")));
    ReceiveMessageRequest request = new ReceiveMessageRequest();
    request.setQueueUrl("https://sqs.us-west-2.amazonaws.com/451578979479/data-projection-queue-dev");
    request.setMaxNumberOfMessages(1);
    ReceiveMessageResult result = sqs.receiveMessage(request);
    Message mess = result.getMessages().get(0);
    System.out.println(mess.getAttributes());
    System.out.println("*******************************");
    System.out.println(mess.getBody());
    
    
  }
}
