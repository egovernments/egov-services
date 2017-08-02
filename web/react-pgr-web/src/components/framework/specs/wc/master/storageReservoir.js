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
							"url": "/egov-location/boundarys/_search?&boundaryType=Locality|$..boundaryNum|$..name",
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
							"url": "/egov-location/boundarys/_search?&boundaryType=Ward|$..boundaryNum|$..name",
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
							"url": "/egov-location/boundarys/_search?&boundaryType=Zone|$..boundaryNum|$..name",
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

		"useTimestamp": true,
		"objectName": "StorageReservoir",
		"groups": [
			{
				"label": "wc.search.storageReservoir.title",
				"name": "createstorageReservoir",
				"fields": [
						{
							"name": "name",
							"jsonPath": "StorageReservoir.name",
							"label": "Reservoir Name",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "reservoirType",
							"jsonPath": "StorageReservoir.reservoirType",
							"label": "Reservoir Type",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/master/_getreservoirtypes?|$..key|$..object",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			}
		],
		"result": {
			"header": [{label: "wc.create.code"},{label: "wc.create.storageReservoir"},{label: "wc.create.typeOfReservoir"}, {label: "wc.create.Location"}, {label: "wc.create.Zone"},
			{label: "wc.create.ward"},{label: "wc.create.StorageCapacityofReservoir"},{label: "wc.create.subLines"},{label: "wc.create.distributionLine "},{label: "wc.create.connection "}],
			"values": ["code","name", "reservoirType", "location","zone","ward","capacity","noOfSubLines","noOfMainDistributionLines","noOfConnection"],
			"resultPath": "StorageReservoirs",
			"rowClickUrlUpdate": "/update/wc/storagereservoir/{id}",
			"rowClickUrlView": "/view/wc/storagereservoir/{id}"
			}
	},
	"wc.view": {
		"numCols": 12/3,
		"url": "/wcms/masters/storagereservoir/_search?id={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "StorageReservoirs",
		"groups": [
			{
				"label": "wc.create.storageReservoir.title",
				"name": "createstorageReservoir",
				"fields": [
						{
							"name": "name",
							"jsonPath": "StorageReservoirs[0].name",
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
							"jsonPath": "StorageReservoirs[0].reservoirType",
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
							"jsonPath": "StorageReservoirs[0].name",
							"label": "Location",
							"pattern": "",
							"type": "singleValueList",
							"url": "/egov-location/boundarys/_search?&boundaryType=Locality|$..boundaryNum|$..name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "wardName",
							"jsonPath": "StorageReservoirs[0].wardName",
							"label": "Ward",
							"pattern": "",
							"type": "singleValueList",
							"url": "/egov-location/boundarys/_search?&boundaryType=Ward|$..boundaryNum|$..name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "zoneName",
							"jsonPath": "StorageReservoirs[0].zoneName",
							"label": "Zone",
							"pattern": "",
							"type": "singleValueList",
							"url": "/egov-location/boundarys/_search?&boundaryType=Zone|$..boundaryNum|$..name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "noOfMainDistributionLines",
							"jsonPath": "StorageReservoirs[0].noOfMainDistributionLines",
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
							"jsonPath": "StorageReservoirs[0].noOfConnection",
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
							"jsonPath": "StorageReservoirs[0].noOfSubLines",
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
							"jsonPath": "StorageReservoirs[0].capacity",
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
	"wc.update" : {
		"numCols": 12/3,
		"searchUrl": "/wcms/masters/storagereservoir/_search?id={id}",
		"url":"/wcms/masters/storagereservoir/_update",
		"tenantIdRequired": true,
		"idJsonPath": "StorageReservoirs[0].id",
		"useTimestamp": true,
		"isResponseArray" : true,
		"objectName": "StorageReservoir[0]",
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
							"url": "/egov-location/boundarys/_search?&boundaryType=Locality|$..boundaryNum|$..name",
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
							"url": "/egov-location/boundarys/_search?&boundaryType=Ward|$..boundaryNum|$..name",
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
							"url": "/egov-location/boundarys/_search?&boundaryType=Zone|$..boundaryNum|$..name",
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
	}
}

export default dat;
