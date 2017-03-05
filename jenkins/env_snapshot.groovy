def takeSnapshot(group, env, ci_image){
    stage("Snapshot ${group} in ${env} env"){
        sh "docker rmi egovio/ci:0.0.3"
        docker.image("${ci_image}").inside {
            set_kube_credentials(env)
            withCredentials([string(credentialsId: "${env}-kube-url", variable: "KUBE_SERVER_URL")]){
                sh "cd jenkins/scripts; python snapshot.py ${group}";
            }
        }
    }
}

def set_kube_credentials(env){
    sh "rm -rf /kube; mkdir -p /kube"
    withCredentials([string(credentialsId: "${env}-kube-ca", variable: "CA")]){
        sh "echo ${CA} >> /kube/ca.pem"
    }
    withCredentials([string(credentialsId: "${env}-kube-cert", variable: "CERT")]){
        sh "echo ${CERT} >> /kube/admin.pem"
    }
    withCredentials([string(credentialsId: "${env}-kube-key", variable: "CERT-KEY")]){
        sh "echo ${CERT-KEY} >> /kube/admin-key.pem"
    }
}

return this;
