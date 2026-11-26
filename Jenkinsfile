pipeline {
    agent any
    tools {
        maven 'apache-maven-3.10.0'
    }
    environment {
        DOCKER_IMAGE = "khalilbouhlel1/achat:1.0-SNAPSHOT"
        AWS_DEFAULT_REGION = "us-east-1"
    }

    stages {
        stage('CHECKOUT GIT') {
            steps {
                git branch: 'main', url: 'https://github.com/khalilbouhlel1/achat-pipeline.git'
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
             withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                 bat "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=achat -Dsonar.host.url=http://localhost:9000 -Dsonar.token=%SONAR_TOKEN% -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml -Dsonar.coverage.exclusions=**/entities/**,**/config/**,**/*Application.java,**/controllers/**"
             }
         }
     }

        stage('PUBLISH TO NEXUS') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASSWORD')]) {
                    writeFile file: 'settings-nexus.xml', text: """<settings>
                        <servers>
                            <server>
                                <id>nexus</id>
                                <username>${NEXUS_USER}</username>
                                <password>${NEXUS_PASSWORD}</password>
                            </server>
                        </servers>
                    </settings>"""

                    bat 'mvn deploy -s settings-nexus.xml -DskipTests'
                }
            }
        }

        stage('BUILDING OUR IMAGE') {
            steps {
                bat "docker build -t %DOCKER_IMAGE% ."
            }
        }

        stage('DEPLOY OUR IMAGE') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat 'docker login -u "%DOCKER_USER%" -p "%DOCKER_PASS%"'
                    bat 'docker push %DOCKER_IMAGE%'
                    bat 'docker logout'
                }
            }
        }
        stage('Test AWS Credentials') {
         steps {
             withCredentials([
                 string(credentialsId: 'aws-access-key', variable: 'AWS_ACCESS_KEY_ID'),
                 string(credentialsId: 'aws-secret-key', variable: 'AWS_SECRET_ACCESS_KEY'),
                 string(credentialsId: 'aws-session-token', variable: 'AWS_SESSION_TOKEN')
             ]) {
                 bat 'aws sts get-caller-identity'
             }
         }
     
    }
    stage('SETUP TERRAFORM') {
            steps {
                withCredentials([
                    string(credentialsId: 'aws-access-key', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'aws-secret-key', variable: 'AWS_SECRET_ACCESS_KEY'),
                    string(credentialsId: 'aws-session-token', variable: 'AWS_SESSION_TOKEN')
                ]) {
                    dir('terraform') {
                        bat 'terraform init'
                        bat 'terraform apply -auto-approve'
                    }
                }
            }
        }
        stage('UPDATE KUBECONFIG') {
            steps {
                withCredentials([
                    string(credentialsId: 'aws-access-key', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'aws-secret-key', variable: 'AWS_SECRET_ACCESS_KEY'),
                    string(credentialsId: 'aws-session-token', variable: 'AWS_SESSION_TOKEN')
                ]) {
                    bat 'aws eks update-kubeconfig --region us-east-1 --name achat-cluster'
                }
            }
        }

        stage('DEPLOY TO AWS KUBERNETES') {
            steps {
                withCredentials([
                    string(credentialsId: 'aws-access-key', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'aws-secret-key', variable: 'AWS_SECRET_ACCESS_KEY'),
                    string(credentialsId: 'aws-session-token', variable: 'AWS_SESSION_TOKEN')
                ]) {
                    bat 'kubectl apply -f k8s/deployment.yaml'
                    bat 'kubectl rollout restart deployment/achat-app'
                    bat 'kubectl get pods'
                    bat 'kubectl get svc achat-service'
                }
            }
        }
        }
}