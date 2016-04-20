serverIp=`head -n 1 private_ips.txt`
last=`wc -l instances.txt | sed 's/instances.txt//g' | sed 's/ //g'`

for (( i=2; i <= $last; i++ ))
do
	nohup sh runCommandOn.sh $i "java -cp job.jar neu.mr.main.MapReduceClient $serverIp" &
done
