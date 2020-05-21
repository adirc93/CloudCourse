#!/usr/bin/bash
#

VM_2_address=132.72.105.240
A=9876
B=20
C=5 

ssh root@$VM_2_address "docker run -d -p $VM_2_address:$A:5432 --name 204484109_318199601 reutdockerbgu/ex2-docker" &
sleep 3s
iperf3 -c $VM_2_address -p $A -t $B -i $C --forceflush | tee output.txt
ssh root@$VM_2_address "docker stop $(docker ps -q)"
