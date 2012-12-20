#!/bin/bash

if [ $# != 3 ]; then
        echo "Usage $0 <user> <pass> <sword URL>"
	echo
	echo "e.g. $0 g.waller@ed.ac.uk redicAdmin http://redicdev.edina.ac.uk:8081/sword/servicedocument" 
        exit 1
fi

USER=$1
PASS=$2
URL=$3

curl -i --user "$USER:$PASS" $3 

