#!/bin/sh
 
 lasttime=`cat lasttime`
 currenttime=`stat -c %Y blog-1.0-SNAPSHOT.jar`

 echo $lasttime $currenttime
 
 if [ "$lasttime" != "$currenttime" ]
   then 
     echo "New deployment found ..."
	 echo $currenttime > lasttime
	 echo "Start deployment at `date`"
 fi
 
 

