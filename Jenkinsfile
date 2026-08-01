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
                        usernameVariable: 'HUB_USER',
                        passwordVariable: 'HUB_PASS'
                )]) {
                    echo 'Создаем временный settings.xml для авторизации Maven... ща упадёт'

                    sh '''
            cat << 'EOF' > settings.xml
<settings xmlns="http://apache.org"
          xmlns:xsi="http://w3.org"
          xsi:schemaLocation="http://apache.org https://apache.org">
    <servers>
        <server>
            <id>registry-1.docker.io</id>
            <username>${HUB_USER}</username>
            <password>${HUB_PASS}</password>
        </server>
    </servers>
</settings>
EOF
            '''

                    echo 'Компилируем проект и пушим в Docker Hub через settings.xml... или шас'
                    sh """
            mvn clean package jib:build \
              -s settings.xml \
              -DskipTests \
              -Djib.to.image=${DOCKER_REPO}:${DOCKER_TAG} \
              -Djib.to.tags=latest
            """
                }
            }
        }

        stage('Deploy to k3s Cluster') {
            steps {
                withCredentials([file(credentialsId: 'k3s-kubeconfig', variable: 'KUBECONFIG')]) {

                    echo 'или шас'
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