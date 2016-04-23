:  > ssh_commands.txt
:  > scp_commands.txt
:  > run_commands.txt
:  > private_ips.txt
:  > check_ssh.txt
while read p;
do
	address=`aws ec2 describe-instances --instance-id $p | grep 'PublicDnsName' | head -n 1 | cut -d ':' -f2 | sed 's/\"//g' | sed 's/ //g' | sed 's/,//g'`

	privateIp=`aws ec2 describe-instances --instance-id $p | grep "PrivateIpAddress" | head -n 1 | cut -d ":" -f2 | sed 's/"//g' | sed 's/,//g' | sed 's/ //g'`

	echo "ssh -i $1 ec2-user@$address" >> ssh_commands.txt

	echo "ssh -i $1 ec2-user@$address %" >> run_commands.txt

	echo "ssh -q -i $1 ec2-user@$address exit; exit $?" >> check_ssh.txt

	echo $privateIp >> private_ips.txt

	echo "scp -i $1 % ec2-user@$address:~/" >> scp_commands.txt
done < instances.txt
