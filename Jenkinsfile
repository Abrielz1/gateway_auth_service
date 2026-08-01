pipeline {
    agent any

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

                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {

                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'

                    sh "docker build -t ${DOCKER_REPO}:${DOCKER_TAG} ."
                    sh "docker tag ${DOCKER_REPO}:${DOCKER_TAG} ${DOCKER_REPO}:latest"

                    sh "docker push ${DOCKER_REPO}:${DOCKER_TAG}"
                    sh "docker push ${DOCKER_REPO}:latest"
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