pipeline{
    agent any
    stages{
        stage('Checkout'){
            steps{
                git 'https://github.com/fengzse/first_maven.git'
            }
        }
        stage('Build'){
            steps{
                bat "mvn compile"
            }
        }
        stage('Test'){
            steps{
                bat "mvn test"
            }
            post{
                always{
                    junit '**/TEST*.xml'
                    emailext attachLog: true, attachmentsPattern: '**/TEST*.xml', body:'',
                    recipientProviders:[culprits()],
                    subject:'$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!'
                }
            }
        }
    }
}