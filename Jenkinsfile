pipeline {
    agent any

    stages {
    	stage('Configure Server URL') {
            steps {
                sh 'sed -i -e \'s#var baseUrl = "http://localhost:8080/Auslandssemesterportal";#var baseUrl = "http://10.3.15.45";#g\' src/main/webapp/assets/js/app.js'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn install'
            }
        }
        stage('Build Docker MWI') {
            steps {
                sh 'docker build -t mwi docker/mwi/.'
            }
        }
        stage('Build Docker Nginx') {
            steps {
                sh 'docker build -t mwi-web docker/web/.'
            }
        }
        stage('Deploy Docker') {
            steps {
                sh 'docker-compose up -d'
            }
        }
    }
}