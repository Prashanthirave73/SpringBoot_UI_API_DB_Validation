pipeline {

    agent any

    tools {
        jdk 'Myjava'
        maven 'TestMaven'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Prashanthirave73/SpringBoot_UI_API_DB_Validation.git'
            }
        }

        stage('Build Project') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Start Spring Boot Application') {
            steps {
                bat '''
                start "" cmd /c "mvn spring-boot:run"
                '''
            }
        }

        stage('Wait For Application Startup') {
            steps {
                sleep(time: 40, unit: 'SECONDS')
            }
        }

        stage('Execute Automation Tests') {
            steps {
                bat 'mvn test'
            }
        }
    }

    post {

        always {
            junit allowEmptyResults: true,
                  testResults: 'target/surefire-reports/*.xml'
        }

        success {
            echo 'Pipeline executed successfully'
        }

        failure {
            echo 'Pipeline execution failed'
        }
    }
}