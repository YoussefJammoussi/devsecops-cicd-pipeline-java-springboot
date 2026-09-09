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

        stage('Security Scan') {
            steps {
                echo 'Analyse de sécurité de l image avec Trivy...'

                dir('devsecops-app') {
                    sh '''
                        GIT_SHA=$(git rev-parse --short HEAD)
                        IMAGE_TAG=${BUILD_NUMBER}-${GIT_SHA}

                        trivy image \
                            --timeout 15m \
                            --severity HIGH,CRITICAL \
                            --exit-code 1 \
                            devsecops-app:${IMAGE_TAG}
                    '''
                }
            }
        }

        stage('Docker Push') {
            steps {
                echo 'Push de l image vers Nexus...'

                dir('devsecops-app') {
                    withCredentials([
                        usernamePassword(
                            credentialsId: 'nexus-credentials',
                            usernameVariable: 'NEXUS_USERNAME',
                            passwordVariable: 'NEXUS_PASSWORD'
                        )
                    ]) {
                        sh '''
                            GIT_SHA=$(git rev-parse --short HEAD)
                            IMAGE_TAG=${BUILD_NUMBER}-${GIT_SHA}

                            NEXUS_REGISTRY=192.168.38.136:8083
                            IMAGE_NAME=${NEXUS_REGISTRY}/devsecops-app:${IMAGE_TAG}

                            echo "$NEXUS_PASSWORD" | docker login "$NEXUS_REGISTRY" \
                                --username "$NEXUS_USERNAME" \
                                --password-stdin

                            docker tag devsecops-app:${IMAGE_TAG} ${IMAGE_NAME}

                            docker push ${IMAGE_NAME}

                            docker logout "$NEXUS_REGISTRY"
                        '''
                    }
                }
            }
        }
    }

    post {

        success {
            echo 'Pipeline executed successfully!'

            emailext(
                subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
Pipeline executed successfully.

Job: ${env.JOB_NAME}
Build: #${env.BUILD_NUMBER}
Status: SUCCESS

Jenkins URL:
${env.BUILD_URL}
""",
                to: "youssefjammoussi101@gmail.com"
            )
        }

        failure {
            echo 'Pipeline failed!'

            emailext(
                subject: "FAILURE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
Pipeline failed.

Job: ${env.JOB_NAME}
Build: #${env.BUILD_NUMBER}
Status: FAILURE

Jenkins URL:
${env.BUILD_URL}
""",
                to: "youssefjammoussi101@gmail.com"
            )
        }

        unstable {
            echo 'Pipeline is unstable!'

            emailext(
                subject: "UNSTABLE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
Pipeline is unstable.

Job: ${env.JOB_NAME}
Build: #${env.BUILD_NUMBER}
Status: UNSTABLE

Jenkins URL:
${env.BUILD_URL}
""",
                to: "youssefjammoussi101@gmail.com"
            )
        }
    }
}
