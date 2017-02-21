def archiveArtifacts(path){
    stage("Archive Results") {
        archive "${path}/target/*.jar"
        archive "${path}/target/**/*.html"
    }
}

return this;
