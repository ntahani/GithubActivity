#!/bin/bash

function killitif {
    docker ps -a  > /tmp/yy_xx$$
    if grep --quiet $1 /tmp/yy_xx$$
     then
     echo "killing older version of $1"
     docker rm -f `docker ps -a | grep $1  | sed -e 's: .*$::'`
   fi
   /bin/rm /tmp/yy_xx$$
}

p=`docker ps -a | grep $1`
if [ ${#p} -gt 0 ]; then
    echo "$1 is already running. Exiting..."
    docker ps -a | grep $1
    exit
fi

new_image_name=""
current_app=""

if [ "$1" == "web1" ]; then
    #change web2 to web1
    new_image_name="activity";
    current_app="web2";
    echo "swapping web2 to web1"
else
    #change web1 to web2
    new_image_name="activity2";
    current_app="web1";
    echo "swapping web1 to web2"
fi

# remove the ps if is running
killitif $1

# --network sets the network mode to ecs161_default
# --name assigns $1 name to the container
# wait 5 secocds and run the new image
sleep 5 && docker run --network ecs161_default --name=$1 $new_image_name &

# do the swap within container
if [ "$1" == "web1" ]; then
    #change web2 to web1
    echo "in 5 seconds do the swap web2 to web1"
    sleep 5 && docker exec ecs161_proxy_1 /bin/bash /bin/swap1.sh
else
    #change web1 to web2
    echo "in 5 seconds do the swap web1 to web2"
    sleep 5 && docker exec ecs161_proxy_1 /bin/bash /bin/swap2.sh
fi

killitif $current_app

