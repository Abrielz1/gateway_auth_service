pipeline {
    agent any

    environment {
        IMAGE_NAME = "abriel/gateway-auth-app:latest"

        KUBECONFIG = "/etc/rancher/k3s/k3s.yaml"
    }

    stages {
        stage('Checkout SCM') {
            steps {
                sh 'rm -rf ..?* .[!.]* *'
                checkout scm
            }
        }

        stage('Build Docker Image via Local Socket') {
            steps {

                sh "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Deploy to k3s Cluster') {
            steps {

                sh 'kubectl apply -f k8s/app.yaml'

                sh 'kubectl rollout restart deployment/gateway-auth-app'
                sh 'kubectl rollout status deployment/gateway-auth-app'
            }
        }
    }

    post {
        success {
            echo '✅ БИЛД И ДЕПЛОЙ ПРОШЛИ УСПЕШНО! ВСЁ РАБОТАЕТ!'
        }
        failure {
            echo '❌ ОШИБКА! Проверь логи Jenkins.'
        }
    }
}

