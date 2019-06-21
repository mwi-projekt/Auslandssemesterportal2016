pipeline {
    agent any

    stages {
    	stage('Configure Server URL') {
            steps {
                sh 'sed -i -e \'s#var baseUrl = "http://localhost:8080/Auslandssemesterportal";#var baseUrl = "http://10.3.15.45:8080/Auslandssemesterportal";#g\' src/main/webapp/WebContent/assets/js/app.js'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn install'
            }
        }
        stage('Deploy') { 
            steps {
                sh 'mv target/Auslandssemesterportal.war $MWI_DEPLOY' 
            }
        }
    }
}