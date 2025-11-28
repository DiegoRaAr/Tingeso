pipeline {
    agent any

    tools {
        maven "maven"
    }

    environment {
        DOCKER_IMAGE = "diegoraar/backend-tingeso"
    }

    stages {
        stage("Checkout") {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']],
                          userRemoteConfigs: [[url: 'https://github.com/DiegoRaAr/Tingeso']]])
            }
        }

        stage("Build JAR") {
            steps {
                dir("backend_tingeso") {
                    sh "mvn -B clean package"
                }
            }
        }

        stage("Test") {
            steps {
                dir("backend_tingeso") {
                    sh "mvn -B test"
                }
            }
        }

        stage("Build and Push Docker Image") {
            steps {
                dir("backend_tingeso") {
                    script {
                        docker.withRegistry('https://index.docker.io/v1/', 'docker-credentials') {
                            def img = docker.build("${env.DOCKER_IMAGE}:${env.BUILD_NUMBER}")
                            img.push()
                            img.push("latest")
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo "Build y push completados: ${env.BUILD_NUMBER}"
        }
        failure {
            echo "Algo falló. Revisa la consola de Jenkins."
        }
    }
}
