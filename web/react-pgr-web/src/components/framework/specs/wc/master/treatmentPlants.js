var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/treatmentplant/_create",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "treatmentPlants",
		"groups": [
			{
				"label": "wc.create.groups.treatmentplant.title",
				"name": "createTreatmentPlant",
				"fields": [
						{
							"name": "name",
							"jsonPath": "treatmentPlants[0].name",
							"label": "wc.create.groups.fields.treatmentPlantName",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "plantType",
							"jsonPath": "treatmentPlants[0].plantType",
							"label": "wc.create.groups.fields.plantType",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/master/_getplanttypes?|$..key|$..object",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "locationName",
							"jsonPath": "treatmentPlants[0].locationName",
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
							"jsonPath": "treatmentPlants[0].wardName",
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
							"jsonPath": "treatmentPlants[0].zoneName",
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
							"name": "storageReservoirName",
							"jsonPath": "treatmentPlants[0].storageReservoirName",
							"label": "wc.create.groups.fields.storageType",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/storagereservoir/_search?|$..name|$..name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "capacity",
							"jsonPath": "treatmentPlants[0].capacity",
							"label": "wc.create.groups.fields.storageCapacityofReservoir(in MLD)",
							"pattern": "",
							"type": "number",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "description",
							"jsonPath": "treatmentPlants[0].description",
							"label": "wc.create.groups.fields.description",
							"pattern": "",
							"type": "text",
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
		"url": "/wcms/masters/treatmentplant/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "treatmentplant",
		"groups": [
			{
				"label": "wc.create.groups.treatmentplant.title",
				"name": "searchTreatmentPlant",
				"fields": [
					{
						"name": "plantType",
						"jsonPath": "plantType",
						"label": "wc.create.groups.fields.plantType",
						"pattern": "",
						"type": "singleValueList",
						"url": "/wcms/masters/master/_getplanttypes?|$..key|$..object",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					}
				]
			}
		],
		"result": {
			"header": [{label: "wc.create.groups.fields.treatmentPlantName"},{label: "wc.create.groups.fields.plantType"}, {label: "wc.create.groups.fields.location"}, {label: "wc.create.groups.fields.zone"},
			{label: "wc.create.groups.fields.ward"},{label: "wc.create.groups.fields.storageCapacityofReservoir(in MLD)"},{label: "wc.create.groups.fields.storageReservoirName"}],
			"values": ["name", "plantType","locationNum","zoneNum","wardNum","capacity","storageReservoirName"],
			"resultPath": "TreatmentPlants",
			"rowClickUrlUpdate": "/update/wc/treatmentPlants/{id}",
			"rowClickUrlView": "/view/wc/treatmentPlants/{id}"
			}
	},
	"wc.view": {
		"numCols": 12/3,
		"url": "/wcms/masters/treatmentPlants/_search?id={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "treatmentPlants",
		"groups": [
			{
				"label": "wc.create.groups.treatmentplant.title",
				"name": "createTreatmentPlant",
				"fields": [
						{
							"name": "name",
							"jsonPath": "treatmentPlants.name",
							"label": "wc.create.groups.fields.treatmentPlantName",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "plantType",
							"jsonPath": "treatmentPlants.plantType",
							"label": "wc.create.groups.fields.plantType",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/master/_getplanttypes?|$..key|$..object",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "locationName",
							"jsonPath": "treatmentPlants[0].locationName",
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
							"jsonPath": "treatmentPlants[0].wardName",
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
							"jsonPath": "treatmentPlants[0].zoneName",
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
							"name": "storageReservoirName",
							"jsonPath": "treatmentPlants[0].storageReservoirName",
							"label": "wc.create.groups.fields.storageType",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/storagereservoir/_search?|$..name|$..name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "capacity",
							"jsonPath": "treatmentPlants[0].capacity",
							"label": "wc.create.groups.fields.storageCapacityofReservoir(in MLD)",
							"pattern": "",
							"type": "number",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "description",
							"jsonPath": "treatmentPlants.description",
							"label": "wc.create.groups.fields.description",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			}
		]
	},
	"wc.update": {
		"numCols": 12/3,
		"url": "/wcms/masters/treatmentPlants/_search?id={id}",
		"url":"/wcms/masters/treatmentPlants/_update",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "treatmentPlants",
		"groups": [
			{
				"label": "wc.create.groups.treatmentplant.title",
				"name": "createTreatmentPlant",
				"fields": [
						{
							"name": "name",
							"jsonPath": "treatmentPlants.name",
							"label": "wc.create.groups.fields.treatmentPlantName",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "plantType",
							"jsonPath": "treatmentPlants.plantType",
							"label": "wc.create.groups.fields.plantType",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/master/_getplanttypes?|$..key|$..object",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "locationName",
							"jsonPath": "treatmentPlants[0].locationName",
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
							"jsonPath": "treatmentPlants[0].wardName",
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
							"jsonPath": "treatmentPlants[0].zoneName",
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
							"name": "storageReservoirName",
							"jsonPath": "treatmentPlants[0].storageReservoirName",
							"label": "wc.create.groups.fields.storageType",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/storagereservoir/_search?|$..name|$..name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "capacity",
							"jsonPath": "treatmentPlants[0].capacity",
							"label": "wc.create.groups.fields.storageCapacityofReservoir(in MLD)",
							"pattern": "",
							"type": "number",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "description",
							"jsonPath": "treatmentPlants.description",
							"label": "wc.create.groups.fields.description",
							"pattern": "",
							"type": "text",
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
