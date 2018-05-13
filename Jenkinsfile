pipeline {
    agent any

    stages {
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn install'
            }
        }
        stage('Deploy') { 
            steps {
                sh 'mv target/Auslandssemesterportal.war /home/mwi/camunda-tomcat8-bpm7.5.0/server/apache-tomcat-8.0.24/webapps' 
            }
        }
    }
}