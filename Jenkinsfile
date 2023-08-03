
peline {
  agent none

  stages {
    stage('Checkout and Build') {
      agent {
        docker {
          image 'abhishekf5/maven-abhishek-docker-agent:v1'
          args '--user root -v /var/run/docker.sock:/var/run/docker.sock' // mount Docker socket to access the host's Docker daemon
        }
      }
      steps {
        sh 'echo passed'
        //git branch: 'main', url: 'https://github.com/iam-veeramalla/Jenkins-Zero-To-Hero.git'
        sh 'cd java-maven-sonar-argocd-helm-k8s/spring-boot-app && mvn clean package'
      }
    }
    stage('Build and Push Docker Image') {
      agent any
      environment {
        AWS_REGION = 'us-east-2'
        ECR_REPOSITORY_NAME = '390253046172.dkr.ecr.us-east-2.amazonaws.com/nodejsapp'
        DOCKER_IMAGE_NAME = "sangram"
        DOCKERFILE_PATH = "java-maven-sonar-argocd-helm-k8s/spring-boot-app" // e.g., "java-maven-sonar-argocd-helm-k8s/spring-boot-app/Dockerfile"
      }
      steps {
        script {
          // Build the Docker image
          sh "docker build -t $DOCKER_IMAGE_NAME $DOCKERFILE_PATH"

          // Log in to AWS ECR
          sh "aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_REPOSITORY_NAME"

          // Tag the Docker image with the ECR repository URI
          def ecrImageURI = "$ECR_REPOSITORY_NAME:${BUILD_NUMBER}"
          sh "docker tag $DOCKER_IMAGE_NAME $ecrImageURI"

          // Push the image to ECR
          sh "docker push $ecrImageURI"
        }
      }
    }
    stage('Update Deployment File') {
      agent any
      environment {
        GIT_REPO_NAME = "cicd-project-2"
        GIT_USER_NAME = "Sangramshinde97"
      }
      steps {
        withCredentials([string(credentialsId: 'github', variable: 'GITHUB_TOKEN')]) {
          sh '''
            git config user.email "Sangram.xyz@gmail.com"
            git config user.name "Sangram Shinde"
            BUILD_NUMBER=${BUILD_NUMBER}
            sed -i "s|image: 390253046172.dkr.ecr.us-east-2.amazonaws.com/nodejsapp:[0-9]*|image: 390253046172.dkr.ecr.us-east-2.amazonaws.com/nodejsapp:${BUILD_NUMBER}|g" java-maven-sonar-argocd-helm-k8s/spring-boot-app-manifests/deployment.yml
            cat java-maven-sonar-argocd-helm-k8s/spring-boot-app-manifests/deployment.yml
            git branch
            git add java-maven-sonar-argocd-helm-k8s/spring-boot-app-manifests/deployment.yml
            git status
            git commit -m "Update deployment image to version ${BUILD_NUMBER}"
            git status
            git branch
            git push https://${GITHUB_TOKEN}@github.com/${GIT_USER_NAME}/${GIT_REPO_NAME} HEAD:main
          '''
        }
      }
    }
  }
}
pipeline {
