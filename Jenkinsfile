pipeline {
    agent any
tools {
        maven 'M3'
    }
    environment {
        DOCKER_IMAGE = "loo9a/achat:1.0-SNAPSHOT"
    }

    stages {
        stage('CHECKOUT GIT') {
            steps {
                git branch: 'dev-farouk', url: 'https://github.com/khalilbouhlel1/achat-pipeline.git'
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
                    bat "mvn sonar:sonar -Dsonar.projectKey=achat -Dsonar.host.url=http://localhost:9000 -Dsonar.token=%SONAR_TOKEN%"
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
                    bat 'echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin'
                    bat "docker push %DOCKER_IMAGE%"
                    bat 'docker logout'
                }
            }
        }
    }
}