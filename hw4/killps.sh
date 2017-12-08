#!/bin/bash

function killitif {
    docker ps -a  > /tmp/yy_xx$$
    if grep --quiet $1 /tmp/yy_xx$$
     then
     echo "killing $1"
     docker rm -f `docker ps -a | grep $1  | sed -e 's: .*$::'`
     /bin/rm /tmp/yy_xx$$
   fi
}

if [ $# -ne 1 ]; then
	echo "need one argument"
	exit
else
	killitif $1
	docker ps -a
fi


