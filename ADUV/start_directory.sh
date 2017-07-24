#!/bin/bash -x
#java -Djava.security.policy=java.policy -Djava.rmi.server.logCalls=true -Djava.rmi.server.hostname=192.168.122.1 -cp dist/ADUV.jar directory/DirectoryServer
java -Djava.security.policy=java.policy -Djava.rmi.server.hostname=192.168.122.1 -cp dist/ADUV.jar directory/DirectoryServer
