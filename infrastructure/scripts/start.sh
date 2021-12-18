#!/bin/bash

helpFunction()
{
   echo ""
   echo "Usage: $0 -p port -u username -e password"
   echo -e "\t-p Port for running the service"
   echo -e "\t-u Username of the authenticated user - mandatory"
   echo -e "\t-e Password of the authenticated user - mandatory"
   exit 1 # Exit script after printing help
}

while getopts "p:u:e:" opt
do
   case "$opt" in
      p ) port="$OPTARG" ;;
      u ) username="$OPTARG" ;;
      e ) password="$OPTARG" ;;
      ? ) helpFunction ;; # Print helpFunction in case parameter is non-existent
   esac
done

# Print helpFunction in case parameters are empty
if [ -z "$username" ] || [ -z "$password" ]
then
   echo "username and password is mandatory";
   helpFunction
fi

# Begin script in case all parameters are correct


DEFAULT_PORT=8080
PORT="${port:-$DEFAULT_PORT}"

echo "\n\n *** Building Service ...\n\n"

docker build -t medication-service -f infrastructure/docker/Dockerfile .

echo "\n\n *** Running Service on $PORT. user: $username, password: $password \n\n"

docker run -e "USER_NAME=${username}" -e "PASSWORD=${password}" -p $PORT:8080 -td medication-service

echo "\n\n *** Medication Service started on port $PORT \n\n ***"
