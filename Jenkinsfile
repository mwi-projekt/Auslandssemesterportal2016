pipeline {
    agent any

    stages {
    	stage('Configure Server URL') {
            steps {
                sh 'sed -i -e \'s#var baseUrl = "http://localhost";#var baseUrl = "http://10.3.15.45";#g\' src/main/webapp/js/app.js'
                sh 'sed -i -e \'s#var baseUrl = "http://localhost";#var baseUrl = "http://10.3.15.45";#g\' src/main/webapp/js/file-browser.js'
            }
        }
        stage('SonarQube') {
            steps {
                withSonarQubeEnv(credentialsId: 'sonarqube', installationName: 'SonarQube') {
                    sh "mvn sonar:sonar -Dsonar.projectKey=jenkins-pipeline -Dsonar.host.url=http://localhost:22770 -Dsonar.login=8f7b2b5a643675feeb2562ee6825ebc8e96c99eb"
                }
            }
        }
        stage('Build') {
            steps {
                sh 'mvn install'
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
    }
}
