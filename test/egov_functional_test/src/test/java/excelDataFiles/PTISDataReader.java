package excelDataFiles;

import builders.dcReports.PTReportBuilder;
import builders.dcReports.VLTReportBuilder;
import builders.ptis.*;
import entities.dcReports.PTReport;
import entities.dcReports.VLTReport;
import entities.ptis.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PTISDataReader extends ExcelReader {

    Sheet propertyHeaderDetailsSheet;
    Sheet ownerDetailsSheet;
    Sheet addressDetailsSheet;
    Sheet assessmentDetailsSheet;
    Sheet amenitiesSheet;
    Sheet constructionTypeDetailsSheet;
    Sheet floorDetailsSheet;
    Sheet searchDetailsSheet;
    Sheet editAssessmentDetailsSheet;
    Sheet editFloorDetailsSheet;
    Sheet documentDetailsSheet;
    Sheet vltReportSheet;
    Sheet ptReportSheet;
    Sheet revisionPetitionDetailsSheet;
    Sheet hearingDetailsSheet;
    Sheet registrationDetailsSheet;
    Sheet demolitionDetailsSheet;
    Sheet bifurcationDetailsSheet;

    public PTISDataReader(String testData) {
        super(testData);
        propertyHeaderDetailsSheet = workbook.getSheet("propertyHeaderDetails");
        ownerDetailsSheet = workbook.getSheet("ownerDetails");
        addressDetailsSheet = workbook.getSheet("addressDetails");
        assessmentDetailsSheet = workbook.getSheet("assessmentDetails");
        amenitiesSheet = workbook.getSheet("amenities");
        constructionTypeDetailsSheet = workbook.getSheet("constructionTypeDetails");
        floorDetailsSheet = workbook.getSheet("floorDetails");
        searchDetailsSheet = workbook.getSheet("searchDetails");
        editAssessmentDetailsSheet = workbook.getSheet("editAssessmentDetails");
        editFloorDetailsSheet = workbook.getSheet("editFloorDetails");
        vltReportSheet = workbook.getSheet("vltReport");
        ptReportSheet = workbook.getSheet("ptReport");
        registrationDetailsSheet = workbook.getSheet("registrationDetails");
        revisionPetitionDetailsSheet = workbook.getSheet("revisionPetitionDetails");
        hearingDetailsSheet = workbook.getSheet("hearingDetails");
        documentDetailsSheet = workbook.getSheet("documentDetails");
        demolitionDetailsSheet = workbook.getSheet("demolitionDetails");
        bifurcationDetailsSheet = workbook.getSheet("bifurcationDetails");
    }

    public SearchDetails getSearchDetails(String searchId) {
        Row dataRow = readDataRow(searchDetailsSheet, searchId);
        SearchDetails searchDetails = new SearchDetails();

        switch (searchId) {
            case "searchWithAssessmentNumber":
                String assessmentNumber = convertNumericToString(searchDetailsSheet, dataRow, "searchValue");

                searchDetails = new SearchDetailsBuilder()
                        .withAssessmentNumber(assessmentNumber)
                        .build();

                break;
            case "searchWithMobileNumber":
                String mobileNumber = convertNumericToString(searchDetailsSheet, dataRow, "searchValue");

                searchDetails = new SearchDetailsBuilder()
                        .withMobileNumber(mobileNumber)
                        .build();

                break;

            case "searchWithDoorNumber":
                String doorNumber = getCellData(searchDetailsSheet, dataRow, "searchValue").getStringCellValue();

                searchDetails = new SearchDetailsBuilder()
                        .withDoorNumber(doorNumber)
                        .build();

                break;

            case "searchWithZoneAndWardNumber":
                String value = getCellData(searchDetailsSheet, dataRow, "searchValue").getStringCellValue();
                String[] values = value.split(";");
                String zone = values[0];
                String ward = values[1];

                String value1 = getCellData(searchDetailsSheet, dataRow, "searchValue2").getStringCellValue();
                String[] values1 = value1.split(";");
                String houseNo = values1[0];
                String ownerName = values1[1];

                searchDetails = new SearchDetailsBuilder()
                        .withZoneNumber(zone)
                        .withWardNumber(ward)
                        .withHouseNo(houseNo)
                        .withOwnerName(ownerName)
                        .build();

                break;

            case "searchWithOwnerName":

                String value2 = getCellData(searchDetailsSheet, dataRow, "searchValue").getStringCellValue();
                String[] values2 = value2.split(";");
                String location = values2[0];
                String ownerName1 = values2[1];

                searchDetails = new SearchDetailsBuilder()
                        .withLocation(location)
                        .withOwnerName(ownerName1)
                        .build();
                break;

            case "searchByDemand":

                String value3 = getCellData(searchDetailsSheet, dataRow, "searchValue").getStringCellValue();
                String[] values3 = value3.split(";");
                String From = values3[0];
                String To = values3[1];

                searchDetails = new SearchDetailsBuilder()
                        .withFrom(From)
                        .withTo(To)
                        .build();
                break;
        }
        return searchDetails;
    }

    public PropertyHeaderDetails getPropertyHeaderDetails(String propertyDetailsDataId) {
        Row dataRow = readDataRow(propertyHeaderDetailsSheet, propertyDetailsDataId);
        String propertyType = getCellData(propertyHeaderDetailsSheet, dataRow, "propertyType").getStringCellValue();
        String categoryOfOwnership = getCellData(propertyHeaderDetailsSheet, dataRow, "categoryOfOwnership").getStringCellValue();

        return new PropertyHeaderDetailsBuilder()
                .withPropertyType(propertyType)
                .withCategoryOfOwnership(categoryOfOwnership)
                .build();
    }

    public OwnerDetails getOwnerDetails(String ownerDetailsDataId) {
        Row dataRow = readDataRow(ownerDetailsSheet, ownerDetailsDataId);
        String mobileNumber = convertNumericToString(ownerDetailsSheet, dataRow, "mobileNumber");
        String ownerName = getCellData(ownerDetailsSheet, dataRow, "ownerName").getStringCellValue();
        String gender = getCellData(ownerDetailsSheet, dataRow, "gender").getStringCellValue();
        String emailAddress = getCellData(ownerDetailsSheet, dataRow, "emailAddress").getStringCellValue();
        String guardianRelation = getCellData(ownerDetailsSheet, dataRow, "guardianRelation").getStringCellValue();
        String guardian = getCellData(ownerDetailsSheet, dataRow, "guardian").getStringCellValue();
        return new OwnerDetailsBuilder()
                .withMobileNumber(mobileNumber)
                .withOwnerName(ownerName)
                .withGender(gender)
                .withEmailAddress(emailAddress)
                .withGuardianName(guardian)
                .withGuardianRelation(guardianRelation).build();
    }

    public PropertyAddressDetails getPropertyAddressDetails(String addressDetailsDataId) {

        Row dataRow = readDataRow(addressDetailsSheet, addressDetailsDataId);

        String locality = getCellData(addressDetailsSheet, dataRow, "locality").getStringCellValue();
        String zoneNumber = getCellData(addressDetailsSheet, dataRow, "zoneNumber").getStringCellValue();
        String electionWard = getCellData(addressDetailsSheet, dataRow, "electionWard").getStringCellValue();
        String pinCode = convertNumericToString(addressDetailsSheet, dataRow, "pincode");

        return new AddressDetailsBuilder()
                .withLocality(locality)
                .withZoneNumber(zoneNumber)
                .withElectionWard(electionWard)
                .withPincode(pinCode).build();
    }

    public AssessmentDetails getAssessmentDetails(String assessmentDetailsDataId) {
        Row dataRow = readDataRow(assessmentDetailsSheet, assessmentDetailsDataId);
        String reasonForCreation = getCellData(assessmentDetailsSheet, dataRow, "reasonForCreation").getStringCellValue();
        String extentOfSite = convertNumericToString(assessmentDetailsSheet, dataRow, "extentOfSite");
        String occupancyCertificateNumber = convertNumericToString(assessmentDetailsSheet, dataRow, "occupancyCertificateNumber");

        return new AssessmentDetailsBuilder().withReasonForCreation(reasonForCreation)
                .withExtentOfSite(extentOfSite)
                .withOccupancyCertificateNumber(occupancyCertificateNumber)
                .build();
    }

    public Amenities getAmenties(String amenitiesDataId) {
        Row dataRow = readDataRow(amenitiesSheet, amenitiesDataId);

        boolean lift = getCellData(amenitiesSheet, dataRow, "lift").getBooleanCellValue();
        boolean toilets = getCellData(amenitiesSheet, dataRow, "toilets").getBooleanCellValue();
        boolean waterTap = getCellData(amenitiesSheet, dataRow, "waterTap").getBooleanCellValue();
        boolean electricity = getCellData(amenitiesSheet, dataRow, "electricity").getBooleanCellValue();
        boolean attachedBathroom = getCellData(amenitiesSheet, dataRow, "attachedBathroom").getBooleanCellValue();
        boolean waterHarvesting = getCellData(amenitiesSheet, dataRow, "waterHarvesting").getBooleanCellValue();
        boolean cableConnection = getCellData(amenitiesSheet, dataRow, "cableConnection").getBooleanCellValue();

        return new AmenitiesBuilder()
                .hasLift(lift)
                .hasToilets(toilets)
                .hasAttachedBathroom(attachedBathroom)
                .hasElectricity(electricity)
                .hasWaterTap(waterTap)
                .hasWaterHarvesting(waterHarvesting)
                .hasCableConnection(cableConnection).build();
    }

    public ConstructionTypeDetails getConstructionTypeDetails(String constructionTypeDetailsDataId) {
        Row dataRow = readDataRow(constructionTypeDetailsSheet, constructionTypeDetailsDataId);

        String floorType = getCellData(constructionTypeDetailsSheet, dataRow, "floorType").getStringCellValue();
        String roofType = getCellData(constructionTypeDetailsSheet, dataRow, "roofType").getStringCellValue();
        String woodType = getCellData(constructionTypeDetailsSheet, dataRow, "woodType").getStringCellValue();
        String wallType = getCellData(constructionTypeDetailsSheet, dataRow, "wallType").getStringCellValue();

        return new ConstructionTypeDetailsBuilder().withFloorType(floorType)
                .withRoofType(roofType)
                .withWallType(wallType)
                .withWoodType(woodType).build();
    }

    public FloorDetails getFloorDetails(String floorDetailsDataId) {
        Row dataRow = readDataRow(floorDetailsSheet, floorDetailsDataId);

        String floorNumber = getCellData(floorDetailsSheet, dataRow, "floorNumber").getStringCellValue();
        String classificationOfBuilding = getCellData(floorDetailsSheet, dataRow, "classificationOfBuilding").getStringCellValue();
        String natureOfUsage = getCellData(floorDetailsSheet, dataRow, "natureOfUsage").getStringCellValue();
        String firmName = getCellData(floorDetailsSheet, dataRow, "firmName").getStringCellValue();
        String occupancy = getCellData(floorDetailsSheet, dataRow, "occupancy").getStringCellValue();
        String occupantName = getCellData(floorDetailsSheet, dataRow, "occupantName").getStringCellValue();
        Date constructionDate = getCellData(floorDetailsSheet, dataRow, "constructionDate").getDateCellValue();
        Date effectiveFromDate = getCellData(floorDetailsSheet, dataRow, "effectiveFromDate").getDateCellValue();
        String unstructuredLand = getCellData(floorDetailsSheet, dataRow, "unstructuredLand").getStringCellValue();
        String length = convertNumericToString(floorDetailsSheet, dataRow, "length");
        String breadth = convertNumericToString(floorDetailsSheet, dataRow, "breadth");
        String buildingPermissionNumber = convertNumericToString(floorDetailsSheet, dataRow, "buildingPermissionNumber");
        Date buildingPermissionDate = getCellData(floorDetailsSheet, dataRow, "buildingPermissionDate").getDateCellValue();
        String plinthAreaInBuildingPlan = convertNumericToString(floorDetailsSheet, dataRow, "plinthAreaInBuildingPlan");


        return new FloorDetailsBuilder().withFloorNumber(floorNumber)
                .withClassificationOfBuilding(classificationOfBuilding)
                .withNatureOfUsage(natureOfUsage)
                .withFirmName(firmName)
                .withOccupancy(occupancy)
                .withOccupantName(occupantName)
                .withConstructionDate(new SimpleDateFormat("dd/MM/yy").format(constructionDate))
                .withEffectiveFromDate(new SimpleDateFormat("dd/MM/yy").format(effectiveFromDate))
                .withUnstructuredLand(unstructuredLand)
                .withLength(length)
                .withBreadth(breadth)
                .withBuildingPermissionNumber(buildingPermissionNumber)
                .withBuildingPermissionDate(new SimpleDateFormat("dd/MM/yy").format(buildingPermissionDate))
                .withPlinthAreaInBuildingPlan(plinthAreaInBuildingPlan)
                .build();
    }

    public DocumentTypeValue getDocumentValue(String documentSelect) {
        Row dataRow = readDataRow(documentDetailsSheet, documentSelect);

        String documentType = getCellData(documentDetailsSheet, dataRow, "documentType").getStringCellValue();
        String deedNo = getCellData(documentDetailsSheet, dataRow, "deedNo").getStringCellValue();
        String deedDate = getCellData(documentDetailsSheet, dataRow, "deedDate").getStringCellValue();

        return new DocumentDetailsBuilder()
                .withdocumentType(documentType)
                .withDeedNo(deedNo)
                .withDeedDate(deedDate)
                .build();
    }

    public EditAssessmentDetails getEditAssessmentDetails(String assessmentDetailsDataName) {
        Row dataRow = readDataRow(editAssessmentDetailsSheet, assessmentDetailsDataName);
        String extentOfSite = convertNumericToString(editAssessmentDetailsSheet, dataRow, "extentOfSite");
        String occupancyCertificateNumber = convertNumericToString(editAssessmentDetailsSheet, dataRow, "occupancyCertificateNumber");

        return new EditAssessmentDetailsBuilder()
                .withExtentOfSite(extentOfSite)
                .withOccupancyCertificateNumber(occupancyCertificateNumber)
                .build();
    }

    public EditFloorDetails getEditFloorDetails(String floordetailsDataName) {
        Row dataRow = readDataRow(editFloorDetailsSheet, floordetailsDataName);
        String editfloorNumber = convertNumericToString(editFloorDetailsSheet, dataRow, "editfloorNumber");
        String editclassificationOfBuilding = convertNumericToString(editFloorDetailsSheet, dataRow, "editclassificationOfBuilding");
        String editnatureOfUsage = convertNumericToString(editFloorDetailsSheet, dataRow, "editnatureOfUsage");
        String editoccupancy = convertNumericToString(editFloorDetailsSheet, dataRow, "editoccupancy");
        String editoccupantName = convertNumericToString(editFloorDetailsSheet, dataRow, "editoccupantName");
        String editconstructionDate = convertNumericToString(editFloorDetailsSheet, dataRow, "editconstructionDate");
        String editeffectiveFromDate = convertNumericToString(editFloorDetailsSheet, dataRow, "editeffectiveFromDate");
        String editunstructuredLand = convertNumericToString(editFloorDetailsSheet, dataRow, "editunstructuredLand");
        String editlength = convertNumericToString(editFloorDetailsSheet, dataRow, "editlength");
        String editbreadth = convertNumericToString(editFloorDetailsSheet, dataRow, "editbreadth");
        String editbuildingPermissionNumber = convertNumericToString(editFloorDetailsSheet, dataRow, "editbuildingPermissionNumber");
        String editbuildingPermissionDate = convertNumericToString(editFloorDetailsSheet, dataRow, "editbuildingPermissionDate");
        String editplinthAreaInBuildingPlan = convertNumericToString(editFloorDetailsSheet, dataRow, "editplinthAreaInBuildingPlan");

        return new EditFloorDetailsBuilder()
                .withEditFloorNumber(editfloorNumber)
                .withEditclassificationOfBuilding(editclassificationOfBuilding)
                .withEditnatureOfUsage(editnatureOfUsage)
                .withEditoccupancy(editoccupancy)
                .withEditoccupantName(editoccupantName)
                .withEditconstructionDate(editconstructionDate)
                .withEditeffectiveFromDate(editeffectiveFromDate)
                .withEditunstructuredLand(editunstructuredLand)
                .withEditlength(editlength)
                .withEditbreadth(editbreadth)
                .withEditbuildingPermissionNumber(editbuildingPermissionNumber)
                .withEditbuildingPermissionDate(editbuildingPermissionDate)
                .withEditplinthAreaInBuildingPlan(editplinthAreaInBuildingPlan)
                .build();
    }

    public VLTReport getVLTReportInfo(String vltReport) {

        Row dataRow = readDataRow(vltReportSheet, vltReport);
        String fromDate = convertNumericToString(vltReportSheet, dataRow, "fromDate");
        String toDate = convertNumericToString(vltReportSheet, dataRow, "toDate");

        return new VLTReportBuilder()
                .withFromDate(fromDate)
                .withToDate(toDate)
                .build();
    }

    public PTReport getPTReportInfo(String ptReport) {

        Row dataRow = readDataRow(ptReportSheet, ptReport);
        String fromDate = convertNumericToString(ptReportSheet, dataRow, "fromDate");
        String toDate = convertNumericToString(ptReportSheet, dataRow, "toDate");

        return new PTReportBuilder()
                .withFromDate(fromDate)
                .withToDate(toDate)
                .build();
    }

    public RegistrationDetails getRegistrationDetails(String registrationDetailsDataId) {
        Row dataRow = readDataRow(registrationDetailsSheet, registrationDetailsDataId);

        String sellerExecutantName = convertNumericToString(registrationDetailsSheet, dataRow, "sellerExecutantName");
        String buyerClaimantName = convertNumericToString(registrationDetailsSheet, dataRow, "buyerClaimantName");
        String doorNo = convertNumericToString(registrationDetailsSheet, dataRow, "doorNo");
        String propertyAddress = convertNumericToString(registrationDetailsSheet, dataRow, "propertyAddress");
        String registeredPlotArea = convertNumericToString(registrationDetailsSheet, dataRow, "registeredPlotArea");
        String registeredPlinthArea = convertNumericToString(registrationDetailsSheet, dataRow, "registerPlinthArea");
        String eastBoundary = convertNumericToString(registrationDetailsSheet, dataRow, "eastBoundary");
        String westBoundary = convertNumericToString(registrationDetailsSheet, dataRow, "westBoundary");
        String northBoundary = convertNumericToString(registrationDetailsSheet, dataRow, "northBoundary");
        String southBoundary = convertNumericToString(registrationDetailsSheet, dataRow, "southBoundary");
        String sroName = convertNumericToString(registrationDetailsSheet, dataRow, "sroName");
        String reasonForChange = convertNumericToString(registrationDetailsSheet, dataRow, "reasonForChange");
        String registrationDocumentNumber = convertNumericToString(registrationDetailsSheet, dataRow, "registrationDocumentNumber");
        String registrationDocumentDate = convertNumericToString(registrationDetailsSheet, dataRow, "registrationDocumentDate");
        String partiesConsiderationValue = convertNumericToString(registrationDetailsSheet, dataRow, "partiesConsiderationValue");
        String departmentGuidelinesValue = convertNumericToString(registrationDetailsSheet, dataRow, "departmentGuide");

        return new RegistrationDetailsBuilder()
                .withSellerExecutantName(sellerExecutantName)
                .withBuyerClaimantName(buyerClaimantName)
                .withDoorNo(doorNo)
                .withPropertyAddress(propertyAddress)
                .withRegisteredPlotArea(registeredPlotArea)
                .withRegisteredPlinthArea(registeredPlinthArea)
                .withEastBoundary(eastBoundary)
                .withWestBoundary(westBoundary)
                .withNorthBoundary(northBoundary)
                .withSouthBoundary(southBoundary)
                .withSroName(sroName)
                .withReasonForChange(reasonForChange)
                .withRegistrationDocumentNumber(registrationDocumentNumber)
                .withRegistrationDocumentDate(registrationDocumentDate)
                .withPartiesConsiderationValue(partiesConsiderationValue)
                .withdePartmentGuidelinesValue(departmentGuidelinesValue)
                .build();
    }

    public RevisionPetitionDetails getRevisionPetitionDetails(String revisionPetitionDataId) {
        Row dataRow = readDataRow(revisionPetitionDetailsSheet, revisionPetitionDataId);
        String revisionPetitionDetails = convertNumericToString(revisionPetitionDetailsSheet, dataRow, "revisionPetitionDetails");

        return new RevisionPetitionDetailsBuilder()
                .withRevisionPetitionDetail(revisionPetitionDetails)
                .build();
    }

    public HearingDetails getHearingDetails(String hearingDataId) {
        Row dataRow = readDataRow(hearingDetailsSheet, hearingDataId);
        String hearingDate = convertNumericToString(hearingDetailsSheet, dataRow, "hearingDate");
        String hearingTime = convertNumericToString(hearingDetailsSheet, dataRow, "hearingTime");
        String venue = getCellData(hearingDetailsSheet, dataRow, "venue").getStringCellValue();

        return new HearingDetailsBuilder()
                .withHearingDate(hearingDate)
                .withHearingTime(hearingTime)
                .withvenue(venue)
                .build();
    }

    public DemolitionDetail getDemolitionDetails(String demolitionDataId) {
        Row dataRow = readDataRow(demolitionDetailsSheet, demolitionDataId);
        String reasonForDemolition = getCellData(demolitionDetailsSheet, dataRow, "reasonForDemolition").getStringCellValue();
        String surveyNumber = convertNumericToString(demolitionDetailsSheet, dataRow, "surveyNumber");
        String pattaNumber = convertNumericToString(demolitionDetailsSheet, dataRow, "pattaNumber");
        String marketValue = convertNumericToString(demolitionDetailsSheet, dataRow, "marketValue");
        String capitalValue = convertNumericToString(demolitionDetailsSheet, dataRow, "capitalValue");
        String North = getCellData(demolitionDetailsSheet, dataRow, "North").getStringCellValue();
        String East = getCellData(demolitionDetailsSheet, dataRow, "East").getStringCellValue();
        String West = getCellData(demolitionDetailsSheet, dataRow, "East").getStringCellValue();
        String South = getCellData(demolitionDetailsSheet, dataRow, "South").getStringCellValue();

        return new DemolitionDetailsBuilder()
                .withReasonForDemolition(reasonForDemolition)
                .withSurveyNumber(surveyNumber)
                .withPattaNumber(pattaNumber)
                .withMarketValue(marketValue)
                .withCaptialValue(capitalValue)
                .withNorth(North)
                .withEast(East)
                .withWest(West)
                .withSouth(South)
                .build();

    }

    public AssessmentDetails getbifurcationDetails(String bifurcationDetailsDataId) {
        Row dataRow = readDataRow(bifurcationDetailsSheet, bifurcationDetailsDataId);
        String bifurcationreasonForCreation = getCellData(bifurcationDetailsSheet, dataRow, "reasonForCreation").getStringCellValue();
        String parentAssessmentNo = convertNumericToString(bifurcationDetailsSheet, dataRow, "parentAssessmentNo");
        String extentOfSite = convertNumericToString(bifurcationDetailsSheet, dataRow, "extentOfSite");
        String occupancyCertificateNumber = convertNumericToString(bifurcationDetailsSheet, dataRow, "occupancyCertificateNumber");

        return new AssessmentDetailsBuilder()
                .withBifurcationReasonForcreation(bifurcationreasonForCreation)
                .withParentAssessmentNo(parentAssessmentNo)
                .withExtentOfSite(extentOfSite)
                .withOccupancyCertificateNumber(occupancyCertificateNumber)
                .build();

    }
}
