package main.jms.consumers;

import java.util.Hashtable;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import main.jms.listeners.TopicListener;

public class JMSTopicMultipleSubscribers {

  public static void main(String[] args) throws JMSException, NamingException, InterruptedException {
    Connection conn = null;
    try {
      Hashtable<String, String> ht = new Hashtable<>();
      ht.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
      ht.put(Context.PROVIDER_URL, "t3://localhost:7001");
      Context context = new InitialContext(ht);
      ConnectionFactory connFactory = (ConnectionFactory) context.lookup("jms/TestConnectionFactory");
      conn = connFactory.createConnection();
      Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
      Topic topic = (Topic) context.lookup("jms/TestTopic");
      conn.start();
      MessageConsumer subscribe1 = session.createConsumer(topic);
      subscribe1.setMessageListener(new TopicListener("subscriber-1"));

      MessageConsumer subscribe2 = session.createConsumer(topic);
      subscribe2.setMessageListener(new TopicListener("subscriber-2"));

      MessageConsumer subscriber3 = session.createConsumer(topic);
      subscriber3.setMessageListener(new TopicListener("subscriber-3"));

      System.out.println("Message listener attached to the publisher to listen to the messages");
      // Making the main thread to sleep for 5 seconds for making the listeners to
      // receive the messages before closing
      Thread.sleep(5000);
    } finally {
      if (conn != null) {
        conn.close();
      }
    }
  }
}
