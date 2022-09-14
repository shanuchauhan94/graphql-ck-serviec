
currentBuild.displayName=currentBuild.number+" process"

pipeline {
    agent any
    stages {

        stage ('Git Checkout') {
            steps {
                  git credentialsId: 'Jenkins-GitHub', url: 'https://github.com/shanuchauhan94/graphql-ck-serviec.git'
                }
            }
        stage ('Build') {
            steps {
                withMaven(maven : 'MAVEN_HOME') {
                    bat "mvn -Dmaven.test.failure.ignore=true clean compile"
                    echo "build user name -> $name"

                }
            }
        }

        stage ('Test') {

            steps {
                withMaven(maven : 'MAVEN_HOME') {
                   bat "mvn -Dmaven.test.failure.ignore=true test"
                }
            }
            post {
                always {
                Jacoco()
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/TEST-*.xml'
                }
            }

        }

        stage ('Sonar-Scan') {
            steps {
                withSonarQubeEnv('sc'){
                withMaven(maven : 'MAVEN_HOME') {
                    bat "mvn -Dmaven.test.failure.ignore=true clean verify sonar:sonar \
                         -Dsonar.projectKey=employee-code \
                         -Dsonar.host.url=http://127.0.0.1:9000 \
                         -Dsonar.login=sqp_45da113e82af152533f09588c25775f862a3c8b2"

                 }
                }
            }
            }

        stage ('Archiving') {
            steps {
                archiveArtifacts '**/target/*.jar'
        }
    }

        stage ('Deployment') {
            steps {
                 echo "deployment in progress......"
            }
        }


}
}