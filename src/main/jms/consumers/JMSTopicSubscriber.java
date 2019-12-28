package main.jms.consumers;

import java.util.Hashtable;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * The topic subscriber will be executed before the publisher published the messages.
 * Since the subscriber has to be present while publisher send the messages.
 * Normal subscriber won't receive the messages which has been send by the publisher previously when the subscriber came back online  
 * @author gbs04543
 *
 */
public class JMSTopicSubscriber {

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
      MessageConsumer consumer = session.createConsumer(topic);
      TextMessage msg = (TextMessage) consumer.receive();
      System.out.println("Message subscriber received the message :" + msg.getText());
    } finally {
      if (conn != null) {
        conn.close();
      }
    }
  }
}
