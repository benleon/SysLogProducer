#!/usr/bin/python
import syslog
import time

syslog.openlog('d', 1, syslog.LOG_LOCAL3)
for i in range(1000000):
    syslog.syslog('test'+str(i))