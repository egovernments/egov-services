var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/storagereservoir/_create",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "StorageReservoir",
		"groups": [
			{
				"label": "wc.create.storageReservoir.title",
				"name": "createstorageReservoir",
				"fields": [
						{
							"name": "name",
							"jsonPath": "StorageReservoir[0].name",
							"label": "Storage Reservoir",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "reservoirType",
							"jsonPath": "StorageReservoir[0].reservoirType",
							"label": "Reservoir Type",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/master/_getreservoirtypes?|$..key|$..object",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "locationName",
							"jsonPath": "StorageReservoir[0].locationName",
							"label": "Location",
							"pattern": "",
							"type": "singleValueList",
							"url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=Locality&hierarchyTypeName=LOCATION|$..name|$..name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "wardName",
							"jsonPath": "StorageReservoir[0].wardName",
							"label": "Ward",
							"pattern": "",
							"type": "singleValueList",
							"url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=Ward&hierarchyTypeName=REVENUE|$..name|$..name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "zoneName",
							"jsonPath": "StorageReservoir[0].zoneName",
							"label": "Zone",
							"pattern": "",
							"type": "singleValueList",
							"url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=Zone&hierarchyTypeName=REVENUE|$..name|$..name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "noOfMainDistributionLines",
							"jsonPath": "StorageReservoir[0].noOfMainDistributionLines",
							"label": "Number of main distribution line",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "noOfConnection",
							"jsonPath": "StorageReservoir[0].noOfConnection",
							"label": "Number of connection from reservoir",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "noOfSubLines",
							"jsonPath": "StorageReservoir[0].noOfSubLines",
							"label": "Number of sub lines",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "capacity",
							"jsonPath": "StorageReservoir[0].capacity",
							"label": "Storage Capacity of Reservoir(in MLD)",
							"pattern": "",
							"type": "number",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}

				]
			}
		]
	},
	"wc.search": {
		"numCols": 12/3,
		"url": "/wcms/masters/storagereservoir/_search",
		"tenantIdRequired": true,
		"objectName": "StorageReservoir",
		"groups": [
			{
				"label": "wc.search.storageReservoir.title",
				"name": "createstorageReservoir",
				"fields": [
						{
							"name": "name",
							"jsonPath": "StorageReservoir.name",
							"label": "Category Type",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "Active",
							"jsonPath": "StorageReservoir.active",
							"label": "Active",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			}
		]
	}
}

export default dat;
