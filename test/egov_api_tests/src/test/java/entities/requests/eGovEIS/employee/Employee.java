package entities.requests.eGovEIS.employee;

import org.codehaus.jackson.annotate.JsonProperty;

public class Employee {
    private String tenantId;

    private int motherTongue;

    private String gpfNo;

    private String passportNo;

    private int recruitmentType;

    private int[] jurisdictions;

    private Test[] test;

    private String retirementAge;

    private int employeeStatus;

    private Technical[] technical;

    private Education[] education;

    private String maritalStatus;

    private Probation[] probation;

    private boolean physicallyDisabled;

    private int recruitmentQuota;

    private int religion;

    private int[] languagesKnown;

    private String dateOfJoining;

    private int bank;

    private String dateOfResignation;

    private int employeeType;

    private String placeOfBirth;

    private boolean medicalReportProduced;

    private String dateOfTermination;

    private int community;

    private String code;
    private ServiceHistory[] serviceHistory;
    @JsonProperty("Assignments")
    private Assignments[] assignments;
    private Regularisation[] regularisation;
    private String dateOfRetirement;
    private int category;
    private int recruitmentMode;
    private int group;
    private String dateOfAppointment;
    @JsonProperty("User")
    private User user;
    private String bankAccount;
    private int bankBranch;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public int getMotherTongue() {
        return motherTongue;
    }

    public void setMotherTongue(int motherTongue) {
        this.motherTongue = motherTongue;
    }

    public String getGpfNo() {
        return gpfNo;
    }

    public void setGpfNo(String gpfNo) {
        this.gpfNo = gpfNo;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public int getRecruitmentType() {
        return recruitmentType;
    }

    public void setRecruitmentType(int recruitmentType) {
        this.recruitmentType = recruitmentType;
    }

    public int[] getJurisdictions() {
        return jurisdictions;
    }

    public void setJurisdictions(int[] jurisdictions) {
        this.jurisdictions = jurisdictions;
    }

    public Test[] getTest() {
        return test;
    }

    public void setTest(Test[] test) {
        this.test = test;
    }

    public String getRetirementAge() {
        return retirementAge;
    }

    public void setRetirementAge(String retirementAge) {
        this.retirementAge = retirementAge;
    }

    public int getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(int employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public Technical[] getTechnical() {
        return technical;
    }

    public void setTechnical(Technical[] technical) {
        this.technical = technical;
    }

    public Education[] getEducation() {
        return education;
    }

    public void setEducation(Education[] education) {
        this.education = education;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Probation[] getProbation() {
        return probation;
    }

    public void setProbation(Probation[] probation) {
        this.probation = probation;
    }

    public boolean getPhysicallyDisabled() {
        return physicallyDisabled;
    }

    public void setPhysicallyDisabled(boolean physicallyDisabled) {
        this.physicallyDisabled = physicallyDisabled;
    }

    public int getRecruitmentQuota() {
        return recruitmentQuota;
    }

    public void setRecruitmentQuota(int recruitmentQuota) {
        this.recruitmentQuota = recruitmentQuota;
    }

    public int getReligion() {
        return religion;
    }

    public void setReligion(int religion) {
        this.religion = religion;
    }

    public int[] getLanguagesKnown() {
        return languagesKnown;
    }

    public void setLanguagesKnown(int[] languagesKnown) {
        this.languagesKnown = languagesKnown;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }

    public String getDateOfResignation() {
        return dateOfResignation;
    }

    public void setDateOfResignation(String dateOfResignation) {
        this.dateOfResignation = dateOfResignation;
    }

    public int getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(int employeeType) {
        this.employeeType = employeeType;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public boolean getMedicalReportProduced() {
        return medicalReportProduced;
    }

    public void setMedicalReportProduced(boolean medicalReportProduced) {
        this.medicalReportProduced = medicalReportProduced;
    }

    public String getDateOfTermination() {
        return dateOfTermination;
    }

    public void setDateOfTermination(String dateOfTermination) {
        this.dateOfTermination = dateOfTermination;
    }

    public int getCommunity() {
        return community;
    }

    public void setCommunity(int community) {
        this.community = community;
    }

    public ServiceHistory[] getServiceHistory() {
        return serviceHistory;
    }

    public void setServiceHistory(ServiceHistory[] serviceHistory) {
        this.serviceHistory = serviceHistory;
    }

    public Assignments[] getAssignments() {
        return assignments;
    }

    public void setAssignments(Assignments[] assignments) {
        this.assignments = assignments;
    }

    public Regularisation[] getRegularisation() {
        return regularisation;
    }

    public void setRegularisation(Regularisation[] regularisation) {
        this.regularisation = regularisation;
    }

    public String getDateOfRetirement() {
        return dateOfRetirement;
    }

    public void setDateOfRetirement(String dateOfRetirement) {
        this.dateOfRetirement = dateOfRetirement;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getRecruitmentMode() {
        return recruitmentMode;
    }

    public void setRecruitmentMode(int recruitmentMode) {
        this.recruitmentMode = recruitmentMode;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(String dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public int getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(int bankBranch) {
        this.bankBranch = bankBranch;
    }
}
