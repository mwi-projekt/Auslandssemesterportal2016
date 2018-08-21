pipeline {
    agent any

    stages {
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