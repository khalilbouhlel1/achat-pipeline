pipeline {
    agent any

    environment {
        JDK25_HOME     = "C:\\Program Files\\Java\\jdk-25.0.4"
        GIT_REPO_URL   = "https://github.com/khalilbouhlel1/achat-pipeline.git"
        GIT_BRANCH     = "dev-lotfi"

        SONAR_URL      = "http://localhost:9000"
        SONAR_TOKEN    = "sqa_e3f8c0f7f3ece10b132f0bc286174a71d58b3ce7"

        MAVEN_SETTINGS = "C:\\Users\\Lotfi\\.m2\\settings.xml"
        IMAGE_NAME     = "lotfii1/achat-app"
        IMAGE_TAG      = "dev-lotfi"
        CONTAINER_NAME = "achat-app-container"
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

        stage('6. Publish to Nexus') {
            steps {
                bat """
                    set "JAVA_HOME=${JDK25_HOME}"
                    set "PATH=${JDK25_HOME}\\bin;%PATH%"
                    mvnw.cmd deploy -DskipTests -s "${MAVEN_SETTINGS}"
                """
            }
        }

        stage('7. Building Our Image') {
            steps {
                bat "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }
        stage('7.5. Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat """
                        echo %DOCKER_PASS%| docker login -u %DOCKER_USER% --password-stdin
                        docker push ${IMAGE_NAME}:${IMAGE_TAG}
                    """
                }
            }
        }

        stage('8. Deploy Our Image') {
            steps {
                bat """
                    docker rm -f ${CONTAINER_NAME} 2>nul || exit /b 0
                    docker run -d --name ${CONTAINER_NAME} -p 8089:8089 ${IMAGE_NAME}:${IMAGE_TAG}
                """
            }
        }
    }

    post {
        success {
            echo "Pipeline complet reussi : SonarQube, Nexus, Docker Build et Deploy !"
        }
        failure {
            echo "Le pipeline a echoue. Verifie les logs dans Console Output."
        }
    }
}