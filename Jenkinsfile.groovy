pipeline{
    agent any
    
    options{
        buildDiscarder(logRotator(numToKeepStr: '5', daysToKeepStr: '5'))
        timestamps()
    }
    environment{
        
        registry = "*********/python_demo"
        registryCredential = '*********'        
    }
    
    stages{
	   stage('Git Pull'){
          steps {
               echo 'Pull Dockerfile from git'
               checkout([$class: 'GitSCM', branches: [[name: '*/master']], userRemoteConfigs: [[url: '****************************']]])
            }
        }
        stage('Building image') {
          steps{
             script {
               dockerImage = docker.build registry + ":$BUILD_NUMBER"
        }
      }
    }
        stage('Deploy Image') {
          steps{
           script {
             docker.withRegistry( '', registryCredential ) {
             dockerImage.push()
          }
        }
      }
    }
 }
}    