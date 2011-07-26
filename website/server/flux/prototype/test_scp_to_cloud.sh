#!/bin/bash

# echo "Will submit a job " $1  >> /research-www/engineering/tanglab/flux/temp/log.txt
BAC=rak1.ampl


echo "Write a log line to  /research-www/engineering/tanglab/flux/temp/log2.txt "
echo "" >> /research-www/engineering/tanglab/flux/temp/log2.txt
echo `date` >> /research-www/engineering/tanglab/flux/temp/log2.txt
echo "Will submit a job " $BAC  >> /research-www/engineering/tanglab/flux/temp/log2.txt

echo "Using scp to copy a file to cloud "
echo "Copy job " $BAC  " to  cloud using scp." >> /research-www/engineering/tanglab/flux/temp/log2.txt
scp -o StrictHostKeyChecking=no -i /research-www/engineering/tanglab/flux/xuy-seas /research-www/engineering/tanglab/flux/temp/$BAC xuy@cloud.seas.wustl.edu:/home/research/xuy/ampl_to_run

echo "Submit the job using qsub"
ssh -i /research-www/engineering/tanglab/flux/xuy-seas xuy@cloud.seas.wustl.edu "cd script; qsub -v JOB=${BAC} ampljob.sh"

