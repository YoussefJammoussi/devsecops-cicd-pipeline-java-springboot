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

                dir('devsecops-app') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Test') {
            steps {
                echo 'Execution des tests...'

                dir('devsecops-app') {
                    sh 'mvn test'
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Code Coverage') {
            steps {
                echo 'Generation du rapport JaCoCo...'

                dir('devsecops-app') {
                    sh 'mvn jacoco:report'
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline executed successfully!'
        }

        failure {
            echo 'Pipeline failed!'
        }
    }
}
