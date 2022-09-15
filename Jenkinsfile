
pipeline {
    agent any
    tools{
        maven 'MAVEN_HOME'
    }
    stages {
        stage ('Build') {
            steps {
                    bat "mvn clean package"
            }
        }
        stage ('Test') {
            steps {
                   bat "mvn test"
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/TEST-*.xml'
                    archiveArtifacts artifacts: '**/target/*.jar'
                }
            }
        }

        stage ('Sonar-Scan') {
            steps {
                withSonarQubeEnv('sc'){
                    bat "mvn clean verify sonar:sonar \
                         -Dsonar.projectKey=employee-code \
                         -Dsonar.host.url=http://127.0.0.1:9000 \
                         -Dsonar.login=sqp_45da113e82af152533f09588c25775f862a3c8b2"
                }
            }
            }
      stage ('Jacoco-Report') {
            steps {
               bat "mvn jacoco:report"
            }
            }

        stage ('Deployment') {
            steps {
                 echo "deployment in progress......"
            }
        }


}
}