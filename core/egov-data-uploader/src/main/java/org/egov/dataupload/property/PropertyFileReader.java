package org.egov.dataupload.property;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
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
import org.egov.dataupload.property.models.OwnerInfo;
import org.egov.dataupload.property.models.OwnerInfo.RelationshipEnum;
import org.egov.dataupload.property.models.Property;
import org.egov.dataupload.property.models.PropertyDetail;
import org.egov.dataupload.property.models.Unit;
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
			Row row = rowIterator.next();
			
			if (rowNumber++ == 0)
				continue;

			if (row.getCell(0).getStringCellValue() == null || row.getCell(0).getStringCellValue().isEmpty())
				break;

			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				setPropertyDetails(cell, property);
			}
			propertyIdMap.put(property.getOldPropertyId(), property);
		}
		
		return propertyIdMap;
	}

	private void setPropertyDetails(Cell cell, Property property) {
		switch (cell.getColumnIndex()) {
		case 0:
			property.setTenantId(cell.getStringCellValue());
			break;
		case 1:
			property.setOldPropertyId(cell.getStringCellValue());
			break;
		case 2:
			property.getPropertyDetails().get(0).setFinancialYear(cell.getStringCellValue());
			break;
		case 3:
			property.getPropertyDetails().get(0).setPropertyType(cell.getStringCellValue());
			break;
		case 4:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				property.getPropertyDetails().get(0).setPropertySubType(cell.getStringCellValue());
			break;
		case 5:
			property.getPropertyDetails().get(0).setUsageCategoryMajor(cell.getStringCellValue());
			break;
		case 6:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				property.getPropertyDetails().get(0).setUsageCategoryMinor(cell.getStringCellValue());
			break;
		case 7:
			property.getPropertyDetails().get(0).setLandArea((float) cell.getNumericCellValue());
			break;
		case 8:
			property.getPropertyDetails().get(0).setNoOfFloors((long) cell.getNumericCellValue());
			break;
		case 9:
			property.getPropertyDetails().get(0).setOwnershipCategory(cell.getStringCellValue());
			break;
		case 10:
			property.getPropertyDetails().get(0).setSubOwnershipCategory(cell.getStringCellValue());
			break;
		case 11:
			property.getAddress().getLocality().setCode(cell.getStringCellValue());
			break;
		case 12:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				property.getAddress().setDoorNo(cell.getStringCellValue());
			break;
		case 13:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				property.getAddress().setBuildingName(cell.getStringCellValue());
			break;
		case 14:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				property.getAddress().setStreet(cell.getStringCellValue());
			break;
		case 15:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				property.getAddress().setPincode(cell.getStringCellValue());
			break;
		case 19:
			cell.setCellType(CellType.STRING);
			property.getAddress().setCity(cell.getStringCellValue());

			break;
		default:
			System.out.print("");
		}

		System.out.print("\t");
	}

	private void parseUnitDetail(Map<String, Sheet> sheetMap, Map<String, Property> propertyIdMap) {

		Sheet propertyUnitSheet = sheetMap.get("Unit-Detail");
		Iterator<Row> rowIterator = propertyUnitSheet.rowIterator();

		int rowNumber = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (rowNumber++ == 0)
				continue;

			if (row.getCell(0).getStringCellValue() == null || row.getCell(0).getStringCellValue().isEmpty())
				break;

			String propertyId = row.getCell(1).getStringCellValue();
			Property property = propertyIdMap.get(propertyId);
			Unit unit = new Unit();

			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				setUnitDetails(cell, unit);
			}
			property.getPropertyDetails().get(0).getUnits().add(unit);
		}
	}

	private void setUnitDetails(Cell cell, Unit unit) {

		switch (cell.getColumnIndex()) {
		case 0:
			unit.setTenantId(cell.getStringCellValue());
			break;
		case 1:
			break;
		case 2:
			unit.setFloorNo(String.valueOf((int)cell.getNumericCellValue()));
			break;
		case 3:
			unit.setUsageCategoryMajor(cell.getStringCellValue());
			break;
		case 4:
			unit.setOccupancyType(cell.getStringCellValue());
			break;
		case 5:
			unit.setUnitArea((float) cell.getNumericCellValue());
			break;
		case 6:
			unit.setArv(new BigDecimal(cell.getNumericCellValue(), MathContext.DECIMAL64));
			break;
		case 7:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				unit.setUsageCategoryMinor(cell.getStringCellValue());
			break;
		case 8:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				unit.setUsageCategorySubMinor(cell.getStringCellValue());
			break;
		case 9:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				unit.setUsageCategoryDetail(cell.getStringCellValue());
			break;
		default:
			log.info("In default case of unit switch");
		}

		System.out.print("\t");
	}

	private void parseOwnerDetail(Map<String, Sheet> sheetMap, Map<String, Property> propertyIdMap) {

		Sheet propertyUnitSheet = sheetMap.get("Owner-Detail");
		Iterator<Row> rowIterator = propertyUnitSheet.rowIterator();

		int rowNumber = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (rowNumber++ == 0)
				continue;

			if (row.getCell(0).getStringCellValue() == null || row.getCell(0).getStringCellValue().isEmpty())
				break;

			String propertyId = row.getCell(1).getStringCellValue();
			Property property = propertyIdMap.get(propertyId);
			PropertyDetail dtl = property.getPropertyDetails().get(0);
			OwnerInfo owner = new OwnerInfo();

			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				setOwnerDetails(cell, owner);
			}
			
			/*
			 * Adding citizen info. 
			 * The first owner object encountered for a property will be
			 * set as primary owner
			 */
			if (CollectionUtils.isEmpty(dtl.getOwners())) dtl.setCitizenInfo(owner);
			dtl.getOwners().add(owner);
		}
	}

	private void setOwnerDetails(Cell cell, OwnerInfo ownerInfo) {

		switch (cell.getColumnIndex()) {
		case 0:
			ownerInfo.setTenantId(cell.getStringCellValue());
			break;
		case 1:
			break;
		case 2:
			ownerInfo.setName(cell.getStringCellValue());
			break;
		case 3:
			cell.setCellType(CellType.STRING);
			ownerInfo.setMobileNumber(cell.getStringCellValue());
			break;
		case 4:
			ownerInfo.setFatherOrHusbandName(cell.getStringCellValue());
			break;
		case 5:
			ownerInfo.setRelationship(RelationshipEnum.fromValue(cell.getStringCellValue()));
			break;
		case 6:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				ownerInfo.setPermanentAddress(cell.getStringCellValue());
			break;
		case 7:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				ownerInfo.setEmailId(cell.getStringCellValue());
			break;
		case 8:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				ownerInfo.setOwnerType(cell.getStringCellValue());
			break;
		case 9:
			if (!StringUtils.isEmpty(cell.getStringCellValue()))
				ownerInfo.setGender(cell.getStringCellValue());
			break;
		default:
			log.info(" default case in owner details setter");
		}

		System.out.print("\t");
	}
}
