kubectl_image = "nsready/kubectl:0.0.1"

def takeSnapshot(group, env){
    stage("Snapshot ${group} in ${env} env"){
        sh "docker rmi ${kubectl_image}"
        docker.image("${kubectl_image}").inside {
            set_kube_credentials(env)
            withCredentials([string(credentialsId: "${env}-kube-url", variable: "KUBE_SERVER_URL")]){
                sh "kubectl config set-cluster env --server ${KUBE_SERVER_URL}"
            }
            sh "python jenkins/scripts/snapshot.py ${group}";
        }
    }
}

def set_kube_credentials(env){
    withCredentials([file(credentialsId: "${env}-kube-ca", variable: "CA")]){
        sh "cp ${CA} /kube/ca.pem"
    }
    withCredentials([file(credentialsId: "${env}-kube-cert", variable: "CERT")]){
        sh "cp ${CERT} /kube/admin.pem"
    }
    withCredentials([file(credentialsId: "${env}-kube-key", variable: "CERT_KEY")]){
        sh "cp ${CERT_KEY} /kube/admin-key.pem"
    }
}

return this;
