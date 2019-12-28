package main.jms.listeners;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class TopicListener implements MessageListener {

  private String consumerName;

  public TopicListener(String consumerName) {
    this.consumerName = consumerName;
  }

  @Override
  public void onMessage(Message message) {
    try {
      TextMessage msg = (TextMessage) message;
      System.out.println("Message received :" + msg.getText() + " by consumer :" + consumerName);
    } catch (JMSException e) {
      e.printStackTrace();
    }
  }

}
