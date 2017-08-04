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
				"name": "createStorageReservoir",
				"fields": [
						{
							"name": "name",
							"jsonPath": "StorageReservoir[0].name",
							"label": "wc.create.groups.fields.storageReservoirName",
							"pattern": "^.{3,100}$",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Maximum length of name is 100"
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
							"url": "/egov-location/boundarys/_search?&boundaryType=Locality|$.Boundary.*.boundaryNum|$.Boundary.*.name",
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
							"url": "/egov-location/boundarys/_search?&boundaryType=Ward|$.Boundary.*.boundaryNum|$.Boundary.*.name",
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
							"url": "/egov-location/boundarys/_search?&boundaryType=Zone|$.Boundary.*.boundaryNum|$.Boundary.*.name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "noOfMainDistributionLines",
							"jsonPath": "StorageReservoir[0].noOfMainDistributionLines",
							"label": "wc.create.groups.fields.numberOfMainDistributionLine",
							"pattern": "^.{1,124}$",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Maximum length of DistributionLine is 124"
						},
						{
							"name": "noOfConnection",
							"jsonPath": "StorageReservoir[0].noOfConnection",
							"label": "wc.create.groups.fields.numberOfConnectionFromReservoir",
							"pattern": "^.{1,124}$",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Maximum length of connection is 124"
						},
						{
							"name": "noOfSubLines",
							"jsonPath": "StorageReservoir[0].noOfSubLines",
							"label": "wc.create.groups.fields.numberOfSubLines",
							"pattern": "^.{1,124}$",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Maximum length of subline is 124"
						},
						{
							"name": "capacity",
							"jsonPath": "StorageReservoir[0].capacity",
							"label": "wc.create.groups.fields.storageCapacityofReservoir(in MLD)",
							"pattern": "^.{1,8}$",
							"type": "number",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Maximum length of capacity is 8"
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
				"name": "searchStorageReservoir",
				"fields": [
						{
							"name": "name",
							"jsonPath": "name",
							"label": "wc.create.groups.fields.storageReservoirName",
							"pattern": "^.{3,100}$",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Maximum length of name is 100"
						},
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
				"name": "viewStorageReservoir",
				"fields": [
						{
							"name": "name",
							"jsonPath": "StorageReservoirs[0].name",
							"label": "wc.create.groups.fields.storageReservoirName",
							"pattern": "^.{3,100}$",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Maximum length of name is 100"
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
							"jsonPath": "StorageReservoirs[0].locationNum",
							"label": "wc.create.groups.fields.location",
							"pattern": "",
							"type": "singleValueList",
							"url": "/egov-location/boundarys/_search?&boundaryType=Locality|$.Boundary.*.boundaryNum|$.Boundary.*.name",
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
							"url": "/egov-location/boundarys/_search?&boundaryType=Ward|$.Boundary.*.boundaryNum|$.Boundary.*.name",
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
							"url": "/egov-location/boundarys/_search?&boundaryType=Zone|$.Boundary.*.boundaryNum|$.Boundary.*.name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "noOfMainDistributionLines",
							"jsonPath": "StorageReservoirs[0].noOfMainDistributionLines",
							"label": "wc.create.groups.fields.numberOfMainDistributionLine",
							"pattern": "^.{1,124}$",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Maximum length of DistributionLine is 124"
						},
						{
							"name": "noOfConnection",
							"jsonPath": "StorageReservoirs[0].noOfConnection",
							"label": "wc.create.groups.fields.numberOfConnectionFromReservoir",
							"pattern": "^.{1,124}$",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Maximum length of connection is 124"
						},
						{
							"name": "noOfSubLines",
							"jsonPath": "StorageReservoirs[0].noOfSubLines",
							"label": "wc.create.groups.fields.numberOfSubLines",
							"pattern": "^.{1,124}$",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Maximum length of subline is 124"
						},
						{
							"name": "capacity",
							"jsonPath": "StorageReservoirs[0].capacity",
							"label": "wc.create.groups.fields.storageCapacityofReservoir(in MLD)",
							"pattern": "^.{1,8}$",
							"type": "number",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Maximum length of capacity is 8"
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
				"name": "updateStorageReservoir",
				"fields": [
						{
							"name": "name",
							"jsonPath": "StorageReservoir[0].name",
							"label": "wc.create.groups.fields.storageReservoirName",
							"pattern": "^.{3,100}$",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Maximum length of name is 100"
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
							"url": "/egov-location/boundarys/_search?&boundaryType=Locality|$.Boundary.*.boundaryNum|$.Boundary.*.name",
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
							"url": "/egov-location/boundarys/_search?&boundaryType=Ward|$.Boundary.*.boundaryNum|$.Boundary.*.name",
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
							"url": "/egov-location/boundarys/_search?&boundaryType=Zone|$.Boundary.*.boundaryNum|$.Boundary.*.name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "noOfMainDistributionLines",
							"jsonPath": "StorageReservoir[0].noOfMainDistributionLines",
							"label": "wc.create.groups.fields.numberOfMainDistributionLine",
							"pattern": "^.{1,124}$",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Maximum length of DistributionLine is 124"
						},
						{
							"name": "noOfConnection",
							"jsonPath": "StorageReservoir[0].noOfConnection",
							"label": "wc.create.groups.fields.numberOfConnectionFromReservoir",
							"pattern": "^.{1,124}$",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Maximum length of connection is 124"
						},
						{
							"name": "noOfSubLines",
							"jsonPath": "StorageReservoir[0].noOfSubLines",
							"label": "wc.create.groups.fields.numberOfSubLines",
							"pattern": "^.{1,124}$",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Maximum length of subline is 124"
						},
						{
							"name": "capacity",
							"jsonPath": "StorageReservoir[0].capacity",
							"label": "wc.create.groups.fields.storageCapacityofReservoir(in MLD)",
							"pattern": "^.{1,8}$",
							"type": "number",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Maximum length of capacity is 8"
						}

				]
			}
		]
	}
}

export default dat;
