package org.apache.hwx.ben.syslogkafka;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.apache.log4j.Logger;
import org.productivity.java.syslog4j.server.SyslogServerEventIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;
import org.productivity.java.syslog4j.server.SyslogServerSessionlessEventHandlerIF;

public class KafkaHandler implements SyslogServerSessionlessEventHandlerIF {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(KafkaHandler.class);
    private static final String KAFKA_PROPERTIES = "/prod.conf";

    private Producer<String, String> producer;

    public KafkaHandler() {
        Properties kafkaProps = null;

        try {
        	kafkaProps = this.getBaseKafkaProperties();
        } catch (IOException e) {
            LOG.error(String.format("Couldn't read property file" +  KAFKA_PROPERTIES));
            LOG.error(e);
            System.exit(1);
        }

        this.producer = new Producer<String, String>(new ProducerConfig(kafkaProps));
    }
    


    public void event( SyslogServerIF server, SocketAddress socketAddress, SyslogServerEventIF event) {
        try {
            KeyedMessage<String, String> kafkaMessage =
                    new KeyedMessage<String, String>("syslog",event.getMessage());
            producer.send(kafkaMessage);

        } catch( Exception ex ) {
            LOG.error(ex.toString());
            ex.printStackTrace();
        }
    }

    public void shutdown() 
    {
        if (producer != null) 
        {
            LOG.info("Shutting down kafka producer.");
            producer.close();
        }
    }

    

	@Override
	public void destroy(SyslogServerIF arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(SyslogServerIF arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exception(SyslogServerIF syslogServer,
			SocketAddress socketAddress, Exception exception) {
		// TODO Auto-generated method stub
		
	}
	
	protected Properties getBaseKafkaProperties() throws IOException {
        Properties properties = new Properties();
        URL propertyUrl = Syslog4JKafkaServer.class.getResource(KAFKA_PROPERTIES);
        if (propertyUrl == null) {
            throw new IllegalArgumentException("Could not find properties file: " + KAFKA_PROPERTIES);
        }
        InputStream in = propertyUrl.openStream();
        try {
        	properties.load(in);
        }
        finally {
            in.close();
        }
        return properties;
    }
}