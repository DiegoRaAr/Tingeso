pipeline{
    agent any
    tools{
        maven "maven"
    }
    stages{
        stage("Build JAR File"){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/DiegoRaAr/Tingeso']])
                dir("backend_tingeso"){
                    sh "mvn clean package"
                }
            }
        }
        stage("Test"){
            steps{
                dir("backend_tingeso"){
                    sh "mvn test"
                }
            }
        }
        stage("Build and Push Docker Images"){
            steps{
                dir("backend_tingeso"){
                    script{
                        withDockerRegistry(credentialsId: 'docker-credentials'){
                            bat "docker build -t diegoraar/backend-tingeso ."
                            bat "docker push diegoraar/backend-tingeso"
                        }
                    }
                }
            }
        }
    }
}