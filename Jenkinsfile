pipeline {
    agent any

    environment {
        IMAGE_NAME         = "nejimissaoui/achat-app"
        IMAGE_TAG          = "latest"
        AWS_DEFAULT_REGION = "us-east-1"
        EKS_CLUSTER_NAME   = "<NOM_CLUSTER_EKS>"
        TERRAFORM_DIR      = "terraform"
        K8S_DIR            = "k8s"
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

        stage('Test AWS Credentials') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'aws-credentials',
                        usernameVariable: 'AWS_ACCESS_KEY_ID',
                        passwordVariable: 'AWS_SECRET_ACCESS_KEY'
                    ),
                    string(
                        credentialsId: 'aws-session-token',
                        variable: 'AWS_SESSION_TOKEN'
                    )
                ]) {
                    bat 'aws sts get-caller-identity'
                }
            }
        }

        stage('SETUP TERRAFORM') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'aws-credentials',
                        usernameVariable: 'AWS_ACCESS_KEY_ID',
                        passwordVariable: 'AWS_SECRET_ACCESS_KEY'
                    ),
                    string(
                        credentialsId: 'aws-session-token',
                        variable: 'AWS_SESSION_TOKEN'
                    )
                ]) {
                    bat """
                        cd %TERRAFORM_DIR%
                        terraform init
                        terraform plan -out=tfplan
                        terraform apply -auto-approve tfplan
                    """
                }
            }
        }

        stage('UPDATE KUBECONFIG') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'aws-credentials',
                        usernameVariable: 'AWS_ACCESS_KEY_ID',
                        passwordVariable: 'AWS_SECRET_ACCESS_KEY'
                    ),
                    string(
                        credentialsId: 'aws-session-token',
                        variable: 'AWS_SESSION_TOKEN'
                    )
                ]) {
                    bat 'aws eks update-kubeconfig --region %AWS_DEFAULT_REGION% --name %EKS_CLUSTER_NAME%'
                }
            }
        }

        stage('DEPLOY TO AWS KUBERNETES') {
            steps {
                bat """
                    cd %K8S_DIR%
                    kubectl apply -f .
                    kubectl get pods
                    kubectl get svc
                """
            }
        }
    }
}