#!/bin/bash -x
#java -Djava.security.policy=java.policy -Djava.rmi.server.logCalls=true -Djava.rmi.server.hostname=$NODE_IP -cp dist/ADUV.jar node/NetworkNode $DIR_IP $DIR_PORT $NODE_IP $NODE_PORT
java -Djava.security.policy=java.policy -Djava.rmi.server.hostname=$NODE_IP -cp dist/ADUV.jar node/NetworkNode $DIR_IP $DIR_PORT $NODE_IP $NODE_PORT
