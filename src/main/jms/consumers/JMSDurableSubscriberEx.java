package main.jms.consumers;

import java.util.Hashtable;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import main.jms.listeners.TopicListener;

public class JMSDurableSubscriberEx {
  public static void main(String[] args) throws JMSException, NamingException, InterruptedException {
    Connection conn = null;
    try {
      Hashtable<String, String> ht = new Hashtable<>();
      ht.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
      ht.put(Context.PROVIDER_URL, "t3://localhost:7001");
      Context context = new InitialContext(ht);
      ConnectionFactory connFactory = (ConnectionFactory) context.lookup("jms/TestConnectionFactory");
      conn = connFactory.createConnection();
      // This client ID is used to set the client and receive only the messages which
      // are published to this client.
      // If we don't pass the client id we will get below error
      // Exception in thread "main" weblogic.jms.common.JMSException: Connection
      // clientID is null
      conn.setClientID("TS1");
      Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
      Topic topic = (Topic) context.lookup("jms/TestTopic");
      conn.start();
      TopicSubscriber subscriber1 = session.createDurableSubscriber(topic, "subscriber-1");
      subscriber1.setMessageListener(new TopicListener("subscriber-1"));

      TopicSubscriber subscriber2 = session.createDurableSubscriber(topic, "subscriber-2");
      subscriber2.setMessageListener(new TopicListener("subscriber-2"));

      TopicSubscriber subscriber3 = session.createDurableSubscriber(topic, "subscriber-3");
      subscriber3.setMessageListener(new TopicListener("subscriber-3"));
      System.out.println("Message listener attached to the publisher to listen to the messages");
      // Making the main thread to sleep for 5 seconds for making the listeners to
      // receive the messages before closing
      Thread.sleep(5000);
      // This below lines are used to unsubscribe the receiver. Before calling
      // unsubscribe method with the subscriber name we have to close the subscriber.
      // Otherwise it will exception. Once unsunscribed the JMSProvider no longer
      // stores the messages for this subscriber which needs to be delivered when the
      // client comes online
      // subscriber1.close();
      // session.unsubscribe("subscriber-1");
    } finally {
      if (conn != null) {
        conn.close();
      }
    }
  }
}
