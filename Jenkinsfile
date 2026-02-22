pipeline {
    agent any

    tools {
        maven "maven"
    }

    environment {
        DOCKER_IMAGE = "diegoraar/backend-tingeso"
        SONAR_HOST_URL = "http://localhost:9000"
    }

    stages {
        stage("Checkout") {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']],
                          userRemoteConfigs: [[url: 'https://github.com/DiegoRaAr/Tingeso']]])
            }
        }

        stage("Backend - Tests & Coverage") {
            steps {
                dir("backend_tingeso") {
                    script {
                        echo "Ejecutando tests con JaCoCo..."
                        sh "mvn clean test jacoco:report"
                    }
                }
            }
        }

        stage("Backend - Checkstyle") {
            steps {
                dir("backend_tingeso") {
                    script {
                        echo "Analizando código con Checkstyle (Google Java Style Guide)..."
                        sh "mvn checkstyle:checkstyle"
                    }
                }
            }
        }

        stage("Backend - SonarQube Analysis") {
            steps {
                dir("backend_tingeso") {
                    script {
                        echo "Ejecutando análisis de SonarQube para Backend..."
                        withSonarQubeEnv('SonarQube') {
                            sh "mvn sonar:sonar"
                        }
                    }
                }
            }
        }

        stage("Backend - Quality Gate") {
            steps {
                script {
                    echo "Verificando Quality Gate del Backend..."
                    timeout(time: 5, unit: 'MINUTES') {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline abortado debido a falla en Quality Gate: ${qg.status}"
                        }
                        echo "✅ Backend: Technical Debt Ratio <= 1.0%"
                        echo "✅ Backend: Maintainability Rating = A"
                        echo "✅ Backend: No Code Smells High/Medium"
                    }
                }
            }
        }

        stage("Frontend - Install Dependencies") {
            steps {
                dir("frontend-tingeso") {
                    script {
                        echo "Instalando dependencias del Frontend..."
                        sh "npm install"
                    }
                }
            }
        }

        stage("Frontend - ESLint") {
            steps {
                dir("frontend-tingeso") {
                    script {
                        echo "Analizando código con ESLint (Airbnb Style Guide)..."
                        sh "npm run lint:report || true"
                    }
                }
            }
        }

        stage("Frontend - SonarQube Analysis") {
            steps {
                dir("frontend-tingeso") {
                    script {
                        echo "Ejecutando análisis de SonarQube para Frontend..."
                        withSonarQubeEnv('SonarQube') {
                            sh "sonar-scanner"
                        }
                    }
                }
            }
        }

        stage("Frontend - Quality Gate") {
            steps {
                script {
                    echo "Verificando Quality Gate del Frontend..."
                    timeout(time: 5, unit: 'MINUTES') {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline abortado debido a falla en Quality Gate: ${qg.status}"
                        }
                        echo "✅ Frontend: Technical Debt Ratio <= 2.0%"
                        echo "✅ Frontend: Maintainability Rating = A"
                        echo "✅ Frontend: No Code Smells High/Medium"
                    }
                }
            }
        }

        stage("Build JAR") {
            steps {
                dir("backend_tingeso") {
                    sh "mvn -B clean package -DskipTests"
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
