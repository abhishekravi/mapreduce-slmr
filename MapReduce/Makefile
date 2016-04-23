BUCKET=pdmrbucket
CLUSTER_SIZE=11
USER_MAIN_CLASS=neu.mr.a3.A3
USER_JOB_PARAMS=''
INSTANCE_TYPE=t2.micro

build:
	mvn clean package
	sh copyRunnableJar.sh

setupCloud:
	sh createInstances.sh $(CLUSTER_SIZE) $(INSTANCE_TYPE)
	sh generateCommands.sh
	sh waitForCluster.sh
	sh scpToAll.sh

run:
	sh startServer.sh $(USER_MAIN_CLASS) $(USER_JOB_PARAMS)
	sleep 5;
	sh startClients.sh

terminate:
	sh terminateAll.sh
	rm -fr *.txt
	rm -fr nohup.out
	rm -fr job.jar
	rm -fr target

