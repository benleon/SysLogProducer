# SysLogProducer

Very Simple KafkaProducer for syslog4j.

The program that can push through 50000 messages a second on a single laptop hosting everything

My test configuration on log generating server

Add to rsyslog.conf:

$ActionQueueType LinkedList # use asynchronous processing
$ActionQueueFileName srvrfwd1 # set file name, also enables disk mode
$ActionResumeRetryCount -1 # infinite retries on insert failure
$ActionQueueSaveOnShutdown on # save in-memory data if rsyslog shuts down
local3.* @@<SyslogServerIP>:45553

To test run the following python script which will create dummy logs:

resources/loggerTest.py

You can then see the logs in the kafka consumer:

bin/kafka-console-consumer.sh --zookeeper sandbox.hortonworks.com:2181 --topic syslog
