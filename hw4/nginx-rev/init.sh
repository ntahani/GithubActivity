#/bin/bash
# This shell is inside the nginx docker, and used to start off operations. 
# IT's fired off by the dorun.sh to start things
cd /etc/nginx
# rename www.cs.ucdavis.edu to web1:8080/activity in nginx,conf file and write the result to /tmp/xxx file
sed -e s?www.cs.ucdavis.edu?web1:8080/activity/? <nginx.conf > /tmp/xxx

#copy the /tmp/xxx as new nginx.conf file
cp /tmp/xxx nginx.conf

# reload nginx service
service nginx reload 
