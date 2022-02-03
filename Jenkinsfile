def get_kubernetes_student_cloud() {
  def cloudName;
  withFolderProperties {
    cloudName = env.CLOUD_ENV
  }
  return cloudName
}

pipeline {
    agent {
      kubernetes {
        cloud get_kubernetes_student_cloud()
        yamlFile 'agent/jenkins-kaniko-agent.yaml'
      }
    }
    stages {
        stage('Build Application') {
            steps {
                container(name: 'maven'){
                    sh 'mvn clean package'
                }
            }
        }
        stage('Code Analysis') {
            steps {
                withSonarQubeEnv(installationName: 'sonarQube-devops', credentialsId: 'sonar-todo-users') {
                    container('maven') {
                        sh 'mvn sonar:sonar'
                    }
                }
            }
        }
        stage('Build Image') {
            environment {
                DOCKER_AUTH = credentials('Docker-devops31')
                GIT_AUTH = credentials('deploy-token-todo-users')
            }
            steps {
                sh 'echo "Building the docker image"'
                container (name: 'kaniko') {
                    sh 'mkdir -p /kaniko/.docker'
                    sh '''
                        set +x
                        echo "{\\"auths\\":{\\"https://index.docker.io/v1/\\":{\\"auth\\":\\"$(echo -n $DOCKER_AUTH_USR:$DOCKER_AUTH_PSW | base64)\\"},\\"registry.devops-labs.it/students/devops31/todo-users\\":{\\"auth\\":\\"$(echo -n $GIT_AUTH_USR:$GIT_AUTH_PSW | base64)\\"}}}" > /kaniko/.docker/config.json
                        set -x
                    '''
                    sh 'cat /kaniko/.docker/config.json'
                    sh '/kaniko/executor --dockerfile Dockerfile --context `pwd` --destination registry.devops-labs.it/students/devops31/todo-users:version-${BUILD_NUMBER}'
                }
            }
        }
        stage('Tag Build') {
            environment {
                GIT_AUTH = credentials('jenkins-bot31')
            }
            steps {
                sh 'git config --local credential.helper "!f() { echo username=\\$GIT_AUTH_USR; echo password=\\$GIT_AUTH_PSW; }; f"'
                sh 'git tag build-${BUILD_NUMBER}'
                sh 'git push --tags'
            }
        }
        stage('Deploy to Staging') {
            steps {
                withCredentials([file(credentialsId:'aws-users-staging-access', variable:'FILE')]) {
                    sh 'chmod 400 $FILE'
                    sh 'cat deployScript.sh | sed "s/{VERSION}/${BUILD_NUMBER}/g" | sed "s/{PROFILE}/staging/g" > deploymentScript.sh'
                    sh 'cat -v deploymentScript.sh'
                    sh 'ssh -tt -i $FILE -o UserKnownHostsFile=known_hosts ec2-user@ec2-52-20-157-9.compute-1.amazonaws.com < deploymentScript.sh'
                }
            }
        }
        stage('Functional tests') {
            steps {
                sh 'echo "Here go steps to execute functional tests"'
            }
        }
        stage('Tag Release') {
            environment {
                GIT_AUTH = credentials('jenkins-bot31')
            }
            steps {
                sh 'git config --local credential.helper "!f() { echo username=\\$GIT_AUTH_USR; echo password=\\$GIT_AUTH_PSW; }; f"'
                sh 'git tag release-${BUILD_NUMBER}'
                sh 'git push --tags'
            }
        }
    }
}