#!/bin/bash
BAC=$1
TYPE=$2
EMAIL=$3
F="NULL"
if [ $# -eq 4 ]
then 
	BAC=$1
	F=$2
	TYPE=$3
	EMAIL=$4
fi 

echo "Will submit a job " $BAC  $TYPE $EMAIL $F  >> /research-www/engineering/tanglab/flux/temp/log3.txt

echo "Will submit a job " $BAC  $TYPE $EMAIL $F 
wget "http://tanglab.engineering.wustl.edu/flux/task/add/?task=$BAC&type=$TYPE&email=$EMAIL&file=$F"

