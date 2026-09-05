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

    stage('SonarQube Analysis') {
        steps {
            echo 'Analyse du code avec SonarQube...'

            dir('devsecops-app') {
                withSonarQubeEnv('SonarQube') {
                    sh '''
                        mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                        -Dsonar.projectKey=devsecops-app \
                        -Dsonar.projectName=devsecops-app
                   
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Analyse du code avec SonarQube...'

                dir('devsecops-app') {
                    withSonarQubeEnv('SonarQube') {
                        sh '''
                            mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                            -Dsonar.projectKey=devsecops-app \
                            -Dsonar.projectName=devsecops-app
                        
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Construction de l image Docker...'

                dir('devsecops-app') {
                    sh '''
                        GIT_SHA=$(git rev-parse --short HEAD)
                        docker build -t devsecops-app:${BUILD_NUMBER}-${GIT_SHA} .
                    '''
                }
            }
        }
    }

    stage('Quality Gate') {
        steps {
            timeout(time: 5, unit: 'MINUTES') {
                waitForQualityGate abortPipeline: true
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

