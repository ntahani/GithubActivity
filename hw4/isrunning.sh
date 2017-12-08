#!/bin/bash
if [ $# -ne 1 ]; then
    echo "Usage: $0 process_name"
    exit
fi

p=`docker ps -a | grep $1`
if [ ${#p} -gt 0 ]; then
	echo $p
else
	echo "$1 is not running"
fi
