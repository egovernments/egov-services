def archiveArtifacts(artifacts){
    stage("Archive Results") {
        for (artifact in artifcats) {
            archive "${artifact}"
        }
    }
}

return this;
