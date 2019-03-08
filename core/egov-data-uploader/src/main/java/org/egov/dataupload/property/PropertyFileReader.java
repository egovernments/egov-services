package org.egov.dataupload.property;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.egov.dataupload.property.models.Document;
import org.egov.dataupload.property.models.OwnerInfo;
import org.egov.dataupload.property.models.OwnerInfo.RelationshipEnum;
import org.egov.dataupload.property.models.Property;
import org.egov.dataupload.property.models.PropertyDetail;
import org.egov.dataupload.property.models.Unit;
import org.egov.dataupload.utils.Constants.PTOwnerDetails;
import org.egov.dataupload.utils.Constants.PTTemplateDetail;
import org.egov.dataupload.utils.Constants.PTUnitDetail;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PropertyFileReader {

	public Map<String, Sheet> readFile(String location) throws InvalidFormatException, IOException {
		Map<String, Sheet> sheetMap = new HashMap<>();

		// Creating a Workbook from an Excel file (.xls or .xlsx)
		Workbook workbook = WorkbookFactory.create(new File(location));

		// Retrieving the number of sheets in the Workbook
		log.info("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

		workbook.forEach(sheet -> {
			log.info("=> " + sheet.getSheetName());
			sheetMap.put(sheet.getSheetName(), sheet);
		});
		workbook.close();

		return sheetMap;
	}

	public Map<String, Property> parseExcel(String location) throws EncryptedDocumentException, InvalidFormatException, IOException {
		Map<String, Sheet> sheetMap = readFile(location);
		Map<String, Property> propertyIdMap = parsePropertyExcel(sheetMap);
		parseUnitDetail(sheetMap, propertyIdMap);
		parseOwnerDetail(sheetMap, propertyIdMap);
		
		return propertyIdMap;
		
	}
	public Map<String, Property>  parsePropertyExcel(Map<String, Sheet> sheetMap) {

		Sheet propertySheet = sheetMap.get("Property_Detail");

		Map<String, Property> propertyIdMap = new LinkedHashMap<>();
		Iterator<Row> rowIterator = propertySheet.rowIterator();
		int rowNumber = 0;
		while (rowIterator.hasNext()) {
			Property property = new Property();
			property.getPropertyDetails().get(0).setAdditionalDetails(new HashMap<String, Object>());
			Row row = rowIterator.next();
			
			if (rowNumber++ == 0)
				continue;

			log.info("Property_Detail, processing row number" + rowNumber);
			// Check the existing property id column (which is 2)
			int existPropertyId_cellIndex= PTTemplateDetail.Existing_Property_Id.ordinal();
			if((row.getCell(existPropertyId_cellIndex)!=null)&&StringUtils.isEmpty(row.getCell(existPropertyId_cellIndex).getStringCellValue())){
				break;
			}
			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				if (null != cell)
					setPropertyDetails(cell, property);
			}
			if(!StringUtils.isEmpty(property.getOldPropertyId())){
				if(null != propertyIdMap.get(property.getOldPropertyId())) {
					StringBuilder id = new StringBuilder();
					id.append("duplicate_").append(property.getOldPropertyId()).append("_").append(rowNumber);
					propertyIdMap.put(id.toString(), property);
				}
				else
				{
					propertyIdMap.put(property.getOldPropertyId(), property);
				}
			}
			else
				continue;

		}
		
		return propertyIdMap;
	}

	@SuppressWarnings("unchecked")
	private void setPropertyDetails(Cell cell, Property property) {
		switch (PTTemplateDetail.from(cell.getColumnIndex())) {
			case Tenant:
				property.setTenantId(cell.getStringCellValue());
				break;
			case City:
				cell.setCellType(CellType.STRING);
				if (!StringUtils.isEmpty(cell.getStringCellValue()))
					property.getAddress().setCity(cell.getStringCellValue());
					break;
			case Existing_Property_Id:
				property.setOldPropertyId(cell.getStringCellValue());
				break;
			case Financial_Year:
				property.getPropertyDetails().get(0).setFinancialYear(cell.getStringCellValue());
				break;
			case PropertyType:
				property.getPropertyDetails().get(0).setPropertyType(cell.getStringCellValue());
				break;
			case Property_Sub_Type:
				if (!StringUtils.isEmpty(cell.getStringCellValue()))
					property.getPropertyDetails().get(0).setPropertySubType(cell.getStringCellValue());
					break;
			case Is_your_property_height_above_36:
				Map<String, Object> addDetails = (Map<String, Object>) property.getPropertyDetails().get(0)
						.getAdditionalDetails();
				addDetails.put("heightAbove36Feet", cell.getBooleanCellValue());
				break;
			case Do_you_Store_Inflammable_material:
				Map<String, Object> addDetailsMap = (Map<String, Object>) property.getPropertyDetails().get(0)
						.getAdditionalDetails();
				addDetailsMap.put("inflammable", cell.getBooleanCellValue());
				break;
			case Usage_Category_Major:
				property.getPropertyDetails().get(0).setUsageCategoryMajor(cell.getStringCellValue());
				break;
			case Usage_Category_Minor:
				if (!StringUtils.isEmpty(cell.getStringCellValue()))
					property.getPropertyDetails().get(0).setUsageCategoryMinor(cell.getStringCellValue());
				else
					property.getPropertyDetails().get(0).setUsageCategoryMinor(null);
				break;
			// in case of shared property value should go to built up area else land area
			case Land_Area_Buildup_Area:
				if(cell.getNumericCellValue()==0) break;
				else if ("SHAREDPROPERTY".equalsIgnoreCase(property.getPropertyDetails().get(0).getPropertySubType()))
					property.getPropertyDetails().get(0).setBuildUpArea((float) cell.getNumericCellValue());
				else
					property.getPropertyDetails().get(0).setLandArea((float) cell.getNumericCellValue());
				break;
			case No_of_Floors:
				if(cell.getNumericCellValue()==0) break;
				property.getPropertyDetails().get(0).setNoOfFloors((long) cell.getNumericCellValue());
				break;
			case Ownership_Category:
				property.getPropertyDetails().get(0).setOwnershipCategory(cell.getStringCellValue());
				break;
			case Sub_Ownership_Category:
				property.getPropertyDetails().get(0).setSubOwnershipCategory(cell.getStringCellValue());
				break;
			case Locality:
				property.getAddress().getLocality().setCode(cell.getStringCellValue());
				break;
			case Door_No:
				if (!StringUtils.isEmpty(cell.getStringCellValue()))
					property.getAddress().setDoorNo(cell.getStringCellValue());
				break;
			case Building_Name:
				if (!StringUtils.isEmpty(cell.getStringCellValue()))
					property.getAddress().setBuildingName(cell.getStringCellValue());
				break;
			case Street_Name:
				if (!StringUtils.isEmpty(cell.getStringCellValue()))
					property.getAddress().setStreet(cell.getStringCellValue());
				break;
			case Pincode:
				if (!StringUtils.isEmpty(cell.getStringCellValue()))
					property.getAddress().setPincode(cell.getStringCellValue());
				break;

			default:
				break;
		}

		System.out.print("\t");
	}

	private void parseUnitDetail(Map<String, Sheet> sheetMap, Map<String, Property> propertyIdMap) {

		Sheet propertyUnitSheet = sheetMap.get("Unit_Detail");
		Iterator<Row> rowIterator = propertyUnitSheet.rowIterator();

		int rowNumber = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (rowNumber++ == 0)
				continue;

			log.info("Unit_Detail, processing row number" + rowNumber);
			// Break if now value in existing property ID
			int existingPTId_cellIndex=PTUnitDetail.Existing_Property_Id.ordinal();
			if((row.getCell(existingPTId_cellIndex)!=null)&&StringUtils.isEmpty(row.getCell(existingPTId_cellIndex).getStringCellValue())){
				break;
			}

			String propertyId = row.getCell(existingPTId_cellIndex).getStringCellValue();
			Property property = propertyIdMap.get(propertyId);

			if (null == property)
				continue;
			
			Unit unit = new Unit();

			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				if (null != cell)
					setUnitDetails(cell, unit);
			}
			property.getPropertyDetails().get(0).getUnits().add(unit);
		}
	}

	private void setUnitDetails(Cell cell, Unit unit) {

		switch (PTUnitDetail.from(cell.getColumnIndex())) {
			case Tenant:
				unit.setTenantId(cell.getStringCellValue());
				break;
			case Existing_Property_Id:
				break;
			case Floor_No:
				unit.setFloorNo(cell.getStringCellValue());
				break;
			case Unit_No:
				break;
			case Unit_Usage:
				unit.setUsageCategoryMajor(cell.getStringCellValue());
				break;
			case Unit_Sub_usage:
				if (!StringUtils.isEmpty(cell.getStringCellValue()))
					unit.setUsageCategoryMinor(cell.getStringCellValue());
				break;
			case Occupancy:
				unit.setOccupancyType(cell.getStringCellValue());
				break;
			case Built_up_area:
				if(cell.getNumericCellValue()==0) break;
				unit.setUnitArea((float) cell.getNumericCellValue());
				break;
			case Annual_rent:
				if(cell.getNumericCellValue()==0) break;
				unit.setArv(new BigDecimal(cell.getNumericCellValue(), MathContext.DECIMAL64));
				break;
			case UsageCategorySubMinor:
				if (!StringUtils.isEmpty(cell.getStringCellValue()))
					unit.setUsageCategorySubMinor(cell.getStringCellValue());
				break;
			case UsageCategoryDetail:
				if (!StringUtils.isEmpty(cell.getStringCellValue()))
					unit.setUsageCategoryDetail(cell.getStringCellValue());
				break;
			default:
				break;
		}

		System.out.print("\t");
	}

	private void parseOwnerDetail(Map<String, Sheet> sheetMap, Map<String, Property> propertyIdMap) {

		Sheet propertyUnitSheet = sheetMap.get("Owner_Detail");
		Iterator<Row> rowIterator = propertyUnitSheet.rowIterator();

		int rowNumber = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (rowNumber++ == 0)
				continue;

			log.info("Owner_Detail, processing row number" + rowNumber);

			// Checking if existing property id is blank, then break
			int existingPTId_cellIndex= PTOwnerDetails.Existing_Property_ID.ordinal();
			if((row.getCell(existingPTId_cellIndex)!=null)&&StringUtils.isEmpty(row.getCell(existingPTId_cellIndex).getStringCellValue())){
				break;
			}

			String propertyId = row.getCell(existingPTId_cellIndex).getStringCellValue();
			Property property = propertyIdMap.get(propertyId);
			String ownershipCategory=property.getPropertyDetails().get(0).getOwnershipCategory();
			if (null == property)
				continue;
			
			PropertyDetail dtl = property.getPropertyDetails().get(0);
			OwnerInfo owner = new OwnerInfo();
			Document ownerDoc = new Document();
			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				if (null != cell)
					setOwnerDetails(cell, owner, ownerDoc,ownershipCategory);
			}
			
			// setting documents to owner
			HashSet<Document> docs = new HashSet<>();
			docs.add(ownerDoc);
			owner.setDocuments(docs);
			
			/*
			 * Adding citizen info. 
			 * The first owner object encountered for a property will be
			 * set as primary owner
			 */
			if (CollectionUtils.isEmpty(dtl.getOwners())) dtl.setCitizenInfo(owner);
			dtl.getOwners().add(owner);
		}
	}

	private void setOwnerDetails(Cell cell, OwnerInfo ownerInfo, Document document,String ownershipCategory) {

		switch (PTOwnerDetails.from(cell.getColumnIndex())) {
			case Tenant:
				ownerInfo.setTenantId(cell.getStringCellValue());
				break;
			case Existing_Property_ID:
				break;
			case Name:
				ownerInfo.setName(cell.getStringCellValue());
				break;
			case MobileNumber:
				cell.setCellType(CellType.STRING);
				ownerInfo.setMobileNumber(cell.getStringCellValue());
				if(ownershipCategory.equals("INSTITUTIONALPRIVATE")||ownershipCategory.equals("INSTITUTIONALGOVERNMENT"))
				{
					ownerInfo.setAltContactNumber(cell.getStringCellValue());
				}
				break;
			case Father_Or_Husband_Name:
				ownerInfo.setFatherOrHusbandName(cell.getStringCellValue());
				break;
			case Relationship:
				ownerInfo.setRelationship(RelationshipEnum.fromValue(cell.getStringCellValue()));
				break;
			case PermanentAddress:
				if (!StringUtils.isEmpty(cell.getStringCellValue()))
					ownerInfo.setPermanentAddress(cell.getStringCellValue());
				break;
			case Owner_Type:
				if (!StringUtils.isEmpty(cell.getStringCellValue()))
					ownerInfo.setOwnerType(cell.getStringCellValue());
				break;
			case Id_Type:
				if (!StringUtils.isEmpty(cell.getStringCellValue()))
					document.setDocumentType(cell.getStringCellValue());
				break;
			case Document_No:
				if (!StringUtils.isEmpty(cell.getStringCellValue()))
					document.setDocumentUid(cell.getStringCellValue());
				break;
			case Email:
				if (!StringUtils.isEmpty(cell.getStringCellValue()))
					ownerInfo.setEmailId(cell.getStringCellValue());
				break;
			case Gender:
				if (!StringUtils.isEmpty(cell.getStringCellValue()))
					ownerInfo.setGender(cell.getStringCellValue());
				break;
		
			default:
				break;
		}

		System.out.print("\t");
	}
}
