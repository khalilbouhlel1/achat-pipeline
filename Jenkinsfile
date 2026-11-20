pipeline {
    agent any

    environment {
        JDK25_HOME   = "C:\\Program Files\\Java\\jdk-25.0.4"
        GIT_REPO_URL = "https://github.com/khalilbouhlel1/achat-pipeline.git"
        GIT_BRANCH   = "dev-lotfi"
        SONAR_URL    = "http://localhost:9000"
        SONAR_TOKEN  = "sqa_e3f8c0f7f3ece10b132f0bc286174a71d58b3ce7"
    }

    stages {
        stage('1. Git Checkout') {
            steps {
                git branch: "${GIT_BRANCH}", url: "${GIT_REPO_URL}"
            }
        }

        stage('2. Maven Clean & Compile') {
            steps {
                bat """
                    set "JAVA_HOME=${JDK25_HOME}"
                    set "PATH=${JDK25_HOME}\\bin;%PATH%"
                    mvnw.cmd clean compile
                """
            }
        }

        stage('3. Unit Tests & JaCoCo') {
            steps {
                bat """
                    set "JAVA_HOME=${JDK25_HOME}"
                    set "PATH=${JDK25_HOME}\\bin;%PATH%"
                    mvnw.cmd test
                """
            }
        }

        stage('4. SonarQube Analysis') {
            steps {
                bat """
                    set "JAVA_HOME=${JDK25_HOME}"
                    set "PATH=${JDK25_HOME}\\bin;%PATH%"
                    mvnw.cmd org.sonarsource.scanner.maven:sonar-maven-plugin:5.9.0.7382:sonar -Dsonar.host.url=${SONAR_URL} -Dsonar.token=${SONAR_TOKEN}
                """
            }
        }

        stage('5. Package JAR') {
            steps {
                bat """
                    set "JAVA_HOME=${JDK25_HOME}"
                    set "PATH=${JDK25_HOME}\\bin;%PATH%"
                    mvnw.cmd package -DskipTests
                """
            }
        }
    }

    post {
        success {
            echo "Pipeline completed! Check your dashboard at http://localhost:9000"
        }
        failure {
            echo "Pipeline failed. Check the stage logs in Console Output."
        }
    }
}