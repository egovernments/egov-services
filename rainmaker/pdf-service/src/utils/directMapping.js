import get from "lodash/get";
import set from "lodash/set";
import cloneDeep from "lodash/set";

export const directMapping=(req,formatconfig,dataconfig,variableTovalueMap)=>{    
    
    var directArr = [];        
    var objectOfDirectMapping = get(dataconfig, "DataConfigs.mappings[0].mappings[0].direct", []);
    directArr = objectOfDirectMapping.map(item => {
      return {
        jPath: item.variable,
        val: get(req, item.value.path, "NA"),
        valJsonPath: item.value.path,
        type: item.type,
        format: item.format
      };
    });
  
    for (var i = 0; i < directArr.length; i++) {
      //for array type direct mapping
      if (directArr[i].type == "array") {        
        
        let arrayOfOwnerObject = [];
        // let ownerObject = JSON.parse(JSON.stringify(get(formatconfig, directArr[i].jPath + "[0]", [])));
        
        let { format = {}, val = [], variable } = directArr[i];
        let { scema = [] } = format;
  
        //taking values about owner from request body
        for (let j = 0; j < val.length; j++) {
          // var x = 1;
          let ownerObject={}
          for (let k = 0; k < scema.length; k++) {
            ownerObject[scema[k]]=get(val[j], scema[k], "");
            // set(ownerObject[x], "text", get(val[j], scema[k], ""));
            // x += 2;
          }
          arrayOfOwnerObject.push(ownerObject);
        }
        // set(formatconfig, directArr[i].jPath, arrayOfOwnerObject);
        variableTovalueMap[directArr[i].jPath]=arrayOfOwnerObject;
      }

      //setting value in pdf for array-column type direct mapping 
      else if(directArr[i].type=="array-column"){        
        
        let arrayOfBuiltUpDetails=[];
        // let arrayOfFields=get(formatconfig, directArr[i].jPath+"[0]",[]);
        // arrayOfBuiltUpDetails.push(arrayOfFields);
        
        let { format = {}, val = [], variable } = directArr[i];
        let { scema = [] } = format;
        
        //to get data of multiple floor Built up details       
        for(let j=0;j<val.length;j++)
        {
          let arrayOfItems=[];
          for(let k=0;k<scema.length;k++){
            arrayOfItems.push(get(val[j],scema[k],"NA"));
          }
          
          arrayOfBuiltUpDetails.push(arrayOfItems);
        }   

        // remove enclosing [ &  ]
        let stringBuildpDetails=JSON.stringify(arrayOfBuiltUpDetails).replace('\[', '');
        stringBuildpDetails=stringBuildpDetails.substring(0,stringBuildpDetails.length-1);

        variableTovalueMap[directArr[i].jPath]=stringBuildpDetails;
        // set(formatconfig,directArr[i].jPath,arrayOfBuiltUpDetails);      
      }
      //setting value in pdf for no type direct mapping
      else{
          //if data/value is not present then put NA
        if(directArr[i].val==null)
          variableTovalueMap[directArr[i].jPath]="NA";
          // set(formatconfig, directArr[i].jPath, "NA");
        else
          variableTovalueMap[directArr[i].jPath]=directArr[i].val;
          // set(formatconfig, directArr[i].jPath, directArr[i].val);        
      }      
    }
}