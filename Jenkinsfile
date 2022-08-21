pipeline {
    agent any

    stages {
        stage('Build') {
        tools {
                // Install the Maven version configured as "MAVEN_HOME" and add it to the path.
                maven "MAVEN_HOME"
            }
            steps {
            // Get some code from a GitHub repository
            git 'https://github.com/shanuchauhan94/graphql-ck-serviec.git'
                // To run Maven on a Windows agent,
                bat "mvn -Dmaven.test.failure.ignore=true clean package"
                echo "Build"
            }
             post {
                  // If Maven was able to run the tests, even if some of the test
                 // failed, record the test results and archive the jar file.
                    success {
                        junit '**/target/surefire-reports/TEST-*.xml'
                        archiveArtifacts 'target/*.jar'
                            }
                        }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}