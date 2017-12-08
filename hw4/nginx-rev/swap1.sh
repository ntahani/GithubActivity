#/bin/bash

#same as init.sh, but swap from web2 to web1
cd /etc/nginx

sed -e s?web2:8080/activity2/?web1:8080/activity/? <nginx.conf > /tmp/xxx

cp /tmp/xxx nginx.conf

service nginx reload
