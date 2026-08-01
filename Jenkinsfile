pipeline {
    agent any

    environment {
        // Локальное имя образа в кэше докера вашей виртуалки
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
                echo 'Очищаем старую папку сборки на виртуалке...'
                sh "ssh root@192.168.1.60 'rm -rf /root/gateway-build && mkdir -p /root/gateway-build'"

                echo 'Перекидываем исходный код и Dockerfile на виртуалку k3s...'
                sh "scp -r src pom.xml Dockerfile root@192.168.1.60:/root/gateway-build/"

                echo 'Запускаем сборку Docker внутри движка k3s...'
                // Docker сам скачает maven, скомпилирует Java 21 и положит образ в локальный кэш k3s!
                sh "ssh root@192.168.1.60 'cd /root/gateway-build && docker build -t ${IMAGE_NAME} .'"
            }
        }

        stage('Deploy to k3s Cluster') {
            steps {
                withCredentials([file(credentialsId: 'k3s-kubeconfig', variable: 'KUBECONFIG')]) {
                    echo 'Применяем манифест приложения k8s.yaml...'
                    sh "kubectl apply -f k8s/app.yaml --kubeconfig=$KUBECONFIG"

                    echo 'Перезапускаем поды, чтобы k3s взял свежий локальный образ...'
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
