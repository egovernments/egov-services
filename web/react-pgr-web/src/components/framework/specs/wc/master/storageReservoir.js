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
							"label": "wc.create.groups.fields.storageReservoirName",
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
							"label": "wc.create.groups.fields.reservoirType",
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
							"jsonPath": "StorageReservoir[0].locationNum",
							"label": "wc.create.groups.fields.location",
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
							"jsonPath": "StorageReservoir[0].wardNum",
							"label": "wc.create.groups.fields.ward",
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
							"jsonPath": "StorageReservoir[0].zoneNum",
							"label": "wc.create.groups.fields.zone",
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
							"label": "wc.create.groups.fields.numberOfMainDistributionLine",
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
							"label": "wc.create.groups.fields.numberOfConnectionFromReservoir",
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
							"label": "wc.create.groups.fields.numberOfSubLines",
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
							"label": "wc.create.groups.fields.storageCapacityofReservoir(in MLD)",
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
				"label": "wc.create.storageReservoir.title",
				"name": "createstorageReservoir",
				"fields": [
						{
							"name": "reservoirType",
							"jsonPath": "reservoirType",
							"label": "wc.create.groups.fields.reservoirType",
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
			"header": [{label: "wc.create.groups.fields.storageReservoirName"},{label: "wc.create.groups.fields.reservoirType"}, {label: "wc.create.groups.fields.location"}, {label: "wc.create.groups.fields.zone"},
			{label: "wc.create.groups.fields.ward"},{label: "wc.create.groups.fields.storageCapacityofReservoir(in MLD)"},{label: "wc.create.groups.fields.numberOfSubLines"},{label: "wc.create.groups.fields.numberOfMainDistributionLine"},{label: "wc.create.groups.fields.numberOfConnectionFromReservoir"}],
			"values": ["name", "reservoirType", "locationName","zoneName","wardName","capacity","noOfSubLines","noOfMainDistributionLines","noOfConnection"],
			"resultPath": "StorageReservoirs",
			"rowClickUrlUpdate": "/update/wc/storageReservoir/{id}",
			"rowClickUrlView": "/view/wc/storageReservoir/{id}"
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
							"label": "wc.create.groups.fields.storageReservoirName",
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
							"label": "wc.create.groups.fields.reservoirType",
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
							"jsonPath": "StorageReservoirs[0].locationName",
							"label": "wc.create.groups.fields.location",
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
							"label": "wc.create.groups.fields.ward",
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
							"label": "wc.create.groups.fields.zone",
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
							"label": "wc.create.groups.fields.numberOfMainDistributionLine",
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
							"label": "wc.create.groups.fields.numberOfConnectionFromReservoir",
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
							"label": "wc.create.groups.fields.numberOfSubLines",
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
							"label": "wc.create.groups.fields.storageCapacityofReservoir(in MLD)",
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
							"label": "wc.create.groups.fields.storageReservoirName",
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
							"label": "wc.create.groups.fields.reservoirType",
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
							"jsonPath": "StorageReservoir[0].locationNum",
							"label": "wc.create.groups.fields.location",
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
							"jsonPath": "StorageReservoir[0].wardNum",
							"label": "wc.create.groups.fields.ward",
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
							"jsonPath": "StorageReservoir[0].zoneNum",
							"label": "wc.create.groups.fields.zone",
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
							"label": "wc.create.groups.fields.numberOfMainDistributionLine",
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
							"label": "wc.create.groups.fields.numberOfConnectionFromReservoir",
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
							"label": "wc.create.groups.fields.numberOfSubLines",
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
							"label": "wc.create.groups.fields.storageCapacityofReservoir(in MLD)",
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
