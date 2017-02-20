def archive(service_name){
    stage("Archive Results") {
        archive "${service_name}/target/*.jar"
        archive "${service_name}/target/**/*.html"
    }
}

return this;
