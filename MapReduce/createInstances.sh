aws ec2 run-instances --image-id ami-c229c0a2 --count $1 --instance-type m3.xlarge --key-name testingec2 --security-groups launch-wizard-1 | grep "InstanceId" | cut -d ":" -f2 | sed 's/"//g' | sed 's/,//g' | sed 's/ //g' > instances.txt
