#!/bin/bash

if [ $# != 4 ]; then
        echo "Usage $0 <user> <pass> <zip file to post> <sword URL>"
	echo
	echo "e.g. $0 g.waller@ed.ac.uk redicAdmin ../etc/sample_data/test_mets_package/test.zip http://redicdev.edina.ac.uk:8081/swordv2/collection/123456789/2" 
        exit 1
fi

USER=$1
PASS=$2
FILE=$3
URL=$4

curl -i --user "$USER:$PASS" --data-binary "@$FILE" -H "Packaging: http://purl.org/net/sword/package/METSDSpaceSIP" -H "In-Progress: false" -H 'Content-Disposition: attachment; filename="myswordfile.zip"' -H "Content-Type: application/zip" -H "X-No-Op: false" -H "X-Verbose: true" $4 

