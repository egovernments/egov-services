package entities.responses.assetManagement.assetService;

public class LocationDetails {
    private String zone;
    private String street;
    private String revenueWard;
    private String pinCode;
    private int locality;
    private String block;
    private String doorNo;
    private int electionWard;

    public String getZone() {
        return this.zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getRevenueWard() {
        return this.revenueWard;
    }

    public void setRevenueWard(String revenueWard) {
        this.revenueWard = revenueWard;
    }

    public String getPinCode() {
        return this.pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public int getLocality() {
        return this.locality;
    }

    public void setLocality(int locality) {
        this.locality = locality;
    }

    public String getBlock() {
        return this.block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getDoorNo() {
        return this.doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public int getElectionWard() {
        return this.electionWard;
    }

    public void setElectionWard(int electionWard) {
        this.electionWard = electionWard;
    }
}
