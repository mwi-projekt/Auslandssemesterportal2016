pipeline {
    agent any

    stages {
    	stage('Configure Server URL') {
            steps {
                sh 'sed -i -e \'s#export const baseUrl = "http://localhost";#export const baseUrl = "http://10.3.15.45";#g\' src/main/webapp/js/config.js'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean install'
                sh 'cp target/Auslandssemesterportal.war docker/mwi/'
            }
        }
        stage('Build Docker') {
            steps {
                sh 'cp testserver.env .env'
                sh 'docker-compose build'
            }
        }
        stage('Deploy Docker') {
            steps {
                sh 'docker-compose up -d'
            }
        }
        stage('Wait until fully deployed') {
            steps {
                sleep 90
            }
        }
        stage('Run Integration Tests') {
            steps {
                sh 'mvn failsafe:integration-test -PintegrationsTests'
            }
            post {always {junit 'target/failsafe-reports/*.xml'}}
        }
    }
}