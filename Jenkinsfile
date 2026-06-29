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

        stage('Start Spring Boot Application') {
            steps {
                bat '''
                start "" cmd /c "mvn clean spring-boot:run"
                '''
            }
        }

        stage('Wait For Application Startup') {
            steps {
                bat 'timeout /t 40'
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
            junit 'target/surefire-reports/*.xml'

            archiveArtifacts artifacts: '**/*.png',
                    allowEmptyArchive: true
        }

        success {
            echo 'Pipeline executed successfully'
        }

        failure {
            echo 'Pipeline execution failed'
        }
    }
}