last=`wc -l instances.txt | sed 's/instances.txt//g' | sed 's/ //g'`

for (( i=1; i <= $last; i++ ))
do
	isUp=false;
	returnVal=255;
	while [[ $isUp != true ]];
	do
		if [[ $returnVal != 0 ]]; then
			`sh checkSshTo.sh $1`
			returnVal=$?;
			sleep 5s;
		else 
			isUp=true;
		fi
	done
done

