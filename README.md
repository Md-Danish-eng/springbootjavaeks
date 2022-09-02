## Complete CI/CD Pipeline with EKS and AWS ECR

#Technologies used

```Kubernetes, Jenkins, AWS EKS, AWS ECR, Java, Maven, Linux, Docker, Git```

#POC Requirements

Create a private AWS ECR Docker repository

Adjust Jenkinsfile to build and push Docker image to AWS ECR Integrate deploying to K8s cluster in the CI/CD pipeline from AWS ECR private registry

So the complete CI/CD project we build has the following configuration:

a.  step: Increment version
b.  step: Build artifact for Java Maven application
c.  step: Build and push Docker image to AWS ECR
d.  step: Deploy new application version to EKS cluster
e.  step: Commit the version update


Install EKS (Elastic Kubernetes Service) On ubuntu server:

Note: There are so many methods to Install Eks. In my case I use command line.

First Go to ```AWS console``` and Install a ```ubuntu 20.04``` server by follwoing methods:

Select this ```Ubuntu Server 20.04 LTS (HVM), SSD Volume Type```

![image](https://user-images.githubusercontent.com/85988020/187845138-a4572857-6973-4b1e-b924-742992b24c66.png)

Select the "t3.medium" and cick on  Next:configure insatnce Details

![image](https://user-images.githubusercontent.com/85988020/188131393-112e49e8-70b7-455b-96f9-b8d75c6aa47b.png)

Keep everything default and click on ```add storage```

![image](https://user-images.githubusercontent.com/85988020/188131650-dec0809c-fdc3-415a-bad6-c168ec93bfc6.png)

Keep everything default and click on ```Next: Add Tags```

![image](https://user-images.githubusercontent.com/85988020/188131984-d27eb40d-9552-4d97-b1a4-b9f8538099a4.png)

 #Here you can add the tags of your instance.

![image](https://user-images.githubusercontent.com/85988020/187847261-a895ba3b-b1ca-46d8-8289-3d4be5cfd66d.png)

#Here you can add security rules, in my case i choose ``All traffic rules allow``` after that click on ```Review and Launch```

![image](https://user-images.githubusercontent.com/85988020/187847881-646d0f2c-87f6-4a7f-9f4c-9c0d6cd58ba0.png)

From here you can review your instance and click on launch

![image](https://user-images.githubusercontent.com/85988020/187848296-fd1ff808-168a-4b17-bbed-ecdf55430ca9.png)

Here Choose the existing key or create a new key and click on launch instance.

![image](https://user-images.githubusercontent.com/85988020/188130515-8d726a9d-5ca6-4e05-90c3-9988cb806d8e.png)

Now check the AWS console your instance is showing here.

![image](https://user-images.githubusercontent.com/85988020/188130787-da310a5f-66d7-457c-990d-53706746d1ad.png)

Do SSH on server and Install EKS by following command:

Install eksctl by run ```curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp```

```sudo mv /tmp/eksctl /usr/local/bin```

```eksctl version```

```curl -o kubectl https://amazon-eks.s3.us-west-2.amazonaws.com/1.21.2/2021-07-05/bin/linux/amd64/kubectl```

```chmod +x ./kubectl```
``mkdir -p $HOME/bin && cp ./kubectl $HOME/bin/kubectl && export PATH=$PATH:$HOME/bin```
```echo 'export PATH=$PATH:$HOME/bin' >> ~/.bashrc```
```kubectl version --short --client```
```eksctl create cluster --name java-poc --region ap-south-1 --version 1.21 --nodegroup-name linux-nodes --node-type t2.medium --nodes 2```
Install awscli by run the following commands:

curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
 unzip awscliv2.zip
sudo apt install unzip
unzip awscliv2.zip
 sudo ./aws/install

Install docker by run the following commands:

sudo apt update
sudo apt install apt-transport-https ca-certificates curl software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"
 sudo apt install docker-ce
sudo systemctl status docker


sudo apt update
 sudo apt install openjdk-11-jdk
 wget -p -O - https://pkg.jenkins.io/debian/jenkins.io.key | sudo apt-key add -
sudo sh -c 'echo deb http://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'
sudo apt install jenkins
sudo systemctl start jenkins
sudo systemctl status jenkins
Go to the console and copy the public ip and hit in the browser with port 8080
It will ask for password
Go to the server and run the command for password ```sudo cat /var/lib/jenkins/secrets/initialAdminPassword```

copy and paste the password in the jenkins browser and click on 





Create a private ECR (Elastic Container Registry)

![image](https://user-images.githubusercontent.com/85988020/188152096-794a4ec1-12d2-443a-81ba-0b7cd1988e94.png)

After this it will look like this

![image](https://user-images.githubusercontent.com/85988020/188152501-201bcf2f-94b9-4e60-bdfe-596095b6beae.png)


Jenkins pipeline
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





