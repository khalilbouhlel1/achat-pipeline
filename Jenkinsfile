pipeline {
    agent any

    environment {
        IMAGE_NAME = "neji/achat-app"
        IMAGE_TAG  = "latest"
    }

    stages {
        stage('CHECKOUT GIT') {
            steps {
                git branch: 'dev-neji', url: 'https://github.com/khalilbouhlel1/achat-pipeline.git'
            }
        }

        stage('MVN CLEAN') {
            steps {
                bat 'mvn clean'
            }
        }

        stage('ARTIFACT CONSTRUCTION') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }

        stage('UNIT TESTS') {
            steps {
                bat 'mvn test'
            }
        }

        stage('MVN SONARQUBE') {
            steps {
                bat 'mvn sonar:sonar'
            }
        }

        stage('PUBLISH TO NEXUS') {
            steps {
                bat 'mvn deploy -DskipTests'
            }
        }

        stage('BUILDING OUR IMAGE') {
            steps {
                bat "docker build -t %IMAGE_NAME%:%IMAGE_TAG% ."
            }
        }

        stage('DEPLOY OUR IMAGE') {
            steps {
                bat "docker login -u USERNAME_DOCKERHUB -p PASSWORD_DOCKERHUB"
                bat "docker push %IMAGE_NAME%:%IMAGE_TAG%"
            }
        }
    }
}