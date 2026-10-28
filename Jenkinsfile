pipeline {
    agent any

    tools {
        maven 'apache-maven-3.10.0' 
        jdk 'jdk21'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build & Test') {
            steps {
                // Changed from 'sh' to 'bat' for Windows execution
                bat 'mvn clean install'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                // Changed from 'sh' to 'bat' for Windows execution
                bat 'mvn package -DskipTests'
            }
        }
    }
}