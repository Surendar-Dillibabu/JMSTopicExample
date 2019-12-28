package main.jms.producers;

import java.util.Hashtable;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSTopicPublisher {

  public static void main(String[] args) throws JMSException, NamingException {
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
      MessageProducer producer = session.createProducer(topic);
      TextMessage msg = session.createTextMessage("Hello world !!");
      producer.send(msg);
      System.out.println("Message successfully published to the topic");
    } finally {
      if (conn != null) {
        conn.close();
      }
    }
  }
}
