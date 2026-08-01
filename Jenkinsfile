pipeline {
    agent any

    environment {
        // Локальное имя образа в кэше докера вашей виртуалки
        IMAGE_NAME = "abriel/gateway-auth-app:latest"
    }

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image via Local Socket') {
            steps {
                echo 'Запускаем каноничную Multi-stage сборку образа через проброшенный Docker хоста...'
                // Команда выполняется локально, используя бинарник и сокет с твоей Devuan-виртуалки!
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Deploy to k3s Cluster') {
            steps {
                withCredentials([file(credentialsId: 'k3s-kubeconfig', variable: 'KUBECONFIG')]) {
                    echo 'Применяем ваш k8s.yaml манифест...'
                    sh "kubectl apply -f k8s/app.yaml --kubeconfig=$KUBECONFIG"

                    echo 'Мгновенно перезапускаем поды из локального кэша...'
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
