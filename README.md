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

First Go to ```AWS console``` and Install a ```ubuntu 20.04``` server.




