var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/storagereservoir/_create",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "treatmentPlant",
		"groups": [
			{
				"label": "wc.create.storageReservoir.title",
				"name": "createstorageReservoir",
				"fields": [
						{
							"name": "name",
							"jsonPath": "treatmentPlant.name",
							"label": "Treatment Plant",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "reservoirType",
							"jsonPath": "storageReservoir.reservoirType",
							"label": "Reservoir Type",
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
							"jsonPath": "storageReservoir.locationName",
							"label": "Location",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/master/_getreservoirtypes?|$..key|$..object",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "wardName",
							"jsonPath": "storageReservoir.wardName",
							"label": "Ward",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/master/_getreservoirtypes?|$..key|$..object",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "zoneName",
							"jsonPath": "storageReservoir.zoneName",
							"label": "Zone",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/master/_getreservoirtypes?|$..key|$..object",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "noOfMainDistributionLines",
							"jsonPath": "storageReservoir.noOfMainDistributionLines",
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
							"jsonPath": "storageReservoir.noOfConnection",
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
							"jsonPath": "storageReservoir.noOfSubLines",
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
							"jsonPath": "storageReservoir.capacity",
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
		"objectName": "storageReservoir",
		"groups": [
			{
				"label": "wc.search.storageReservoir.title",
				"name": "createstorageReservoir",
				"fields": [
						{
							"name": "name",
							"jsonPath": "storageReservoir.name",
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
							"jsonPath": "storageReservoir.active",
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
