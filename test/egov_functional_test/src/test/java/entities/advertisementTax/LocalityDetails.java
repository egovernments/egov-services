package entities.advertisementTax;

public class LocalityDetails {

    private String locality;
    private String localityAddress;
    private String electionWard;

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLocalityAddress() {
        return localityAddress;
    }

    public void setLocalityAddress(String localityAddress) {
        this.localityAddress = localityAddress;
    }

    public String getElectionWard() {
        return electionWard;
    }

    public void setElectionWard(String electionWard) {
        this.electionWard = electionWard;
    }
}
