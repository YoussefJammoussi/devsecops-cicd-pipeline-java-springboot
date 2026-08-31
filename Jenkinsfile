pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                echo 'Checkout du code source...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Build de l application...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo 'Execution des tests...'
                sh 'mvn test'
            }
        }

    }
}
