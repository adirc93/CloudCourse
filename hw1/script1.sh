#!/usr/bin/bash
#ex1 
#Reut Nezer 318199601
#Adir Cohen 204484109

#Function for parsing YAML files
parse_yaml() {
   local prefix=$2
   local s='[[:space:]]*' w='[a-zA-Z0-9_]*' fs=$(echo @|tr @ '\034')
   sed -ne "s|^\($s\)\($w\)$s:$s\"\(.*\)\"$s\$|\1$fs\2$fs\3|p" \
        -e "s|^\($s\)\($w\)$s:$s\(.*\)$s\$|\1$fs\2$fs\3|p"  $1 |
   awk -F$fs '{
      indent = length($1)/2;
      vname[indent] = $2;
      for (i in vname) {if (i > indent) {delete vname[i]}}
      if (length($3) > 0) {
         vn=""; for (i=0; i<indent; i++) {vn=(vn)(vname[i])("_")}
         printf("%s%s%s=\"%s\"\n", "'$prefix'",vn, $2, $3);
      }
   }'
}

#use the parser on the config file
eval $(parse_yaml config.yaml)

#Start iperf server on VM_2, which listens on port A
ssh root@$VM_2_address "iperf3 -s -p $A -1 > /dev/null" &

#Sleep command allows to create a connection between the client and the server 
sleep 1s

#Start iperf client which sends packages for B seconds and prints the report every C seconds
iperf3 -c 132.72.105.220 -p $A -t $B -i $C --forceflush $protocol | tee $MY_FILE

