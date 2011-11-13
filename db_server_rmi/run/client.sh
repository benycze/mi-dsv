#!/bin/bash
echo "starting client with batch file $1"

java -Djava.security.policy=./java.policy -jar client.jar localhost 2010 $1
