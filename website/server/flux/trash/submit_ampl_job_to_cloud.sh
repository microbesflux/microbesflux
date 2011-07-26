#!/bin/bash
BAC=$1
TYPE=$2
EMAIL=$3

echo "Will submit a job " $BAC  $TYPE $EMAIL  >> /research-www/engineering/tanglab/flux/temp/log2.txt

wget "http://tanglab.engineering.wustl.edu/flux/task/add/?task=$BAC&type=$TYPE&email=$EMAIL"

