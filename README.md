# SysLogProducer

Very Simple KafkaProducer for syslog4j. Simple Java program that can push through 50000 messages a second even on a single Laptop hosting everything

My test configuration on log generating server

$ActionQueueType LinkedList # use asynchronous processing
$ActionQueueFileName srvrfwd1 # set file name, also enables disk mode
$ActionResumeRetryCount -1 # infinite retries on insert failure
$ActionQueueSaveOnShutdown on # save in-memory data if rsyslog shuts down
local3.* @@<SyslogServerIP>:45553

To test run the 

resources/loggerDemo.py file on the log creation server
