kubectl_image = "nsready/kubectl:0.0.1"

def takeSnapshot(group, env){
    stage("Snapshot ${group} in ${env} env"){
        sh "docker rmi ${kubectl_image}"
        docker.image("${kubectl_image}").inside {
            set_kube_credentials(env)
            withCredentials([string(credentialsId: "${env}-kube-url", variable: "KUBE_SERVER_URL")]){
                sh "cd jenkins/scripts; python snapshot.py ${group}";
            }
        }
    }
}

def set_kube_credentials(env){
    withCredentials([string(credentialsId: "${env}-kube-ca", variable: "CA")]){
        sh "echo ${CA} >> /kube/ca.pem"
        def fu = sh(returnStdout: true, script: "cat /kube/ca.pem")
        println "ca: ${CA}"
    }
    withCredentials([string(credentialsId: "${env}-kube-cert", variable: "CERT")]){
        sh "echo ${CERT} >> /kube/admin.pem"
    }
    withCredentials([string(credentialsId: "${env}-kube-key", variable: "CERT-KEY")]){
        sh "echo ${CERT-KEY} >> /kube/admin-key.pem"
    }
}

return this;
