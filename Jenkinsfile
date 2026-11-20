pipeline {
    agent any

    environment {
        IMAGE_NAME = "nejimissaoui/achat-app"
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
                bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.11.0.3922:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=sqa_0a08791e0ac011ca4e6e95f4c5fae289b821ed64'
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
                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat 'docker login -u %DOCKER_USER% -p %DOCKER_PASS%'
                    bat 'docker push %IMAGE_NAME%:%IMAGE_TAG%'
                }
            }
        }
    }
}