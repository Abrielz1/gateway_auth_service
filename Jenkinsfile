pipeline {
    agent any

    tools {
        maven 'maven-3.9'
    }

    environment {

        DOCKER_REPO = "abriel/gateway-auth-app"
        DOCKER_TAG = "v${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {

                checkout scm
            }
        }

        stage('Build Spring Boot') {
            steps {
                echo 'Компилируем проект через Maven...'

                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build & Push Docker Image (via Jib)') {
            steps {
                withCredentials([usernamePassword(
                        credentialsId: 'docker-hub-creds',
                        usernameVariable: 'JIB_TO_AUTH_USERNAME',
                        passwordVariable: 'JIB_TO_AUTH_PASSWORD'
                )]) {
                    echo 'Сборка и пуш Docker-образа через переменные среды Jib...'
                    sh """
            mvn clean package jib:build \\
              -DskipTests \\
              -Djib.to.image=${DOCKER_REPO}:${DOCKER_TAG} \\
              -Djib.to.tags=latest
            """
                }
            }
        }

        stage('Deploy to k3s Cluster') {
            steps {
                withCredentials([file(credentialsId: 'k3s-kubeconfig', variable: 'KUBECONFIG')]) {


                    sh "sed -i 's|image: .*|image: ${DOCKER_REPO}:${DOCKER_TAG}|g' k8s/app.yaml"

                    sh "kubectl apply -f k8s/app.yaml --kubeconfig=$KUBECONFIG"

                    sh "kubectl rollout status deployment/gateway-auth-app --kubeconfig=$KUBECONFIG"
                }
            }
        }
    }

    post {
        success {
            echo '✅ Билд и Деплой прошли успешно! Сервис в кластере обновлен!'
        }
        failure {
            echo '❌ ОШИБКА! Билд упал. Проверь логи Jenkins.'
        }
    }
}