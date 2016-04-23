# SLMR - Something Like MapReduce
Our very own implementation of the MapReduce framework

## Requirements

- JDK 7 or above.
- Maven compiler to build the project.
- Linux/Mac operating system to execute Make commands or some program to execute the make commands.
- Credentials for Amazon S3. To be configured in the property file.
- Amazon CLI, as our scripts make use of them to provision the cluster.
- EC2 key for the region configured on your aws command line. Also the key needs to be downloaded in the project folder and it's name needs to be added to the make file.
- A security group associated with the EC2 machines with allowance of inbound/outbound traffic from everywhere. Name of the security group to be configured in the make file.

## Steps to build and run the project

- Configure all the required parameters in the make file
- Download and put the EC2 key in the project folder at the same level as the src folder
- Make sure you have maven and java variables setup in your environment
- Also make sure you have the AWS CLI setup with proper key and Id on your machine. If not use "aws configure" to set it up.
- Once everything is done execute "make build" to build the project and generate the runnable jar file.
- After it's built execute "make setupCloud" to provision a cluster of the desired number of machines on Amazon. This step will also generate the required commands for ssh-ing and scp-ing into the newly provisioned machines and upload the job jar to all of them
- Once the cloud is setup execute "make run" to start the server on one of the machine and clients on the rest. This will start the program execution, the logs for which can be seen by ssh-ing into the server machine by running "sh sshTo.sh 1". The logs are stored in a file called mrlogs.log. The same goes for client machines where you can ssh using "sh sshTo.sh 2" (Same for 3, 4, 5 and so on).
- After the job has finished it's execution you should see a "_SUCCESS" file in your output folder on S3 and this marks the completion of the job
- You can now execute "make terminate" and this'll clean up your project folder and terminate your cluster.