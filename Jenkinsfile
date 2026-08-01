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

        stage('Build Docker Image via SSH') {
            steps {
                withCredentials([usernamePassword(
                        credentialsId: 'devuan-root-creds',
                        usernameVariable: 'SSH_USER',
                        passwordVariable: 'SSH_PASS'
                )]) {
                    echo 'убиваем старуй сборк на хуй...'
                    sh "sshpass -p '${SSH_PASS}' ssh -o StrictHostKeyChecking=no ${SSH_USER}@192.168.1.60 'rm -rf /root/gateway-build && mkdir -p /root/gateway-build'"

                    echo 'из докера швыряем образ в k3s и...'
                    sh "sshpass -p '${SSH_PASS}' scp -o StrictHostKeyChecking=no -r src pom.xml Dockerfile ${SSH_USER}@192.168.1.60:/root/gateway-build/"

                    echo 'Запускаю сборку Docker внутри движка k3s... и...'
                    sh "sshpass -p '${SSH_PASS}' ssh -o StrictHostKeyChecking=no ${SSH_USER}@192.168.1.60 'cd /root/gateway-build && docker build -t ${IMAGE_NAME} .'"
                }
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
