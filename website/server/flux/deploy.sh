#!/bin/bash
tar -cvf flux.tar *
scp flux.tar xuy@ssh.seas.wustl.edu:/research-www/engineering/tanglab/flux/
