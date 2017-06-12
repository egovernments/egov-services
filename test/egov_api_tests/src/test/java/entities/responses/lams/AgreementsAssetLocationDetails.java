package entities.responses.lams;

public class AgreementsAssetLocationDetails {
    private Object zone;
    private Object street;
    private Object revenueWard;
    private String locality;
    private Object block;
    private String electionWard;

    public Object getZone() {
        return this.zone;
    }

    public void setZone(Object zone) {
        this.zone = zone;
    }

    public Object getStreet() {
        return this.street;
    }

    public void setStreet(Object street) {
        this.street = street;
    }

    public Object getRevenueWard() {
        return this.revenueWard;
    }

    public void setRevenueWard(Object revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getLocality() {
        return this.locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public Object getBlock() {
        return this.block;
    }

    public void setBlock(Object block) {
        this.block = block;
    }

    public String getElectionWard() {
        return this.electionWard;
    }

    public void setElectionWard(String electionWard) {
        this.electionWard = electionWard;
    }
}
