pipeline {
    agent any

    environment {
        IMAGE_NAME = "abriel/gateway-auth-app:latest"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image via Local Socket') {
            steps {
                sh '''
                apt-get update && apt-get install -y \
                  ca-certificates \
                  curl \
                  gnupg \
                  lsb-release
                
                mkdir -p /etc/apt/keyrings
                curl -fsSL https://docker.com | gpg --dearmor --yes -o /etc/apt/keyrings/docker.gpg
                
                echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://docker.com $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
                
                apt-get update && apt-get install -y docker-ce-cli
                '''
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }


        stage('Deploy to k3s Cluster') {
            steps {
                withCredentials([file(credentialsId: 'k3s-kubeconfig', variable: 'KUBECONFIG')]) {
                    sh "kubectl apply -f k8s/app.yaml --kubeconfig=$KUBECONFIG"
                    sh "kubectl rollout restart deployment/gateway-auth-app --kubeconfig=$KUBECONFIG"
                    sh "kubectl rollout status deployment/gateway-auth-app --kubeconfig=$KUBECONFIG"
                }
            }
        }
    }

    post {
        success {
            echo '✅ Билд и локальный Деплой прошли успешно! Архитектура работает!'
        }
        failure {
            echo '❌ ОШИБКА! Проверь логи Jenkins.'
        }
    }
}
