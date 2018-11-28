pipeline {
    agent any
    stages {
        stage ('Checkout Project') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']],
                          doGenerateSubmoduleConfigurations: false,
                          extensions: [],
                          submoduleCfg: [],
                          userRemoteConfigs: [[credentialsId: '', url: 'https://github.com/acet/PubSubProducer']]])
            }
        }

        stage('Build Push Deploy') {
            steps{
                googleCloudBuild \
                    credentialsId: 'gcp-project-220211',
                    source: local('.'),
                    request: file('cloudbuild.yaml'),
                    substitutions: [
                        _BUILD_NUMBER: "$BUILD_NUMBER"
                    ]
            }
        }
    }
}