pipeline {
    agent any

    tools {
        maven 'maven-3.9'
    }

    environment {

        DOCKER_REPO = "Abriel"
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
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {

                    sh """
                    mvn jib:build \
                      -Djib.to.image=${DOCKER_REPO}:${DOCKER_TAG} \
                      -Djib.to.tags=latest \
                      -Djib.to.auth.username=\$DOCKER_USER \
                      -Djib.to.auth.password=\$DOCKER_PASS
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