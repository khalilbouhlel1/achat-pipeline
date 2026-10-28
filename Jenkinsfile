pipeline {
    agent any

    tools {
        // These names must match your Jenkins "Manage Jenkins -> Tools" configuration
        maven 'Maven-3.9' 
        jdk 'JDK-17'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build & Test') {
            steps {
                // This executes all the Mockito and MockMvc tests we just wrote
                sh 'mvn clean install'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
    }
}