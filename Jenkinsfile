pipeline {
    agent any

    environment {
        JDK25_HOME         = "C:\\Program Files\\Java\\jdk-25.0.4"
        GIT_REPO_URL       = "https://github.com/khalilbouhlel1/achat-pipeline.git"
        GIT_BRANCH         = "dev-lotfi"

        SONAR_URL          = "http://localhost:9000"
        SONAR_TOKEN        = "sqa_e3f8c0f7f3ece10b132f0bc286174a71d58b3ce7"

        MAVEN_SETTINGS     = "C:\\Users\\Lotfi\\.m2\\settings.xml"
        IMAGE_NAME         = "lotfii1/achat-app"
        IMAGE_TAG          = "dev-lotfi"
        CONTAINER_NAME     = "achat-app-container"

        AWS_DEFAULT_REGION = "us-east-1"
        EKS_CLUSTER_NAME   = "achat-eks-cluster"

        PATH               = "C:\\Program Files\\Amazon\\AWSCLIV2;C:\\Users\\Lotfi\\AppData\\Local\\Microsoft\\WinGet\\Links;${env.PATH}"
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
                    docker network create achat-net 2>nul || ver >nul
                    docker rm -f ${CONTAINER_NAME} 2>nul || ver >nul
                    docker run -d --name ${CONTAINER_NAME} --network achat-net -p 8089:8089 -e SPRING_DATASOURCE_URL="jdbc:mysql://achat-mysql:3306/achat?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC" -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD=root ${IMAGE_NAME}:${IMAGE_TAG}
                """
            }
        }

        stage('Test AWS Credentials') {
            steps {
                withCredentials([
                    file(credentialsId: 'aws-creds-file', variable: 'AWS_SHARED_CREDENTIALS_FILE')
                ]) {
                    bat 'aws sts get-caller-identity'
                }
            }
        }

        stage('SETUP TERRAFORM') {
            steps {
                withCredentials([
                    file(credentialsId: 'aws-creds-file', variable: 'AWS_SHARED_CREDENTIALS_FILE')
                ]) {
                    dir('terraform') {
                        bat '''
                            terraform init
                            terraform apply -auto-approve
                        '''
                    }
                }
            }
        }

        stage('UPDATE KUBECONFIG') {
            steps {
                withCredentials([
                    file(credentialsId: 'aws-creds-file', variable: 'AWS_SHARED_CREDENTIALS_FILE')
                ]) {
                    bat "aws eks update-kubeconfig --region ${AWS_DEFAULT_REGION} --name ${EKS_CLUSTER_NAME}"
                }
            }
        }

        stage('DEPLOY TO AWS KUBERNETES') {
            steps {
                withCredentials([
                    file(credentialsId: 'aws-creds-file', variable: 'AWS_SHARED_CREDENTIALS_FILE')
                ]) {
                    bat '''
                        kubectl apply -f k8s/
                        kubectl get pods
                        kubectl get svc
                    '''
                }
            }
        }
    }

    post {
        always {
            // Tes éventuelles actions existantes ici (ex: echo, cleanWs...)
            emailext(
                to: 'lotfimz456@gmail.com',
                subject: "Jenkins Pipeline [${currentBuild.currentResult}] - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    <p><b>Job:</b> ${env.JOB_NAME}</p>
                    <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
                    <p><b>Status:</b> ${currentBuild.currentResult}</p>
                    <p><b>Console Output:</b> <a href="${env.BUILD_URL}console">${env.BUILD_URL}console</a></p>
                    <p>The full pipeline log is attached to this email.</p>
                """,
                mimeType: 'text/html',
                attachLog: true,
                compressLog: false
            )
        }
        success {
            echo 'Pipeline réussi !'
        }
        failure {
            echo 'Pipeline échoué !'
        }
    }
}