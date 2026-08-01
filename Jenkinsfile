pipeline {
    agent any

    environment {
        // ТВОЙ ЛОГИН НА DOCKER HUB (замени "abrielz1" на свой реальный логин в Docker Hub)
        DOCKER_REPO = "abrielz1/gateway_auth_service"
        // Генерируем уникальный тег для каждого билда (например, v15)
        DOCKER_TAG = "v${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                // Дженкинс сам скачает код из ветки main
                checkout scm
            }
        }

        stage('Build Spring Boot') {
            steps {
                echo 'Компилируем проект через Maven...'
                // Собираем JAR без тестов (чтобы билд был быстрым и не зависел от баз)
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                echo 'Собираем Docker образ и пушим в Registry...'
                // Подтягиваем креды от Docker Hub (нужно создать в настройках Jenkins)
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    // Логинимся в Докер
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'

                    // Собираем образ по нашему Dockerfile
                    sh "docker build -t ${DOCKER_REPO}:${DOCKER_TAG} ."
                    sh "docker tag ${DOCKER_REPO}:${DOCKER_TAG} ${DOCKER_REPO}:latest"

                    // Заливаем в интернет
                    sh "docker push ${DOCKER_REPO}:${DOCKER_TAG}"
                    sh "docker push ${DOCKER_REPO}:latest"
                }
            }
        }

        stage('Deploy to k3s Cluster') {
            steps {
                echo 'Деплоим сервис в Kubernetes...'
                // Подтягиваем конфиг k3s (нужно создать в настройках Jenkins)
                withCredentials([file(credentialsId: 'k3s-kubeconfig', variable: 'KUBECONFIG')]) {

                    // Магия Linux: берем наш файл k8s/app.yaml и подменяем там строчку с image на наш новый свежий тег
                    sh "sed -i 's|image: .*|image: ${DOCKER_REPO}:${DOCKER_TAG}|g' k8s/app.yaml"

                    // Применяем манифест к кластеру
                    sh "kubectl apply -f k8s/app.yaml --kubeconfig=$KUBECONFIG"

                    // Ждём, пока Кубер поднимет поды и убедится, что они здоровы
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