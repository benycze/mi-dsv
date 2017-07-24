#!/bin/bash
echo "Čistím soubory ORB daemona"
rm -rf orb.db/

orbd -ORBDInitialHost localhost -ORBDInitialPort 12345
