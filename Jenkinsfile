pipeline {
    agent any

    environment {
        // Use your installed JDK 17 for compiling & testing the Spring Boot project
        JDK17_HOME   = "C:\\Program Files\\Eclipse Adoptium\\jdk-17.0.4.101-hotspot"
        // Use your Java 25 for the SonarQube 26.10 scanner
        JDK25_HOME   = "C:\\Java\\jdk-25.0.4"

        GIT_REPO_URL = "https://github.com/khalilbouhlel1/achat-pipeline.git"
        GIT_BRANCH   = "main" // Change to 'main' or 'master' if you merged your branch

        SONAR_URL    = "http://localhost:9000"
        SONAR_TOKEN  = "squ_5426936d009fed14309c280c0f21429e256cafbd"
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
                    set "JAVA_HOME=${JDK17_HOME}"
                    set "PATH=${JDK17_HOME}\\bin;%PATH%"
                    mvn clean compile
                """
            }
        }

        stage('3. Unit Tests & JaCoCo') {
            steps {
                bat """
                    set "JAVA_HOME=${JDK17_HOME}"
                    set "PATH=${JDK17_HOME}\\bin;%PATH%"
                    mvn test
                """
            }
        }

        stage('4. SonarQube Analysis') {
            steps {
                // SonarQube 26.10 scanner requires Java 21+ (uses your JDK 25)
                bat """
                    set "JAVA_HOME=${JDK25_HOME}"
                    set "PATH=${JDK25_HOME}\\bin;%PATH%"
                    mvn org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar -Dsonar.host.url=${SONAR_URL} -Dsonar.token=${SONAR_TOKEN}
                """
            }
        }

        stage('5. Package JAR') {
            steps {
                bat """
                    set "JAVA_HOME=${JDK17_HOME}"
                    set "PATH=${JDK17_HOME}\\bin;%PATH%"
                    mvn package -DskipTests
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