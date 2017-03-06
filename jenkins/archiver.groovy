def archiveArtifacts(artifacts){
    stage("Archive Results") {
        for (artifact in artifacts) {
            archive "${artifact}"
        }
    }
}

return this;
