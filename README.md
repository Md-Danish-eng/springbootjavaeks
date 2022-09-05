# Complete CI/CD Pipeline with EKS and AWS ECR

## Technologies used

```Kubernetes, Jenkins, AWS EKS, AWS ECR, Java, Maven, Linux, Docker, Git```

## POC Requirements

1: Create a private AWS ECR Docker repository

2: Adjust Jenkinsfile to build and push Docker image to AWS ECR Integrate deploying to K8s cluster in the CI/CD pipeline from AWS ECR private registry.

```So the complete CI/CD project we build has the following configuration```

```
a.  step: Increment version
b.  step: Build artifact for Java Maven application
c.  step: Build and push Docker image to AWS ECR
d.  step: Deploy new application version to EKS cluster
e.  step: Commit the version update

```
## Start implementation

## Install EKS (Elastic Kubernetes Service) On ubuntu server:

```Note: There are so many methods to Install Eks. In my case i use command line```

First Go to ```AWS console``` and install an ```ubuntu 20.04``` server by follwoing methods:

### Select this ```Ubuntu Server 20.04 LTS (HVM), SSD Volume Type```

![image](https://user-images.githubusercontent.com/85988020/187845138-a4572857-6973-4b1e-b924-742992b24c66.png)

### Select the "t3.medium" and cick on  Next:configure insatnce Details

![image](https://user-images.githubusercontent.com/85988020/188131393-112e49e8-70b7-455b-96f9-b8d75c6aa47b.png)

### Keep everything default and click on ```add storage```

![image](https://user-images.githubusercontent.com/85988020/188131650-dec0809c-fdc3-415a-bad6-c168ec93bfc6.png)

### Keep everything default and click on ```Next Add Tags```

![image](https://user-images.githubusercontent.com/85988020/188131984-d27eb40d-9552-4d97-b1a4-b9f8538099a4.png)

 ### Here you can add the tags of your instance.

![image](https://user-images.githubusercontent.com/85988020/187847261-a895ba3b-b1ca-46d8-8289-3d4be5cfd66d.png)

### Here you can add security rules, in my case i choose ```All traffic rules allow``` after that click on ```Review and Launch```

![image](https://user-images.githubusercontent.com/85988020/187847881-646d0f2c-87f6-4a7f-9f4c-9c0d6cd58ba0.png)

### From here you can review your instance and click on launch

![image](https://user-images.githubusercontent.com/85988020/187848296-fd1ff808-168a-4b17-bbed-ecdf55430ca9.png)

### Here Choose the existing key or create a new key and click on launch instance.

![image](https://user-images.githubusercontent.com/85988020/188130515-8d726a9d-5ca6-4e05-90c3-9988cb806d8e.png)

### Now check the AWS console your instance is showing here.

![image](https://user-images.githubusercontent.com/85988020/188130787-da310a5f-66d7-457c-990d-53706746d1ad.png)

### Do SSH on server and install  ```EKS``` by following command:

 ```curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp```

```sudo mv /tmp/eksctl /usr/local/bin```

```eksctl version```  You can check the version of eksctl 

## Install kubectl by run the following command:

```curl -o kubectl https://amazon-eks.s3.us-west-2.amazonaws.com/1.21.2/2021-07-05/bin/linux/amd64/kubectl```

```chmod +x ./kubectl```

``mkdir -p $HOME/bin && cp ./kubectl $HOME/bin/kubectl && export PATH=$PATH:$HOME/bin```

```echo 'export PATH=$PATH:$HOME/bin' >> ~/.bashrc```

```kubectl version --short --client```  you can check version of kubectl 

## After run the below command wait for 15-20 minutes for cluster creation.

```eksctl create cluster --name java-poc --region ap-south-1 --version 1.21 --nodegroup-name linux-nodes --node-type t2.medium --nodes 2```

## After see cluster has been successfully created check by running these commands:

![image](https://user-images.githubusercontent.com/85988020/188311972-496b2734-e00b-496a-a4ca-2204043117bd.png)

## Go and check the server on AWS console

![image](https://user-images.githubusercontent.com/85988020/188312762-affce0e4-d858-4e25-8e29-082d012934a1.png)

### Go to AWS console and create an IAM user and store the ```ACCESS_KEY_ID``` & ```SECRET_ACCESS_KEY_ID``` into the ```jenkins credentials``` ( It require to push the image into ECR ) Create an IAM role with below permission and attach this IAM role to the instance in which jenkins had installed so that it login to the ECR repo. 

### IAM USER

![image](https://user-images.githubusercontent.com/85988020/188314957-354e706d-821b-4d51-97dd-ed74941909b6.png)

### IAM role

![image](https://user-images.githubusercontent.com/85988020/188314989-08b14b1f-d857-497d-9953-329c345dc85b.png)

## Install the other required things also.

### Install awscli by run the following commands

```curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"```
```unzip awscliv2.zip```
```sudo apt install unzip```
```unzip awscliv2.zip```
```sudo ./aws/install```

### Install docker by run the following commands:

```sudo apt update```
```sudo apt install apt-transport-https ca-certificates curl software-properties-common```
```curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -```
```sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"```
```sudo apt install docker-ce```
```sudo systemctl status docker```

### Install maven by run the following commands:

```sudo apt update```
```sudo apt-get install maven -y```
```mvn --version```

### Install Jenkins by run the following commands:

```sudo apt update```
```sudo apt install openjdk-11-jdk```
```wget -p -O - https://pkg.jenkins.io/debian/jenkins.io.key | sudo apt-key add -```
```sudo sh -c 'echo deb http://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'```
```sudo apt install jenkins```
```sudo systemctl start jenkins```
```sudo systemctl status jenkins```

### Go to the console and copy the public ip and hit in the browser with port 8080, it will ask for password

### Go to the server and run the command for password ```sudo cat /var/lib/jenkins/secrets/initialAdminPassword```

### copy and paste the password in the jenkins browser and click on continue

![image](https://user-images.githubusercontent.com/85988020/188312232-13f1e561-754b-41f8-807a-7b049f382448.png)

### The next screen presents the option of installing suggested plugins or selecting specific plugins:

![image](https://user-images.githubusercontent.com/85988020/188312275-47ea7c7c-a7b6-4136-b0ee-55724007a3a4.png)

### We’ll click the Install suggested plugins option, which will immediately begin the installation process.

![image](https://user-images.githubusercontent.com/85988020/188312290-b81e34b4-9207-4c0a-80d4-ea2ee5267e67.png)

### Enter the name and password for your user:

![image](https://user-images.githubusercontent.com/85988020/188312337-60b289c6-47f8-4775-93f1-34e8600b92a4.png)

### You’ll receive an Instance Configuration page that will ask you to confirm the preferred URL for your Jenkins instance. Confirm either the domain name for your server or your server’s IP address:

![image](https://user-images.githubusercontent.com/85988020/188312376-02aa98d0-86fc-40e0-b928-3223ccf67fd3.png)

### After confirming the appropriate information, click Save and Finish. You’ll receive a confirmation page confirming that “Jenkins is Ready!”:

![image](https://user-images.githubusercontent.com/85988020/188312394-215efe00-b8b9-46d4-b76d-5d2e45d75324.png)

### Click Start using Jenkins to visit the main Jenkins dashboard:

![image](https://user-images.githubusercontent.com/85988020/188312455-aa62c09b-b539-412e-a90b-04613f691d43.png)

## Now,Create a private ECR (Elastic Container Registry)

![image](https://user-images.githubusercontent.com/85988020/188152096-794a4ec1-12d2-443a-81ba-0b7cd1988e94.png)

### After this it will look like this

![image](https://user-images.githubusercontent.com/85988020/188152501-201bcf2f-94b9-4e60-bdfe-596095b6beae.png)

### Go to the chrome browser and hit the public ip with port 8080 and type the username and Password for login

![image](https://user-images.githubusercontent.com/85988020/188309686-51ba7be9-4c60-41d9-a79c-1f1fd290675c.png)

### Go to jenkins Dashboard and click on Manage Jenkins

![image](https://user-images.githubusercontent.com/85988020/188309778-c6b7857f-34ee-4c54-8409-63d48726fab2.png)

### Then click on Manage plungins

![image](https://user-images.githubusercontent.com/85988020/188309796-d7cfd17c-2296-4899-bd22-6fc4dabd07ac.png)

### In the Available section search the following plugins  ```ECR``` ```maven``` ```kubernetes continuous deployment```

![image](https://user-images.githubusercontent.com/85988020/188309823-1a822c76-ef14-457f-935c-f17970648f8f.png)

### After select the plugins click on Install and restart 

![image](https://user-images.githubusercontent.com/85988020/188309896-f33f53ef-e7ae-4cc9-b6c7-4a6b5c698593.png)

### Note: For ```kubernetes continuous deployment``` click on Advance option and scroll down ( If you use this plugins from jenkins dashboard which is ```2.3.2``` version, it occurs error while deployment so, it's recommended to use downgrade version which is ```1.0.0 ``` )

```I upload the downgrade plugin into my github repo. Feel free to use```

![image](https://user-images.githubusercontent.com/85988020/188309959-97c8cab5-a884-4264-b780-fae36d3feeb2.png)

### From here we can install custom plugin by click choose file and after that click on deploy. The plugins will be installed.

![image](https://user-images.githubusercontent.com/85988020/188310006-5f15a45e-f8e1-4ab4-b4f5-b13532fd124b.png)

### Go back to the jenkins dashboard and manage jenkins 

![image](https://user-images.githubusercontent.com/85988020/188310101-745077c1-af12-4daa-8cf3-28a3bf0464f2.png)

### Click on global tool configuration 

![image](https://user-images.githubusercontent.com/85988020/188310112-2d739633-2e1c-423f-88e3-a0eac7796692.png)

### In the Maven installation add the maven installation path or we can also go with installed automatically.

![image](https://user-images.githubusercontent.com/85988020/188310169-9551525b-efe1-4c8b-a7e1-765149718909.png)

```Click on save and apply```

![image](https://user-images.githubusercontent.com/85988020/188310220-8d260dc0-3ffb-4a10-a9b7-501aa6478912.png)

### Go back to jenkins Dashboard and click on manage jenkins

![image](https://user-images.githubusercontent.com/85988020/188310245-825114e1-8551-433d-ab92-d639330813fc.png)

### After that click on Manage credentails

![image](https://user-images.githubusercontent.com/85988020/188310278-cd584a0d-07e7-4462-87ef-e69ff019706d.png)

### After click on Add credentials

![image](https://user-images.githubusercontent.com/85988020/188310372-282e8f96-0440-4995-84c2-9034abce4f35.png)

### Fill the following details and click on save

![image](https://user-images.githubusercontent.com/85988020/188310407-1e5675bb-b942-434c-b6cd-9bb87ae6b555.png)

### The same process for kubernetes intergration ``` Do SSH on the server in which EKS master had installed and copy the entire content of the following file:

```sudo cat ~/.kube/config```

![image](https://user-images.githubusercontent.com/85988020/188310482-0c0c88e4-cc79-483d-9f33-0ab3784ba95a.png)

### Click on new item 

![image](https://user-images.githubusercontent.com/85988020/188310569-3b0e26f5-bfcb-4c48-b5e7-ddbf2c13e3c7.png)

### Give a new to your project and click on pipeline

![image](https://user-images.githubusercontent.com/85988020/188310592-c67de7e5-c425-471b-b5c0-72a082f08dbf.png)

### In the Build Triggers click on GitHub hook trigger for GITScm polling

![image](https://user-images.githubusercontent.com/85988020/188310660-fb96ffab-e8bf-4745-b26d-04a68b5ec399.png)

### In the pipeline section choose the pipeline script and add the following lines.

```
pipeline {
  environment {
    registry = '646094415288.dkr.ecr.ap-south-1.amazonaws.com/java_poc'
    registryCredential = 'AWS_POC'
    dockerImage = '646094415288.dkr.ecr.ap-south-1.amazonaws.com/java_poc'
  }
    agent any    
    stages {
        stage('clone code') {
            steps {
               checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'github-creds', url: 'https://github.com/Md-Danish-eng/springbootjavaeks.git']]])
            }
        }    

        stage('build') {
            steps {
              sh 'mvn clean install'
              sh 'whoami'
                
            }
        } 
        stage('Building image') {
            steps{
                script {
                     dockerImage = docker.build registry + ":$BUILD_NUMBER"
        }
      }
    }
    
        stage('push to ECR') {
            steps {
              sh 'aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin 646094415288.dkr.ecr.ap-south-1.amazonaws.com'
              sh 'docker build -t java_poc:$BUILD_NUMBER .'
              sh 'docker tag java_poc:$BUILD_NUMBER 646094415288.dkr.ecr.ap-south-1.amazonaws.com/java_poc:$BUILD_NUMBER'
              sh 'docker push 646094415288.dkr.ecr.ap-south-1.amazonaws.com/java_poc:$BUILD_NUMBER'
                }
        }

        stage('Deploy eks') {
            steps{
                script {
                    
                    // sh 'kubectl rollout restart deployment spring-boot.yaml' 
                    kubernetesDeploy(configs: "spring-boot.yaml", kubeconfigId: "kubernetes", enableConfigSubstitution: "true")

        }
      }
    }
  }  
}

```

### In the jenkins ci/cd pipeline it did two changes for reflect whenever i change in the content in the following path ```src/main/java/com/springboot/app/WelcomeController.java```

1: I use ```$BUILD_NUMBER``` instead of using latest tag

2: enableConfigSubstitution: "true" in the jenkins pipeline

### spring-boot.yaml

```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: spring-boot-app
spec:
  replicas: 2
  selector:
    matchLabels:
      app: spring-boot-app
  template:
    metadata:
      labels:
        app: spring-boot-app
    spec:
      containers:
      - name: spring-boot-app
        image: 646094415288.dkr.ecr.ap-south-1.amazonaws.com/java_poc:${BUILD_NUMBER}
        imagePullPolicy: Always
        ports:
        - containerPort: 8080
# service type loadbalancers
---
apiVersion: v1
kind: Service
metadata:
  name: spring-boot-svc
spec:
  selector:
    app: spring-boot-app
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: LoadBalancer
  
  ```

```Click on apply and save```

![image](https://user-images.githubusercontent.com/85988020/188310724-564d71b2-0daf-44fa-aaab-aa9816024a45.png)

Before click on build the jenkins project run the following commands on that server in wchich eks master had installed otherwise it shows the following error.

![image](https://user-images.githubusercontent.com/85988020/188315243-f671504b-aafc-4810-94e0-3814266768ac.png)

### set a clusterrole as cluster-admin

By default, clusterrolebinding has system:anonymous set which blocks the cluster access. Execute the following command to set a clusterrole as cluster-admin which will give you the required access.

```kubectl create clusterrolebinding cluster-system-anonymous --clusterrole=cluster-admin --user=system:anonymous```

### After done the all configuration click on build now

![image](https://user-images.githubusercontent.com/85988020/188310777-610cfa07-e6ac-4f3a-9722-696031d953db.png)

### After that check the console output.

![image](https://user-images.githubusercontent.com/85988020/188310827-5e481d1e-9c25-436f-b68c-74f7fbd2c172.png)

![image](https://user-images.githubusercontent.com/85988020/188310868-5a6a19af-3d6e-4121-aadc-5c3edcb82e51.png)

### From here you can see the Overall pipeline flow:

![image](https://user-images.githubusercontent.com/85988020/188310884-a5d08f2f-8d58-4575-9f89-2d6193dc696d.png)

### Every time we buid the jenkins pipeline a new image build and store the newly image into ECR.

![image](https://user-images.githubusercontent.com/85988020/188310935-39339732-5e9f-4586-8eb9-ffaf5966cb90.png)

### After successfully build jenkins pipeline, go to eks server and check the pods by running the follwoing command.

![image](https://user-images.githubusercontent.com/85988020/188311027-56b06731-e458-4bdd-93fe-c3aecbe84b87.png)

### Check the service by running the follwoing command.

![image](https://user-images.githubusercontent.com/85988020/188311131-e8075b49-dd5f-46e3-b5d5-28af9b96111f.png)

### Go to the chrome browser and paste the load balancer link and attached /welcome with that link

![image](https://user-images.githubusercontent.com/85988020/188311377-e0a3830f-8a38-40d0-a565-a9c04fe60282.png)

### For webhook go to github repo and click on settings

![image](https://user-images.githubusercontent.com/85988020/188311441-2c5818f3-f24e-4d2c-bac3-2c8505988e3b.png)

### From here we can add the webhook

![image](https://user-images.githubusercontent.com/85988020/188311505-514ad5a3-d87b-49b6-999b-c5e9e73ecd59.png)

### Here is the Document link which i follow for this POC.

https://www.cidevops.com/2020/12/deploy-springboot-docker-app-into.html

```HAPPY LEARNING (:```
