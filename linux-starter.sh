#!/bin/bash

# Color variables
red='\033[0;31m'
green='\033[0;32m'
yellow='\033[0;33m'
blue='\033[0;34m'
magenta='\033[0;35m'
cyan='\033[0;36m'
# Clear the color after that
clear='\033[0m'

echo -e "${red}Starting all required servers${clear}"
npm start --prefix _backendForFrontend/ &
BFF_SERVER_PID=$!
mvn -f "communityServer/pom.xml" spring-boot:run &
COMMUNITY_SERVER_PID=$!
mvn -f "authServer/pom.xml" spring-boot:run &
AUTH_SERVER_PID=$!
mvn -f "productAndServiceServer/pom.xml" spring-boot:run &
PnD_SERVER_PID=$!
echo -e "${red}!!! - Press Enter stop all servers safely${clear}"
read
kill $BFF_SERVER_PID
kill $COMMUNITY_SERVER_PID
kill $AUTH_SERVER_PID
kill $PnD_SERVER_PID
clear
exit
