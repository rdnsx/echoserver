pipeline {
    agent any
    
    environment {
        DOMAIN = 'echoserver'
        STACK_NAME = 'echordnsxde'

        DOCKER_HUB_CREDENTIALS = 'DockerHub'
        TAG_NAME = 'latest'
        SSH_USER = 'root'
        SSH_HOST = '91.107.199.72'
        SSH_PORT = '22'

        SOURCE_REPO_URL = "https://github.com/rdnsx/${DOMAIN}.git"
        DOCKER_IMAGE_NAME = "rdnsx/${STACK_NAME}"
    }
      
        stage('Deploy to Swarm') {
            steps {
                script {
                    sshagent(['Swarm00']) {
                        sh """
                            ssh -o StrictHostKeyChecking=no -p ${SSH_PORT} ${SSH_USER}@${SSH_HOST} '
                            mount -a &&
                            cd /mnt/SSS/DockerCompose/ &&
                            rm -rf ${DOMAIN}/ &&
                            mkdir ${DOMAIN}/ &&
                            cd ${DOMAIN}/ &&
                            curl -o docker-compose-swarm.yml https://raw.githubusercontent.com/rdnsx/${DOMAIN}/main/docker-compose-swarm.yml &&
                            docker stack deploy -c docker-compose-swarm.yml ${STACK_NAME};'
                        """
                    }
                }
            }
        }
    }
}