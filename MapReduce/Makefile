BUCKET=pdmrbucket
CLUSTER_SIZE=4
USER_JOB_PARAMS='parameters here, 1, 2, 3'

build:
	mvn clean package
	sh copyRunnableJar.sh

setupCloud:
	sh createInstances.sh $(CLUSTER_SIZE)
	sh generateCommands.sh
	sh waitForCluster.sh
	sh scpToAll.sh

run:
	sh startServer.sh $(USER_JOB_PARAMS)
	sleep 5;
	sh startClients.sh

terminate:
	sh terminateAll.sh
	rm -fr *.txt
	rm -fr nohup.out
	rm -fr job.jar
	rm -fr target

