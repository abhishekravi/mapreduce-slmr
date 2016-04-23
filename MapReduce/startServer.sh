echo $1
echo $2
nohup sh runCommandOn.sh 1 "java -cp job.jar $1 $2" &
