#!/bin/bash

BAC=$1
python2.4 preplot.py $BAC
scp -i /home/research/tanglab-runner/tanglab-rsa $BAC.adjlist tanglab-runner@cloud.seas.wustl.edu:/home/research/tanglab-runner/to_plot/
ssh -i /home/research/tanglab-runner/tanglab-rsa tanglab-runner@cloud.seas.wustl.edu "cd script; qsub -v JOB=${BAC} plotjob.sh"


#scp -i /home/research/tanglab-runner/tanglab-rsa det.adjlist tanglab-runner@cloud.seas.wustl.edu:/home/research/tanglab-runner/to_plot/
#ssh -i /home/research/tanglab-runner/tanglab-rsa tanglab-runner@cloud.seas.wustl.edu "cd script; qsub -v JOB=det plotjob.sh"
