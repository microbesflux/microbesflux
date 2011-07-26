#!/bin/tcsh
#$ -cwd
setenv PATH /cluster/cloud/Ipopt-3.8.3/bin:$PATH
ampl $1 > out.txt
