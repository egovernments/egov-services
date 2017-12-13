

// const data = 
// [
// "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yaml"	
// // "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/asset/contracts/v1-0-0.yml"
// ]
			
const data = 
{
	"swm" :
	{
		"url": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yaml",
		"masters":
		[
			{
				"name" : "wasteType",
				"isStateLevel":true,
				"dataurl" : "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=WasteType|$..code|$..name"
			}
		]
	},

	"asset" :
	{
		"url": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/asset/contracts/v1-0-0.yml",
		"masters":
		[
			{
				"name" : "AssetCategory",
				"isStateLevel":true,
				"dataurl" : "/egov-mdms-service/v1/_get?&moduleName=ASSET&masterName=AssetCategory|$.MdmsRes.ASSET.AssetCategory.*.id|$.MdmsRes.ASSET.AssetCategory.*.name",			
			}
		]
	},
	
	"lcms" :
	{
		"url": "https://raw.githubusercontent.com/egovernments/egov-services/master/docs/lcms/contracts/lcms-services/v1-0-0.yml",
		"masters":
		[
			{		
			}
		]
	}

}

// exports.data = data;
exports.data = data;