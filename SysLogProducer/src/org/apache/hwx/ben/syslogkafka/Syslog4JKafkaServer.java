package org.apache.hwx.ben.syslogkafka;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.productivity.java.syslog4j.server.SyslogServer;
import org.productivity.java.syslog4j.server.SyslogServerConfigIF;
import org.productivity.java.syslog4j.server.SyslogServerIF;
import org.productivity.java.syslog4j.server.impl.net.tcp.TCPNetSyslogServerConfig;
import org.productivity.java.syslog4j.server.impl.net.udp.UDPNetSyslogServerConfig;


public class Syslog4JKafkaServer {
    private static final Logger LOG = Logger.getLogger(Syslog4JKafkaServer.class);
    private static KafkaHandler kafkaEventHandler;

    public static void main(String [] args) throws IOException {
    	
    	final TCPNetSyslogServerConfig tcpNetSyslogServerConfig=new TCPNetSyslogServerConfig(45553);
    	final UDPNetSyslogServerConfig udpNetSyslogServerConfig=new UDPNetSyslogServerConfig(45553);
        final SyslogServerIF udpSyslogServer = SyslogServer.createThreadedInstance("udp45553", udpNetSyslogServerConfig);
        final SyslogServerIF tcpSyslogServer = SyslogServer.createThreadedInstance("tcp45553", tcpNetSyslogServerConfig);

        SyslogServerConfigIF udpSyslogServerConfig = udpSyslogServer.getConfig();
        SyslogServerConfigIF tcpSyslogServerConfig = tcpSyslogServer.getConfig();

        kafkaEventHandler = new KafkaHandler();
      

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                kafkaEventHandler.shutdown();
                LOG.info("Shutting down");
                SyslogServer.shutdown();
            }
        });

        udpSyslogServerConfig.addEventHandler(kafkaEventHandler);

        tcpSyslogServerConfig.addEventHandler(kafkaEventHandler);

        try {
            udpSyslogServer.getThread().join();
            tcpSyslogServer.getThread().join();
        } catch (InterruptedException e) {
            LOG.error("Main thread stopped");
        }
    }
}