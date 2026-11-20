pipeline {
    agent any

    environment {
        // Installed JDK on system
        JDK_HOME     = "C:\\Program Files\\Java\\jdk-21.0.12"

        GIT_REPO_URL = "https://github.com/khalilbouhlel1/achat-pipeline.git"
        GIT_BRANCH   = "dev-khalil"

        SONAR_URL    = "http://localhost:9000"
        SONAR_TOKEN  = "squ_34b303ac2a595be9b3162eba30610c84f37ef89d"

        NEXUS_URL    = "http://localhost:8081"
        DOCKER_IMAGE = "khalilbouhlel1/achat-devops:latest"
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
                    set "JAVA_HOME=${JDK_HOME}"
                    set "PATH=${JDK_HOME}\\bin;%PATH%"
                    mvn clean compile
                """
            }
        }

        stage('3. Unit Tests & JaCoCo') {
            steps {
                bat """
                    set "JAVA_HOME=${JDK_HOME}"
                    set "PATH=${JDK_HOME}\\bin;%PATH%"
                    mvn test
                """
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('4. SonarQube Analysis') {
            steps {
                bat """
                    set "JAVA_HOME=${JDK_HOME}"
                    set "PATH=${JDK_HOME}\\bin;%PATH%"
                    mvn org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar ^
                        -Dsonar.host.url=${SONAR_URL} ^
                        -Dsonar.token=${SONAR_TOKEN} ^
                        -Dsonar.java.coveragePlugin=jacoco ^
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                """
            }
        }

        stage('5. Package JAR') {
            steps {
                bat """
                    set "JAVA_HOME=${JDK_HOME}"
                    set "PATH=${JDK_HOME}\\bin;%PATH%"
                    mvn package -DskipTests
                """
            }
        }

        stage('6. Deploy to Nexus') {
            steps {
                bat """
                    set "JAVA_HOME=${JDK_HOME}"
                    set "PATH=${JDK_HOME}\\bin;%PATH%"
                    mvn deploy -DskipTests
                """
            }
        }

        stage('7. Docker Build') {
            steps {
                bat "docker build -t ${DOCKER_IMAGE} ."
            }
        }
    }

    post {
        success {
            echo "Pipeline completed successfully!"
            echo "SonarQube: http://localhost:9000"
            echo "Nexus: http://localhost:8081"
        }
        failure {
            echo "Pipeline failed. Check the stage logs in Console Output."
        }
    }
}