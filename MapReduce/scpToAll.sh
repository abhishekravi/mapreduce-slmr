last=`wc -l instances.txt | sed 's/instances.txt//g' | sed 's/ //g'`
for (( i=1; i <= $last; i++ ))
do
	sh scpTo.sh $i job.jar	
done
