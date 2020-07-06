pipeline {
    agent any
    
    /*environment {
        SONARQUBE_TOKEN = credentials('sonarqube')
    }*/
    
    stages {
    	stage('Configure Server URL') {
            steps {
                sh 'sed -i -e \'s#var baseUrl = "http://localhost:81";#var baseUrl = "http://10.3.15.45:81";#g\' src/main/webapp/js/app.ts'
                sh 'sed -i -e \'s#var baseUrl = "http://localhost:81";#var baseUrl = "http://10.3.15.45:81";#g\' src/main/webapp/js/file-browser.ts'
            }
        }
        /*stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh "mvn clean verify sonar:sonar -Dsonar.projectKey=jenkins-pipeline -Dsonar.host.url=http://localhost:22770 -Dsonar.login=$SONARQUBE_TOKEN"
                }
            }
        }*/
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
        stage("Dynamic Test"){
            steps {
                sh "docker exec Zap zap-cli --verbose quick-scan --self-contained --start-options '-config api.disablekey=true' http://http://10.3.15.45:80"
            }
        }
    }
}
