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

Select the Free tier and cick on  Next:configure insatnce Details

![image](https://user-images.githubusercontent.com/85988020/187845410-4e96fc43-79f7-41ed-add4-515444ecdd20.png)

Keep everything default and click on ```add storage```

![image](https://user-images.githubusercontent.com/85988020/187845669-2dbe88f9-00c6-4ead-b609-5e0c19657e6c.png)

Keep everything default and click on ```Next: Add Tags```

![image](https://user-images.githubusercontent.com/85988020/187847261-a895ba3b-b1ca-46d8-8289-3d4be5cfd66d.png)

 #Here you can add the tags of your instance.

![image](https://user-images.githubusercontent.com/85988020/187847070-e349c87a-b52b-4310-8729-4001a3050760.png)

#Here you can add security rules, in my case i choose ``All traffic rules allow``` after that click on ```Review and Launch```

![image](https://user-images.githubusercontent.com/85988020/187847881-646d0f2c-87f6-4a7f-9f4c-9c0d6cd58ba0.png)

From here you can review your instance and click on launch

![image](https://user-images.githubusercontent.com/85988020/187848296-fd1ff808-168a-4b17-bbed-ecdf55430ca9.png)






