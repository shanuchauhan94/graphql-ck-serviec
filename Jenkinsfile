pipeline {
    agent any

    stages {
        stage ('Build') {
            steps {
                withMaven(maven : 'MAVEN_HOME') {
                    bat "mvn -Dmaven.test.failure.ignore=true clean compile"

                }
            }
        }

        stage ('Testing') {

            steps {
                withMaven(maven : 'MAVEN_HOME') {
                   bat "mvn -Dmaven.test.failure.ignore=true test"
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/TEST-*.xml'
                }
            }

        }


        stage ('Deployment') {
            steps {
                withMaven(maven : 'MAVEN_HOME') {
                    bat "mvn -Dmaven.test.failure.ignore=true verify"
                }
            }
        }


        stage ('Archiving') {
            steps {
                archiveArtifacts '**/target/*.jar'
        }
    }
}
}