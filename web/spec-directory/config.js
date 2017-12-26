

// const data = 
// [
// "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yaml"	
// // "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/asset/contracts/v1-0-0.yml"
// ]
			
const data = 
{
	"swm" :
	{
		"isSpecificHeader": false,
		"url": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yaml",
		"masters":
		[
			{
				"masterName": "VehicleType",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yml#/definitions/VehicleType",
				"uniqueKeys": [
					"code",
					"tenantId",
					"name"
				]
			},
			{
				"masterName": "FuelType",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yml#/definitions/FuelType",
				"uniqueKeys": [
					"code",
					"tenantId"
				]
			},
			{
				"masterName": "OilCompany",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yml#/definitions/OilCompany",
				"uniqueKeys": [
					"code",
					"tenantId"
				]
			},
			{
				"masterName": "WasteType",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yml#/definitions/WasteType",
				"uniqueKeys": [
					"code",
					"tenantId",
					"name"
				]
			},
			{
				"masterName": "WasteSubType",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yml#/definitions/WasteSubType",
				"uniqueKeys": [
					"code",
					"tenantId",
					"name"
				]
			},
			{
				"masterName": "DumpingGround",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yml#/definitions/DumpingGround",
				"uniqueKeys": [
					"code",
					"tenantId",
					"name"
				]
			},
			{
				"masterName": "ProcessingSite",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yml#/definitions/ProcessingSite",
				"uniqueKeys": [
					"code",
					"tenantId",
					"name"
				]
			},
			{
				"masterName": "ShiftType",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yml#/definitions/ShiftType",
				"uniqueKeys": [
					"code",
					"tenantId",
					"name"
				]
			},
			{
				"masterName": "Shift",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yml#/definitions/Shift",
				"uniqueKeys": [
					"code",
					"tenantId"
				]
			},
			{
				"masterName": "CollectionType",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yml#/definitions/CollectionType",
				"uniqueKeys": [
					"code",
					"tenantId"
				]
			},
			{
				"masterName": "Population",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yml#/definitions/Population",
				"uniqueKeys": [
					"code",
					"tenantId"
				]
			},
			{
				"masterName": "Toilet",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yml#/definitions/Toilet",
				"uniqueKeys": [
					"code",
					"tenantId"
				]
			},
			{
				"masterName": "SwmProcess",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yml#/definitions/SwmProcess",
				"uniqueKeys": [
					"code",
					"tenantId"
				]
			},
			{
				"masterName": "PaymentTerms",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yml#/definitions/PaymentTerms",
				"uniqueKeys": [
					"label",
					"tenantId"
				]
			}
		]
	},

	"asset" :
	{
		"isSpecificHeader": false,
		"url": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/asset/contracts/v1-0-0.yml",
		"masters":
		[
			{
				"masterName": "AssetCategory",
				"moduleDefinition": "a/b/c",
				"uniqueKeys": [
					"id",
					"tenantId"
				]
			}
		]
	},
	
	"lcms" :
	{
		"isSpecificHeader": true,
		"url": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/lcms/contracts/lcms-services/v1-0-0.yml",
		"masters":
		[
			{
				"masterName": "court",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/lcms/contracts/lcms-services/v1-0-0.yml#/definitions/Court",
				"uniqueKeys": [
					"code",
					"tenantId"
				],
				"specificHeaders": [
					"name",
					"code",
					"active",
					"type",
					"tenantId",
					"addressLine1",
					"addressLine2",
					"city",
					"pincode"

				]
			},
			{
				"masterName": "side",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/lcms/contracts/lcms-services/v1-0-0.yml#/definitions/Side",
				"uniqueKeys": [
					"code",
					"tenantId"
				]
			},
			{
				"masterName": "caseType",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/lcms/contracts/lcms-services/v1-0-0.yml#/definitions/CaseType",
				"uniqueKeys": [
					"code",
					"tenantId"
				]
			},
			{
				"masterName": "caseCategory",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/lcms/contracts/lcms-services/v1-0-0.yml#/definitions/CaseCategory",
				"uniqueKeys": [
					"code",
					"tenantId"
				]
			},
			{
				"masterName": "bench",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/lcms/contracts/lcms-services/v1-0-0.yml#/definitions/Bench",
				"uniqueKeys": [
					"code",
					"tenantId"
				]
			},
			{
				"masterName": "caseStatus",
				"moduleDefinition": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/lcms/contracts/lcms-services/v1-0-0.yml#/definitions/CaseStatus",
				"uniqueKeys": [
					"code",
					"tenantId"
				]
			}
		]
	}

}

// exports.data = data;
exports.data = data;