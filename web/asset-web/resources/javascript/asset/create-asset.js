var CONST_API_GET_FILE = "/filestore/v1/files/id?tenantId=" + tenantId + "&fileStoreId=";
var flag = 0, flag1 = 0, mode = getUrlVars()["type"];
const makeAjaxUpload = function(file, cb) {
    if(file.constructor == File) {
      let formData = new FormData();
      formData.append("jurisdictionId", tenantId);
      formData.append("module", "ASSET");
      formData.append("file", file);
      $.ajax({
          url: baseUrl + "/filestore/v1/files?tenantId=" + tenantId,
          data: formData,
          cache: false,
          contentType: false,
          processData: false,
          type: 'POST',
          success: function(res) {
              cb(null, res);
          },
          error: function(jqXHR, exception) {
              cb(jqXHR.responseText || jqXHR.statusText);
          }
      });
    } else {
      cb(null, {files: [{
        fileStoreId: file
      }]});
    }
}

const hasValues = function(files) {
  for(var i=0; i< files.length; i++) {
    if(files[i] && files[i].value && files[i].value.constructor == Array && files[i].value.length)
      return true;
  }

  return false;
}

const uploadFiles = function(body, cb) {
    if(body.Asset.assetAttributes && body.Asset.assetAttributes.length) {
        var counter1 = body.Asset.assetAttributes.length;
        var breakout = 0;
        for(let i=0; i<body.Asset.assetAttributes.length; i++) {
          if(body.Asset.assetAttributes[i].type == "File") {
            var counter = body.Asset.assetAttributes[i].value.length;
            var docs = [];
            if(counter > 0) {
              for(let j=0; j<body.Asset.assetAttributes[i].value.length; j++) {
                  makeAjaxUpload(body.Asset.assetAttributes[i].value[j], function(err, res) {
                      if (breakout == 1)
                          return;
                      else if (err) {
                          cb(err);
                          breakout = 1;
                      } else {
                          counter--;
                          docs.push(res.files[0].fileStoreId);
                          if(counter == 0) {
                              body.Asset.assetAttributes[i].value = docs;
                              counter1--;
                              if(counter1 == 0 && breakout == 0)
                                  cb(null, body);
                          }
                      }
                  })
              }
            } else {
              counter1--;
              if(counter1 == 0 && breakout == 0) {
                  cb(null, body);
              }
            }
          } else {
            counter1--;
            if(counter1 == 0 && breakout == 0) {
                cb(null, body);
            }
          }
        }
    } else {
        cb(null, body);
    }
}

const uploadFilesForAsset = function (body, cb) {
  if (body.Asset.documents) {
    var breakout = 0;
    var docs = [];
    let counter = body.Asset.documents.length;
    for (let j = 0; j < body.Asset.documents.length; j++) {
      makeAjaxUpload(body.Asset.documents[j], function (err, res) {
        if (breakout == 1)
          return;
        else if (err) {
          cb(err);
          breakout = 1;
        } else {
          counter--;
          docs.push(res.files[0].fileStoreId);
          if (counter == 0) {
            body.Asset.documents = docs;
            cb(null, body);
          }
        }
      })
    }
  } else {
    cb(null, body);
  }
}

const defaultAssetSetState = {
    "tenantId": tenantId,
    "name": "",
    "code": "",
    "department": {
        "id": ""
    },
    "assetCategory": {
        tenantId,
        "id": "",
        "name": ""
    },
		"assetDetails": "",
    "modeOfAcquisition": "",
    "status": "",
    "grossValue": "",
    "accumulatedDepreciation": "",
    "marketValue":"",
    "surveyNumber":"",
    "description": "",
    "dateOfCreation": "",
    "locationDetails": {
      "locality": "",
      "zone": "",
      "revenueWard": "",
      "block": "",
      "street": "",
      "electionWard": "",
      "doorNo": "",
      "pinCode": ""
    },
    "version": "",
    "remarks": "",
    "length": "",
    "width": "",
    "totalArea": "",
    "assetReference": "",
    "assetReferenceName": "",
    "assetAttributes":[],
    "depreciationRate": "",
    "scheme": "",
    "subScheme": "",
    "enableYearWiseDepreciation": false,
    "yearWiseDepreciation": [Object.assign({}, defaultyearWiseDepRateTemp)]
};

const defaultyearWiseDepRateTemp = {
    "depreciationRate": "",
    "financialYear": "",
};

class CreateAsset extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
        list: [],
        ifassetLandIM : false,
        tblSet: {
          removeCustom: false
        },
        assetSet: Object.assign({}, defaultAssetSetState),
        refSet: {
          tenantId: tenantId,
          name: "",
          department: "",
          assetCategory: "",
          status: "",
          code: ""
        },
        assetCategories: [],
        locality: [],
        electionwards: [],
        departments: [],
        acquisitionList: [],
        revenueZone: [],
        street: [],
        revenueWards: [],
        revenueBlock: [],
        customFields:[],
        statusList: [],
        asset_category_type: [],
        capitalized: false,
        error: "",
        success: "",
				assetAttribute:{},
        readonly: false,
        newRows: {},
        references: [],
        relatedAssets: [],
        removeAsset: "",
        modify1: false,
        modify2: false,
        allFiles: [],
        removedFiles: {},
        financialYears: [],
        schemes: [],
        subSchemes: []
    }
    this.handleChange = this.handleChange.bind(this);
    this.handleChangeTwoLevel = this.handleChangeTwoLevel.bind(this);
    this.addOrUpdate = this.addOrUpdate.bind(this);
    this.handleChangeAssetAttr = this.handleChangeAssetAttr.bind(this);
    this.addNewRow = this.addNewRow.bind(this);
    this.handleReferenceChange = this.handleReferenceChange.bind(this);
    this.handleRefSearch = this.handleRefSearch.bind(this);
    this.selectRef = this.selectRef.bind(this);
    this.removeRow = this.removeRow.bind(this);
    this.handleClick = this.handleClick.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
    this.openRelatedAssetMdl = this.openRelatedAssetMdl.bind(this);
    this.removeReferenceConfirm = this.removeReferenceConfirm.bind(this);
    this.removeReference = this.removeReference.bind(this);
    this.openNewRelAssetMdl = this.openNewRelAssetMdl.bind(this);
    this.addToRemovedFiles = this.addToRemovedFiles.bind(this);
    this.handleAddNewYearRow = this.handleAddNewYearRow.bind(this);
    this.handleDelYearRow = this.handleDelYearRow.bind(this);
    this.handleYearChange = this.handleYearChange.bind(this);
  }

  handleAddNewYearRow() {
    var _yearWiseTemplateArr = Object.assign([], this.state.assetSet.yearWiseDepreciation);
    _yearWiseTemplateArr.push(Object.assign({}, defaultyearWiseDepRateTemp));
    this.setState({
      assetSet: {
        ...this.state.assetSet,
        yearWiseDepreciation: _yearWiseTemplateArr
      }
    });
  }

  handleDelYearRow(ind) {
    var _yearWiseTemplateArr = Object.assign([], this.state.assetSet.yearWiseDepreciation);
    _yearWiseTemplateArr.splice(ind, 1);
    this.setState({
      assetSet: {
        ...this.state.assetSet,
        yearWiseDepreciation: _yearWiseTemplateArr
      }
    });
  }

  handleYearChange(e, key, ind) {
    var _yearWiseTemplateArr = Object.assign([], this.state.assetSet.yearWiseDepreciation);
    _yearWiseTemplateArr[ind][key] = e.target.value;
    this.setState({
      assetSet: {
        ...this.state.assetSet,
        yearWiseDepreciation: _yearWiseTemplateArr
      }
    });
  }

  close() {
      open(location, '_self').close();
  }

  removeReferenceConfirm(e, asset) {
    e.preventDefault();
    e.stopPropagation();
    this.setState({
      ...this.state,
      removeAsset: Object.assign({}, asset)
    });

    $("#confirmDeleteMdl").modal("show");
  }

  openNewRelAssetMdl(e) {
    e.preventDefault();
    var _this = this;
    $("#relatedAssetsModal").modal("hide");
    setTimeout(function(){
      _this.setState({
        references: [],
        modify2: true
      });

      setTimeout(function(){
        _this.setState({
          modify2: false
        })
      }, 1200);
      $("#newRelAssetMdl").modal("show");
    }, 500);
  }

  removeReference(e) {
    e.preventDefault();
    e.stopPropagation();
    if(this.state.removeAsset) {
      $("#confirmDeleteMdl").modal("hide");
      var _this = this;
      var body = {
        RequestInfo: requestInfo,
        Asset: Object.assign({}, this.state.removeAsset)
      };
      body.Asset.assetReference = "";
      if(body.Asset.dateOfCreation) {
        body.Asset.dateOfCreation = moment(body.Asset.dateOfCreation, "DD/MM/YYYY").valueOf();;
      }

      $.ajax({
          url: baseUrl + "/asset-services/assets/_update/"+ _this.state.removeAsset.code + "?tenantId=" + tenantId,
          type: 'POST',
          dataType: 'json',
          data: JSON.stringify(body),
          contentType: 'application/json',
          headers:{
              'auth-token': authToken
          },
          success: function(res) {
            var relatedAssets = _this.state.relatedAssets;
            for(var i=0;i<relatedAssets.length;i++) {
              if(body.Asset.id == relatedAssets[i].id) {
                relatedAssets.splice(i, 1);
                break;
              }
            }
            _this.setState({
              removeAsset: "",
              relatedAssets
            });
          },
          error: function(err) {
            console.log(err);
            var _err = err["responseJSON"].Error.message || "";
            if(err["responseJSON"].Error.fields && Object.keys(err["responseJSON"].Error.fields).length) {
              for(var key in err["responseJSON"].Error.fields) {
                _err += "\n " + key + "- " + err["responseJSON"].Error.fields[key] + " "; //HERE
              }
              showError(_err);
            } else if(_err) {
              showError(_err);
            } else {
              showError(err["statusText"]);
            }
          }
      })
    }
  }

  handleClick(asset) {
    window.open(`app/asset/create-asset.html?id=${asset.id}&type=view`, '_blank', 'location=yes, height=760, width=800, scrollbars=yes, status=yes');
  }

  getCategory(id) {
      if(this.state.assetCategories.length) {
				// return cutf;

        for (var i = 0; i < this.state.assetCategories.length; i++) {
          if (this.state.assetCategories[i].id == id) {
              return this.state.assetCategories[i]["assetFieldsDefination"];
          }
        }
      }
			else {
        return [];
      }
  }

  handleReferenceChange(e, name) {
    e.preventDefault();
    this.setState({
      refSet: {
        ...this.state.refSet,
        [name]: e.target.value
      }
    })
  }

  selectRef(e, asset) {
    e.preventDefault();
    e.stopPropagation();
    if($('#newRelAssetMdl').hasClass('in')) {
      var newAsset = Object.assign({}, asset);
      newAsset.assetReference = this.state.assetSet.id;
      if(newAsset.dateOfCreation) {
        newAsset.dateOfCreation = moment(newAsset.dateOfCreation, "DD/MM/YYYY").valueOf();;
      }

      var body = {
        RequestInfo: requestInfo,
        Asset: newAsset
      };
      $.ajax({
          url: baseUrl + "/asset-services/assets/_update/" + newAsset.code + "?tenantId=" + tenantId,
          type: 'POST',
          dataType: 'json',
          data: JSON.stringify(body),
          contentType: 'application/json',
          headers:{
              'auth-token' :authToken
          },
          success: function(res) {
            $("#newRelAssetMdl").modal("hide");
            showSuccess("Related asset added successfully.");
          },
          error: function(err) {
            console.log(err);
            var _err = err["responseJSON"].Error.message || "";
            if(err["responseJSON"].Error.fields && Object.keys(err["responseJSON"].Error.fields).length) {
              for(var key in err["responseJSON"].Error.fields) {
                _err += "\n " + key + "- " + err["responseJSON"].Error.fields[key] + " "; //HERE
              }
              showError(_err);
            } else if(_err) {
              showError(_err);
            } else {
              showError(err["statusText"]);
            }
          }
      })
    } else {
      this.setState({
        assetSet: {
          ...this.state.assetSet,
          assetReference: asset.id,
          assetReferenceName: asset.name,
          locationDetails: asset.locationDetails
        },
        refSet: {
          tenantId: tenantId,
          name: "",
          department: "",
          assetCategory: "",
          status: "",
          code: ""
        },
        references: []
      });
      flag = 1;
      $("#refModal").modal("hide");
    }
  }

  componentWillUpdate() {
    if(flag == 1) {
      flag = 0;
      $('#refTable').dataTable().fnDestroy();
    } else if(flag1 == 1) {
      flag1 = 0;
      $('#relatedTable').dataTable().fnDestroy();
    }
  }

  componentDidUpdate(prevProps, prevState) {
      if (this.state.modify2) {
          $('#refTable').DataTable({
             dom: 'Bfrtip',
             ordering: false,
             bDestroy: true,
             language: {
                "emptyTable": "No Records"
             },
             buttons: []
          });
      } else if(this.state.modify1) {
          $('#relatedTable').DataTable({
             dom: 'Bfrtip',
             ordering: false,
             bDestroy: true,
             language: {
                "emptyTable": "No Records"
             },
             buttons: []
          });
      }
  }

  handleRefSearch(e) {
    e.preventDefault();
    var assets = [], _this = this;
    commonApiPost("asset-services","assets","_search", this.state.refSet, function(err, res) {
      if(res && res["Assets"]) {
          assets = res["Assets"];
          assets.sort(function(item1, item2) {
            return item1.code.toLowerCase() > item2.code.toLowerCase() ? 1 : item1.code.toLowerCase() < item2.code.toLowerCase() ? -1 : 0;
          });
          _this.setState({
            references: assets,
            modify2: true
          })
      } else {
        _this.setState({
          references: [],
          modify2: true
        })
      }

      setTimeout(function(){
        _this.setState({
          modify2: false
        })
      }, 1200);
    });

    flag = 1;
  }

  handleChange(e, name, pattern) {
      
    if (name == "scheme") {
      commonApiPost("egf-masters", "subschemes", "_search", { tenantId, scheme: val }, function (err, res) {
        if (res) {
          _this.setState({
            ...this.state,
            subSchemes: res["subSchemes"] || [],
            assetSet: {
              ...this.state.assetSet,
              [name]: e.target.value
            }
          })
        }
      })
    } else if(name === "status") {
        // console.log(name, e.target.value);
        this.state.capitalized = ( e.target.value === "CAPITALIZED");
      } else if(name == "enableYearWiseDepreciation") {
        return this.setState({
          assetSet: {
              ...this.state.assetSet,
              [name]: e.target.checked
          }
         })
      }

      if(pattern && e.target.value){
        // console.log('pattern exists');
        var reg = new RegExp(pattern);
        if(reg.test(e.target.value)){
          // console.log('pattern success');
          this.setState({
              assetSet: {
                  ...this.state.assetSet,
                  [name]: e.target.value
              }
          })
        }else{
          // console.log('pattern fails'); //Dont update the state
        }
      }else{
        this.setState({
            assetSet: {
                ...this.state.assetSet,
                [name]: name == "documents" ? e.target.files : e.target.value
            }
        })
      }

  }

  addOrUpdate(e) {
      e.preventDefault();
      this.setState({
        ...this.state,
        error: "",
        success: ""
      });
      var tempInfo = Object.assign({}, this.state.assetSet) , _this = this, type = getUrlVars()["type"];
      var remFiles = Object.assign({}, this.state.removedFiles);
      if(tempInfo && tempInfo.assetCategory)
        tempInfo.assetCategory.tenantId = tenantId;
      delete tempInfo.assetReferenceName;

      if(tempInfo.assetAttributes && tempInfo.assetAttributes.length) {
        for(var i=0; i<tempInfo.assetAttributes.length;i++){
          if(tempInfo.assetAttributes[i].type == "Table") {
              for(var j=0; j<tempInfo.assetAttributes[i].value.length; j++)
                delete tempInfo.assetAttributes[i].value[j]["new"];
          }

          if(tempInfo.assetAttributes[i].type == "File" && tempInfo.assetAttributes[i].value && remFiles && Object.keys(remFiles).length > 0 && remFiles[tempInfo.assetAttributes[i].key]) {
            tempInfo.assetAttributes[i].value.map(function(val, ind) {
              if(remFiles[tempInfo.assetAttributes[i].key][val])
                tempInfo.assetAttributes[i].value.splice(ind, 1);
            })
          }
        }
      }

      if(tempInfo.dateOfCreation) {
        tempInfo.dateOfCreation = moment(tempInfo.dateOfCreation, "DD/MM/YYYY").valueOf();
      }

      if(!tempInfo.enableYearWiseDepreciation) {
        delete tempInfo.yearWiseDepreciation;
      } else {
        delete tempInfo.depreciationRate;
        var seen = {};
        var hasDuplicates = tempInfo.yearWiseDepreciation.some(function (currentObject) {
            return seen.hasOwnProperty(currentObject.financialYear)
                || (seen[currentObject.financialYear] = false);
        });

        if(hasDuplicates)
          return showError("Duplicate financial years not allowed.");
      }

      var floorDetails = tempInfo.assetAttributes.find(function(element) {return element["key"]==="Floor Details"});
      var noOfFloors = tempInfo.assetAttributes.find(function(element) {return element["key"]==="No. of Floors"});

      console.log("noOfFloors",floorDetails,noOfFloors);

      if(floorDetails && noOfFloors && floorDetails["value"].length != noOfFloors["value"] ){
        return showError("No of Floors and Floor details Does not match. Please Check");
      }

      // console.log(JSON.stringify(tempInfo));

      var body = {
          "RequestInfo": requestInfo,
          "Asset": tempInfo
      };

    uploadFiles(body, function (err, _body) {
      if (err) {
        showError(err);
      } else {
        uploadFilesForAsset(_body, function (err1, __body) {
          if (err1) {
            showError(err1);
          } else {
            $.ajax({
              url: baseUrl + "/asset-services/assets/" + (type == "update" ? "_update" : "_create") + "?tenantId=" + tenantId,
              type: 'POST',
              dataType: 'json',
              data: JSON.stringify(__body),
              contentType: 'application/json',
              headers: {
                'auth-token': authToken
              },
              success: function (res) {
                window.location.href = `app/asset/create-asset-ack.html?name=${tempInfo.name}&type=&value=${getUrlVars()["type"]}&code=${res && res.Assets && res.Assets[0] && res.Assets[0].code ? res.Assets[0].code : ""}`;
              },
              error: function (err) {
                console.log(err);
                var _err = err["responseJSON"].Error.message || "";
                if (err["responseJSON"].Error.fields && Object.keys(err["responseJSON"].Error.fields).length) {
                  for (var key in err["responseJSON"].Error.fields) {
                    _err += "\n " + key + "- " + err["responseJSON"].Error.fields[key] + " "; //HERE
                  }
                  showError(_err);
                } else if (_err) {
                  showError(_err);
                } else {
                  showError(err["statusText"]);
                }
              }
            })
          }
        })
      }
    })
  }

  handleChangeAssetAttr(e, type, key, col, ind, multi) {
    let attr = Object.assign([], this.state.assetSet.assetAttributes);
    for(var i=0; i<attr.length; i++) {
      if(attr[i].key == key) {
        if(col) {
          if(multi) {
            var options = e.target.options;
            var values = [];
            for (var i = 0, l = options.length; i < l; i++) {
              if (options[i].selected) {
                values.push(options[i].value);
              }
            }

            if(attr[i].value[ind])
              attr[i].value[ind][col] = values;
            else {
              attr[i].value[ind] = {
                [col]: values,
                new: true
              };
            }
          } else {
            if(attr[i].value[ind])
              attr[i].value[ind][col] = e.target.value;
            else {
              attr[i].value[ind] = {
                [col]: e.target.value,
                new: true
              };
            }
          }
        } else {
          if(multi) {
            var options = e.target.options;
            var values = [];
            for (var i = 0, l = options.length; i < l; i++) {
              if (options[i].selected) {
                values.push(options[i].value);
              }
            }
            attr[i].value = values;
          } else if(attr[i].type == "File") {
            if(attr[i].value && attr[i].value.constructor == Array) {
              for(var z=0; z< e.target.files.length; z++) {
                attr[i].value.push(e.target.files[z]);
              }
            } else {
              attr[i].value = e.target.files;
            }
          } else {
            attr[i].value = e.target.value;
          }
        }
        this.setState({
          assetSet: {
            ...this.state.assetSet,
            assetAttributes: Object.assign([], attr)
          }
        });
        return;
      }
    }

    if(col) {
      if(multi) {
        var options = e.target.options;
        var values = [];
        for (var i = 0, l = options.length; i < l; i++) {
          if (options[i].selected) {
            values.push(options[i].value);
          }
        }
        var val = [];
        val[ind] = {
          [col]: values
        };
        attr.push({
          key: key,
          type: type,
          value: val
        })
      } else {
        var val = [];
        val[ind] = {
          [col]: e.target.value
        };
        attr.push({
          key: key,
          type: type,
          value: val
        })
      }
    } else if(multi) {
       var options = e.target.options;
        var values = [];
        for (var i = 0, l = options.length; i < l; i++) {
          if (options[i].selected) {
            values.push(options[i].value);
          }
        }
        attr.push({
          key: key,
          type: type,
          value: values
        })
    } else if(type == "File") {
      attr.push({
          key: key,
          type: type,
          value: e.target.files
        })
    } else {
      attr.push({
          key: key,
          type: type,
          value: e.target.value
        })
    }

    this.setState({
      assetSet: {
        ...this.state.assetSet,
        assetAttributes: Object.assign([], attr)
      }
    });
  }

  handleChangeTwoLevel(e, pName, name, multi) {
      let text, type, codeNo, innerJSON, version, depRate;
      if (pName == "assetCategory") {
          let el = document.getElementById('assetCategory');
          text = el.options[el.selectedIndex].innerHTML;
          version = el.options[el.selectedIndex].getAttribute("data");
          this.setState({
              customFields: this.getCategory(e.target.value),
              assetAttributes: []
          })
          e.target.value = parseInt(e.target.value);
          for(var i=0;i< this.state.assetCategories.length; i++){
            if (e.target.value==this.state.assetCategories[i].id) {
                type = this.state.assetCategories[i].assetCategoryType;
                codeNo = this.state.assetCategories[i].code;
                depRate = this.state.assetCategories[i].depreciationRate;
                break;
            }
         }
         if (type==='LAND' ||type==='IMMOVABLE' ){
           this.setState({ifassetLandIM:true});
         }else{
           this.setState({ifassetLandIM:false});
         }
      }
      if(multi) {
        var options = e.target.options;
        var values = [];
        for (var i = 0, l = options.length; i < l; i++) {
          if (options[i].selected) {
            values.push(options[i].value);
          }
        }
        innerJSON = {
            ...this.state.assetSet[pName],
            [name]: values
        };
      } else {
        innerJSON = {
            ...this.state.assetSet[pName],
            [name]: e.target.type == "file" ? e.target.files : e.target.value
        };
      }

      if(type) {
        innerJSON["assetCategoryType"] = type;
      }

      if(codeNo) {
        innerJSON["code"] = codeNo;
      }

      if(text) {
        innerJSON["name"] = text;
      }

      this.setState({
          assetSet: {
              ...this.state.assetSet,
              [pName]: innerJSON,
              version: version || this.state.assetSet.version || "",
              depreciationRate: pName == "assetCategory" ? (typeof depRate != "undefined" ? depRate : "") : (typeof this.state.assetSet.depreciationRate != "undefined" ? this.state.assetSet.depreciationRate : "")
          }
      })

  }

  setInitialState(initObject) {
    this.setState(initObject);
  }

  componentDidMount() {
      var _this = this, type = getUrlVars()["type"];
      if(window.opener && window.opener.document) {
        var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
        if(logo_ele && logo_ele[0]) {
          document.getElementsByClassName("homepage_logo")[0].src = (logo_ele[0].getAttribute("src") && logo_ele[0].getAttribute("src").indexOf("http") > -1) ? logo_ele[0].getAttribute("src") : window.location.origin + logo_ele[0].getAttribute("src");
        }
      }

      $('body').on('change', '.custom-date-picker', function(e) {
        if(e.target && e.target.getAttribute("data-type") == "Date") {
          _this.handleChangeAssetAttr(e, "Date", e.target.getAttribute("data-name"));
        } else {
          _this.handleChangeAssetAttr(e, "Table", e.target.getAttribute("data-parent"), e.target.getAttribute("data-type"), e.target.getAttribute("data-index"));
        }
      });

      $("#refModal").on("hidden.bs.modal", function () {
        flag = 1;
        _this.setState({
          refSet: {
            tenantId: tenantId,
            name: "",
            department: "",
            assetCategory: "",
            status: "",
            code: ""
          },
          references: [],
          modify2: true
        });

        setTimeout(function(){
          _this.setState({
            modify2: false
          });
        }, 1200);
      });

			if(getUrlVars()["type"])
        $('#hpCitizenTitle').text(titleCase(getUrlVars()["type"]) + " Asset");
      else $('#hpCitizenTitle').text("Create Asset");

			if(type==="update"|| type==="view" ){
				$(document).ready(function(){
	     		$('#assetCategory,#code').attr('disabled','disabled');
		 		})
			}
      var count = 12;
      var _state = {
        readonly: (type === "view")
      };
      var checkCountNCall = function(key, res) {
        count--;
        _state[key] = res;
        if(count == 0) {
          _this.setInitialState(_state);
        }
      }
      let assetCat = [];
      // commonApiPost("asset-services", "assetCategories", "_search", {tenantId,isChildCategory:true}, function(err, res) {
      //     assetCat = res["AssetCategory"];
      //     console.log('inside asset');
      //     _this.setState({"assetCategories":res["AssetCategory"]});
      // });

      var assetCategoryPromise = new Promise(function(resolve, reject) {
        // console.log('came to promise');
        // setTimeout(resolve, 100, 'foo');
        commonApiPost("asset-services", "assetCategories", "_search", {tenantId,isChildCategory:true}, function(err, res) {
            // assetCat = res["AssetCategory"];
            resolve(res["AssetCategory"]);
            // console.log('inside asset');
            //
        });
      });

      getDropdown("locality", function(res) {
        checkCountNCall("locality", res);
      });
      getDropdown("electionwards", function(res) {
        checkCountNCall("electionwards", res);
      });
      getDropdown("assignments_department", function(res) {
        checkCountNCall("departments", res);
      });
      getDropdown("acquisitionList", function(res) {
        checkCountNCall("acquisitionList", res);
      });
      getDropdown("revenueZone", function(res) {
        checkCountNCall("revenueZone", res);
      });
      getDropdown("street", function(res) {
        checkCountNCall("street", res);
      });
      getDropdown("revenueWard", function(res) {
        checkCountNCall("revenueWards", res);
      });
      getDropdown("revenueBlock", function(res) {
        checkCountNCall("revenueBlock", res);
      });
      getDropdown("statusList", function(res) {
        checkCountNCall("statusList", res);
      }, {objectName: 'Asset Master'});
      getDropdown("asset_category_type", function(res) {
        checkCountNCall("asset_category_type", res);
      });
      getDropdown("financialYears", function(res) {
          res.sort(function (left, right) {
            return moment.utc(right.startingDate).diff(moment.utc(left.startingDate))
          });
          checkCountNCall("financialYears", res);
      });
      getDropdown("assignments_function", function(res) {
       checkCountNCall("functions", res);
     });

      var id = getUrlVars()["id"];
      $(document).on('focus',".custom-date-picker", function(){
            $(this).datepicker({
                format: 'dd/mm/yyyy',
                autoclose: true
            });
      });

      $('#dateOfCreation').datepicker({
          format: 'dd/mm/yyyy',
          endDate: new Date(),
          autoclose: true
      });

      $('#dateOfCreation').on("change", function(e) {
         _this.setState({
            assetSet: {
                ..._this.state.assetSet,
                  "dateOfCreation": e.target.value
            }
         })
      });

      if (type === "view" || type === "update") {
          getCommonMasterById("asset-services", "assets", id, function(err, res) {
            if(res) {
              let asset = res["Assets"][0];
              var _date = asset.dateOfCreation ? asset.dateOfCreation : "";
              var _files = [];
              if(asset.assetAttributes && asset.assetAttributes.length) {
                for(var i=0; i<asset.assetAttributes.length; i++) {
                  if(asset.assetAttributes[i].type == "File") {
                    _files.push(asset.assetAttributes[i]);
                  }
                }
              } else {
                asset.assetAttributes = [];
              }

              // setTimeout(function() {
                _this.setState({
                    assetSet: {
                      ...asset,
                      dateOfCreation: _date ? moment(new Date(_date)).format("DD/MM/YYYY") : ""
                    },
                    allFiles: JSON.parse(JSON.stringify(_files))
                });
              // }, 100);

              assetCategoryPromise.then((response) => {
                // console.log("Yay! " + JSON.stringify(response));
                _this.setState({"assetCategories":response});
                // console.log('Global var : ', response);
                console.log('Asset Category Master from Promise for update / view: ',response);
                
                  console.log('Its capitalized');
                  let ifassetLandIMObj = response && response.find((obj)=>{return obj.name === asset.assetCategory.name});
                  console.log('LANDIMMOV : ',ifassetLandIMObj);
                  _this.setState({
                      capitalized: asset.status == "CAPITALIZED"?true:false,
                      ifassetLandIM: ifassetLandIMObj && (ifassetLandIMObj.assetCategoryType === 'LAND' || ifassetLandIMObj.assetCategoryType === 'IMMOVABLE') ? true : false
                  });
                
              });

              if(asset.assetCategory && asset.assetCategory.id) {
                  let count = 10;
                  let timer = setInterval(function(){
                      count--;
                      if(count == 0) {
                        clearInterval(timer);
                      } else if(_this.state.assetCategories.length) {
                        clearInterval(timer);
                        _this.setState({
                            customFields: _this.getCategory(asset.assetCategory.id)
                        })
                      }
                  }, 2000);
              }

              if(asset.assetReference) {
                getCommonMasterById("asset-services", "assets", asset.assetReference, function(err, res1) {
                  if(res1 && res1["Assets"] && res1["Assets"][0]) {
                    // setTimeout(function() {
                      _this.setState({
                        assetSet: {
                            ..._this.state.assetSet,
                            assetReferenceName: res1["Assets"][0].name
                        }
                      })
                    // }, 200);
                  }
                })
              }
            }
          })
      }else{
        //no update or view.. its create
        assetCategoryPromise.then((response) => {
          // console.log("Yay! " + JSON.stringify(response));
          console.log('Asset Category Master from Promise for create: ',response);
          this.setState({"assetCategories":response});
        });
      }


    commonApiPost("egf-masters", "schemes", "_search", { tenantId, fund: "1" }, function (err, res) {
      if (res) {
        _this.setState({
          schemes: res["schemes"] || []
        })
      }
    });
                 
  }

  removeRow(e, type, name, index, assetIndex) {
    console.log("Remove Row", type);
    e.preventDefault();
    switch(type) {
      case "custom":
        this.setState({
          tblSet: {
            ...this.state.tblSet,
            removeCustom: true
          }
        });
        var assetAttributes = Object.assign([], this.state.assetSet.assetAttributes);
        for(var i=0; i<assetAttributes.length; i++) {
          if(assetAttributes[i].key == name && assetAttributes[i].value[assetIndex]) {
            assetAttributes[i].value.splice(assetIndex, 1);
            break;
          }
        }

        var _this = this;

        setTimeout(function(){
          _this.setState({
            assetSet: {
              ..._this.state.assetSet,
              assetAttributes: assetAttributes
            }
          })
        }, 200);
        break;
      case "old":
        var assetAttributes = Object.assign([], this.state.assetSet.assetAttributes);
        for(var i=0; i<assetAttributes.length; i++) {
          if(assetAttributes[i].key == name) {
            assetAttributes[i].value.splice(index, 1);
            break;
          }
        }

        this.setState({
          assetSet: {
            ...this.state.assetSet,
            assetAttributes: assetAttributes
          }
        });

        break;
      case "new":
        var _newRow = this.state.newRows[name];
        _newRow.splice(index, 1);
        this.setState({
          newRows: {
            ...this.state.newRows,
            [name]: _newRow
          }
        })

        var assetAttributes = Object.assign([], this.state.assetSet.assetAttributes);
        for(var i=0; i<assetAttributes.length; i++) {
          if(assetAttributes[i].key == name && assetAttributes[i].value[assetIndex]) {
            assetAttributes[i].value.splice(assetIndex, 1);
            break;
          }
        }

        var _this = this;

        setTimeout(function(){
          _this.setState({
            assetSet: {
              ..._this.state.assetSet,
              assetAttributes: assetAttributes
            }
          })
        }, 200);
        break;
    }
  }

  openRelatedAssetMdl(e) {
    e.preventDefault();
    e.stopPropagation();
    var _this = this;
    _this.setState({
      relatedAssets: [],
      modify1: true
    });

    commonApiPost("asset-services", "assets", "_search", {tenantId, assetReference: this.state.assetSet.id}, function(err, res) {
      if(res) {
        flag1 = 1;
        _this.setState({
          relatedAssets: res["Assets"],
          modify1: true
        })
      }

      setTimeout(function(){
        _this.setState({
          modify1: false
        })
      }, 1200);
    });

    $("#relatedAssetsModal").modal('show');
  }

  addNewRow(e, name) {
    e.preventDefault();
    if(!this.state.newRows[name])
      this.setState({
        newRows: {
          ...this.state.newRows,
          [name]: [1]
        }
      })
    else
      this.setState({
        newRows: {
          ...this.state.newRows,
          [name]: [...this.state.newRows[name], 1]
        }
      })
  }

  addToRemovedFiles(name, fileId, addBack) {
    var removedFiles = Object.assign({}, this.state.removedFiles);
    if(addBack) {
      delete removedFiles[name][fileId];
    } else {
      if(removedFiles[name]) {
        removedFiles[name][fileId] = 1;
      } else {
        removedFiles[name] = {[fileId]: 1};
      }
    }
    this.setState({
      removedFiles: Object.assign({}, removedFiles)
    })
  }

  render() {
    // console.log(this.state);
    let {handleChange, openRelatedAssetMdl, handleClick, addOrUpdate, handleChangeTwoLevel, handleChangeAssetAttr, addNewRow, handleReferenceChange, handleRefSearch, selectRef, removeRow, removeReference, removeReferenceConfirm, openNewRelAssetMdl, addToRemovedFiles, handleAddNewYearRow, handleDelYearRow, handleYearChange} = this;
    let {isSearchClicked, list, customFields, error, success, acquisitionList, readonly, newRows, refSet, references, tblSet,departments, relatedAssets, allFiles, removedFiles, financialYears, functions, subSchemes, schemes} = this.state;
    let {
      assetCategory,
      locationDetails,
      assetCategoryType,
      locality,
      doorNo,
      name,
      pinCode,
      street,
      electionWard,
      dateOfCreation,
      assetAddress,
      block,
      zone,
      totalArea,
      code,
      department,
      description,
      modeOfAcquisition,
      length,
      width,
      revenueWard,
      accumulatedDepreciation,
      grossValue,
      marketValue,
      surveyNumber,
      status,
      assetAttributes,
      assetReferenceName,
      assetReference,
      enableYearWiseDepreciation,
      depreciationRate,
      yearWiseDepreciation,
      subScheme,
      scheme
  	} = this.state.assetSet;

    const getType = function() {
        switch(mode) {
            case 'update':
                return "Update";
            case 'view':
                return "View";
            default:
                return "Create";
        }
    }

    const showActionButton = function() {
        if((!mode) || mode === "update") {
          return (<button type="submit" className="btn btn-submit">{mode? "Update": "Create"}</button>);
        }
    };

    const showAlert = function(error, success) {
        if(error) {
          return (
            <div className="alert alert-danger alert-dismissible alert-toast" role="alert">
              <button type="button" className="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <strong>Error!</strong> {error}
            </div>
          )
        } else if(success) {
           return (
            <div className="alert alert-success alert-dismissible alert-toast" role="alert">
              <button type="button" className="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <strong>Success!</strong> {success}
            </div>
          )
        }
    };

    /*const renderRadio = function(list, name, isMandatory) {
        if(list && list.length) {
            return list.map((item, ind) => {
                return (
                    <label className="radio-inline radioUi">
                        <input type="radio" name={name} value={item} disabled={readonly} required={isMandatory} onClick={(e)=>{handleChangeAssetAttr(e,"Radio", name)}}/> {item} &nbsp;&nbsp;
                    </label>
                )
            })
        }
    }

    const renderCheckBox = function(list, name) {
        if(list && list.length) {
            return list.map((item, ind) => {
                return (
                    <label className="radio-inline radioUi">
                        <input type="checkbox" name={item} value={item} disabled={readonly} onClick={(e)=>{handleChangeAssetAttr(e,"Check Box", name)}}/> &nbsp; {item} &nbsp;&nbsp;
                    </label>
                )
            })
        }

    }*/

    const renderOption = function(list, assetCatBool, statusBool, yearBool) {
        if(list) {
            if (list.length) {
              if(statusBool) {
                return list.map((item, ind) => {
                  return (<option key={ind} value={item.code}>
                          {item.code}
                    </option>)
                })
              };

              list.sort(function(item1, item2) {
                if(item1.name && item2.name)
                  return item1.name.toLowerCase() > item2.name.toLowerCase() ? 1 : item1.name.toLowerCase() < item2.name.toLowerCase() ? -1 : 0;
                else
                  return 0;
              });

              if(yearBool) {
                return list.map((item, ind)=> {
                    return (<option key={ind} value={item.finYearRange}>
                          {item.finYearRange}
                    </option>)
                })
              }

              return list.map((item, ind)=> {
                  if(typeof item == "object") {
                    return (<option key={ind} data={assetCatBool ? item.version : ""} value={item.id}>
                          {item.name}
                    </option>)
                  } else {
                    return (<option key={ind} value={item}>
                          {item}
                    </option>)
                  }
              })

            } else {
              return Object.keys(list).map((k, index)=> {
                return (<option key={index} value={k}>
                        {list[k]}
                  </option>)

               })
            }

        }
    }

    const showCategorySection = function()
    {
        if(customFields.length > 0)
        {
					let customFieldsDisply = function() {
              var _custFields = Object.assign([], customFields);
              _custFields.sort(function(item1, item2) {
                return Number(item1.order || "999") > Number(item2.order || "999") ? 1 : Number(item1.order || "999") < Number(item2.order || "999") ? -1 : 0;
              });

              return _custFields.map((item, index) => {
                return checkFields(item, index)
              })
					}

              return (
                <div className="form-section" id="customFieldsDetailsBlock">
                  <h3 className="categoryType">Category Details </h3>
                  <div className="form-section-inner">
                      <div className="row">
                          {/*<div className="col-sm-6">
                            <div className="row">
                              <div className="col-sm-6 label-text">
                                <label for="name">Name <span>* </span></label>
                              </div>
                              <div className="col-sm-6">
                                <input id="name" name="name" value={name} type="text"
                                  onChange={(e)=>{handleChange(e, "name")}} required disabled={readonly}/>
                              </div>
                            </div>
                          </div>
                          <div className="col-sm-6">
                            <div className="row">
                              <div className="col-sm-6 label-text">
                                <label for="name">Mode Of Acquisition <span>* </span></label>
                              </div>
                              <div className="col-sm-6">
                                <select id="modeOfAcquisition" name="modeOfAcquisition" required value={modeOfAcquisition} onChange={(e)=>
                                    {handleChange(e,"modeOfAcquisition") }} disabled={readonly}>
                                    <option value="">Select Mode Of Acquisition</option>
                                    {renderOption(acquisitionList)}
                                </select>
                              </div>
                            </div>
                          </div>*/}
													{customFieldsDisply()}

                      </div>
                  </div>
                </div>
              )
        }
    }

    const renderFileDelBtn = function(name, fileId) {
      if(!removedFiles[name] || (removedFiles[name] && !removedFiles[name][fileId]))
        return (
          <button type="button" className="btn btn-close" style={{"color": "#000000"}} onClick={() => addToRemovedFiles(name, fileId)}>Delete</button>
        )
      else
        return (
          <button type="button" className="btn btn-close" style={{"color": "#000000"}} onClick={() => addToRemovedFiles(name, fileId, true)}>Undo</button>
        )
    }

    const renderFileBody = function(fles) {
      return fles.map(function(v, ind) {
        return v.value.map(function(file, ind2) {
          return (
            <tr key={ind2} style={{"background-color": (removedFiles[v.key] && removedFiles[v.key][file] ? "#d3d3d3" : "#ffffff"), "text-decoration": (removedFiles[v.key] && removedFiles[v.key][file] ? "line-through" : "")}}>
              <td>{ind2+1}</td>
              <td>{v.key}</td>
              <td>
                <a href={window.location.origin + CONST_API_GET_FILE + file} target="_blank">
                  Download
                </a>
              </td>
              <td>{getUrlVars()["type"] == "update" ? renderFileDelBtn(v.key, file) : ""}</td>
            </tr>
          )
        })
      })
    }

    const showAttachedFiles = function() {
      if(allFiles.length && hasValues(allFiles)) {
          return (
              <table id="fileTable" className="table table-bordered">
                  <thead>
                  <tr>
                      <th>Sr. No.</th>
                      <th>Name</th>
                      <th>File</th>
                      <th>Action</th>
                  </tr>
                  </thead>
                  <tbody id="agreementSearchResultTableBody">
                    {
                      renderFileBody(allFiles)
                    }
                  </tbody>

             </table>
            )
      }
    }

    const checkFields = function(item, index, ifTable) {
			switch (item.type) {
				case "Text":
					return showTextBox(item, index, ifTable);
				case "Number":
					return showTextBox(item, index, ifTable);
				case "Email":
					return showTextBox(item, index, ifTable);
				// case "Radio":
				// 	return showRadioButton(item, index, ifTable);
				// case "Check Box":
				// 	return showCheckBox(item, index, ifTable);
				case "Select":
					return showSelect(item, index, false, ifTable);
				case "Multiselect":
					return showSelect(item, index, true, ifTable);
				case "Date":
	        return showDatePicker(item, index, ifTable);
	      case "File":
			  	return showFile(item, index, ifTable);
				case "Table":
				  	return showTable(item, index);
				default:
					return showTextBox(item, index, ifTable);
		}

			}

		const showTextBox = function(item, index, ifTable) {
      if(ifTable) {
        return (
          <input style={{"margin-bottom": 0}} name={item.name} type="text" maxLength= "200"
                  defaultValue={item.values} onChange={(e)=>{handleChangeAssetAttr(e, "Table", item.parent, item.name, index)}} required={item.isMandatory} disabled={readonly || [true, "true"].indexOf(item.isActive) == -1}/>
        );
      } else {
        var type = getUrlVars()["type"];
        var _values;
        if(["view", "update"].indexOf(type) > -1 && assetAttributes.length) {
          var textItem = assetAttributes.filter(function(val, ind) {
            return (val.type == "Text" && item.name == val.key);
          });

          if(textItem && textItem[0])
            _values = textItem[0].value;
        }
        return (
          <div className="col-sm-6" key={index}>
            <div className="row">
              <div className="col-sm-6 label-text">
                <label for={item.name}>{titleCase(item.name)}  {showStart(item.isMandatory)}</label>
              </div>
              <div className="col-sm-6">
                <input name={item.name} type="text" maxLength= "200"
                  defaultValue={_values || item.values} onChange={(e)=>{handleChangeAssetAttr(e, "Text", item.name)}} required={item.isMandatory} disabled={readonly || [true, "true"].indexOf(item.isActive) == -1}/>
              </div>
            </div>
          </div>
        );
      }
		}

    const showDatePicker = function(item, index, ifTable) {
      if(ifTable) {
        return (
          <input data-type="Table" data-parent={item.parent} data-name={item.name} data-index={index} className="custom-date-picker" name={item.name} type="text"
            defaultValue={item.values} onChange={(e)=>{handleChangeAssetAttr(e, "Table", item.parent, item.name, index)}} required={item.isMandatory} disabled={readonly || [true, "true"].indexOf(item.isActive) == -1}/>
        )
      } else {
        var type = getUrlVars()["type"];
        var _values;
        if(["view", "update"].indexOf(type) > -1 && assetAttributes.length) {
          var textItem = assetAttributes.filter(function(val, ind) {
            return (val.type == "Date" && item.name == val.key);
          });

          if(textItem && textItem[0])
            _values = textItem[0].value;
        }
        return (<div className="col-sm-6" key={index}>
                  <div className="row">
                      <div className="col-sm-6 label-text">
                          <label for={item.name}>{titleCase(item.name)}  {showStart(item.isMandatory)}</label>
                      </div>
                      <div className="col-sm-6">
                          <input data-type="Date" data-name={item.name} data-index={index} className="custom-date-picker" name={item.name} type="text"
                            defaultValue={_values || item.values} onChange={(e)=>{handleChangeAssetAttr(e, "Date", item.name)}} required={item.isMandatory} disabled={readonly || [true, "true"].indexOf(item.isActive) == -1}/>
                      </div>
                  </div>
              </div>)
      }
    }

		const showSelect = function(item, index, multi, ifTable) {
      if(ifTable) {
        return (
          <select name={item.name} size={multi ? 3 : 1} multiple={multi ? true : false}
                  onChange={(e)=>{handleChangeAssetAttr(e, "Table", item.parent, item.name, index, multi)}} required={item.isMandatory} disabled={readonly || [true, "true"].indexOf(item.isActive) == -1}>
                  <option value="">Select</option>
                  {item.values ? renderOption(item.values.split(',')) : ""}
          </select>
        )
      } else {
        var type = getUrlVars()["type"];
        var _values;
        if(["view", "update"].indexOf(type) > -1 && assetAttributes.length && !ifTable) {
          var textItem = assetAttributes.filter(function(val, ind) {
            return (val.type == "Select" && item.name == val.key);
          });
          if(textItem && textItem[0])
            _values = textItem[0].value;
        }
        return (
          <div className="col-sm-6" key={index}>
            <div className="row">
              <div className="col-sm-6 label-text">
                <label for={item.name}>{titleCase(item.name)}  {showStart(item.isMandatory)}</label>
              </div>
              <div className="col-sm-6">
                <select name={item.name} size={multi ? 3 : 1} multiple={multi ? true : false}
                  defaultValue={_values || ""} onChange={(e)=>{handleChangeAssetAttr(e, (multi ? "Multiselect" : "Select"), item.name, null, null, multi)}} required={item.isMandatory} disabled={readonly || [true, "true"].indexOf(item.isActive) == -1}>
                  <option value="">Select</option>
                  {item.values ? renderOption(item.values.split(',')) : ""}
                </select>
              </div>
            </div>
          </div>
        );
      }
		}

		/*const showRadioButton = function(item, index, ifTable) {
			return (
				<div className="col-sm-6" key={index}>
					<div className="row">
						<div className="col-sm-6 label-text">
							<label for={item.name}>{titleCase(item.name)}  {showStart(item.isMandatory)}</label>
						</div>
						<div className="col-sm-6">
                {renderRadio(item.values.split(","), item.name, item.isMandatory)}
						</div>
					</div>
				</div>
			);
		}

		const showCheckBox = function(item, index, ifTable) {
			return (
				<div className="col-sm-6" key={index}>
					<div className="row">
						<div className="col-sm-6 label-text">
							<label for={item.name}>{titleCase(item.name)}  {showStart(item.isMandatory)}</label>
						</div>
						<div className="col-sm-6">
							{renderCheckBox(item.values.split(","), item.name)}
						</div>
					</div>
				</div>
			);
		}*/

    const showFile = function(item, index, ifTable) {
      if(ifTable) {
        return (
          <input  name={item.name} type="file" onChange={(e)=>{handleChangeAssetAttr(e, "Table", item.parent, item.name, index)}} required={item.isMandatory} disabled={readonly || [true, "true"].indexOf(item.isActive) == -1} multiple/>
        )
      } else {
        return (<div className="col-sm-6" key={index}>
                <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for={item.name}>{titleCase(item.name)}  {showStart(item.isMandatory)}</label>
                    </div>
                    <div className="col-sm-6">
                        <input  name={item.name} type="file" onChange={(e)=>{handleChangeAssetAttr(e, "File", item.name)}} required={item.isMandatory} disabled={readonly || [true, "true"].indexOf(item.isActive) == -1} multiple/>
                    </div>
                </div>
            </div>)
      }
    }

    const renderNewRows = function(name, item) {
      var tr = "";
      if(!newRows[name]) {
        return tr;
      } else {
        var type = getUrlVars()["type"];
        var tableItems, len;
        if(["view", "update"].indexOf(type) > -1 && assetAttributes.length) {
          tableItems = assetAttributes.filter(function(val, ind) {
            return (val.type == "Table" && name == val.key);
          });
          if(tableItems && tableItems[0] && tableItems[0].value) {
            len = 0;
            for(var i=0; i< tableItems[0].value.length; i++)
              if(!tableItems[0].value[i].new)
                len++;
          }
        }
        return newRows[name].map(function(val, ind) {
          return (<tr>
              <td style={{"padding-top": "12px"}}>{len ? (len+ind+ ( tblSet.removeCustom ? 0 : 1)) : (ind+( tblSet.removeCustom ? 1 : 2))}</td>
              {item.columns.map((itemOne, index) => {
              itemOne.parent = item.name;
              itemOne.isActive = item.isActive;
              return (
                <td>{checkFields(itemOne, (len ? (len+ind) : (ind+1)), true)}</td>
              );
            })}
                {<td>
                  <button className="btn btn-close" onClick={(e) => {removeRow(e, "new", item.name, (len ? len : ind), len ? (len+ind) : (ind+1))}}>Remove</button>
                </td>}
              </tr>)
        })
      }
    }

    const renderOldRows = function(name, item) {
      var type = getUrlVars()["type"];
      if(["view", "update"].indexOf(type) > -1 && assetAttributes.length) {
        var tableItems = assetAttributes.filter(function(val, ind) {
          return (val.type == "Table" && name == val.key);
        });      
        return tableItems.map(function(val, index) {
          return val.value.map(function(itm, ind){
              if(Object.keys(itm).indexOf("new") == -1) {
                var _itms = [];
                for (var _clm in item.columns) {
                  for (var key in itm) {
                    if (item.columns[_clm].name === key) {
                      _itms.push({
                        key: key,
                        value: itm[key]
                      })
                    }
                  }
              };
                return (<tr>
                    <td style={{"padding-top": "12px"}}>{ind+1}</td>
                    {_itms.map(function(_itm, ind2) {
                    for(var i=0; i<item.columns.length; i++) {
                      if(item.columns[i].name == _itm.key) {
                        var newItem = Object.assign({}, item.columns[i]);
                        newItem.values = _itm.value;
                        newItem.parent = name;
                        newItem.isActive = item.isActive;
                        return (
                          <td>{checkFields(newItem, ind, true)}</td>
                        );
                      }
                    }
                })}
                    <td>
                      <button className="btn btn-close" onClick={(e) => {removeRow(e, "old", name, ind)}}>Remove</button>
                    </td>
                    </tr>)
              }
            })
        })
      }
    }

		const showTable = function(item, index) {
        var type = getUrlVars()["type"];

        let tableColumns = function() {
					return item.columns.map((itemOne, index) => {
						return (
							<th key={index}>
									{itemOne.name}
						 	</th>
						)
					})
				}

				let tableRows = function() {
          var rndr = false;
          if(["view", "update"].indexOf(type) > -1 && assetAttributes.length) {
              rndr = assetAttributes.some(function(val, ind) {
                if(val.type == "Table" && item.name == val.key && !val.new) {
                  return true;
                }
              });
          }

          if(!rndr && !tblSet.removeCustom)
  					return (<tr>
                <td  style={{"padding-top": "12px"}}>1</td>
                {item.columns.map((itemOne, index) => {
                itemOne.parent = item.name;
                itemOne.isActive = item.isActive;
                return (
                  <td>{checkFields(itemOne, 0, true)}</td>
                )
              })}
                <td>
                  <button className="btn btn-close" onClick={(e) => {removeRow(e, "custom",item.name,0,0)}}>Remove</button>
                </td>
               </tr>)
				}

        return (
          <div className="col-sm-12" key={index}>
             <div className="form-section row">
                <div className="row">
                   <div className="col-md-8">
                      <h3 className="categoryType">{item.name} {showStart(item.isMandatory)}</h3>
                   </div>
                   <div className="col-md-4 text-right">

                   </div>
                </div>
                <div className="row">
                   <div className="land-table table-responsive">
                      <table className="table table-bordered">
                         <thead>
                            <tr>
                               <th>Sr. No.</th>
                               {tableColumns()}
                               {/*<th>Action</th>*/}
                            </tr>
                         </thead>
                         <tbody>
                            {tableRows()}
                            {renderOldRows(item.name, item)}
                            {renderNewRows(item.name, item)}
                         </tbody>
                      </table>
                   </div>
                </div>
             </div>
             <div className="row text-right">
                <button type="button" className="btn btn-primary" onClick={(e) => {addNewRow(e, item.name)}} disabled={getUrlVars()["type"] == "view" || [true, "true"].indexOf(item.isActive) == -1}>Add</button>
             </div>
          </div>
        )
		}

    const loadModal = function(e) {
      e.preventDefault();
      if(getUrlVars()["type"] == "view") return;
      $("#refModal").modal("show");
    }

    const showStart = function(status) {
        if (status) {
            return(
              <span> * </span>
            )
        }
    }

    const getTodaysDate = function() {
        var now = new Date();
        var month = (now.getMonth() + 1);
        var day = now.getDate();
        if(month < 10)
            month = "0" + month;
        if(day < 10)
            day = "0" + day;
        return (now.getFullYear() + '-' + month + '-' + day);
    }

    const renderIfCapitalized = function(capitalized,ifassetLandIM) {
      if(capitalized) {
        return (
          <div className="row">
              <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for="grossValue">Gross Value</label>
                    </div>
                    <div className="col-sm-6">
                        <input type="number" id="grossValue" name="grossValue" value= {grossValue}
                          onChange={(e)=>{handleChange(e,"grossValue")}} min="1" maxlength="16" disabled={readonly}/>
                    </div>
                  </div>
              </div>
              <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for="accumulatedDepreciation">Accumulated Depreciation</label>
                    </div>
                    <div className="col-sm-6">
                        <input type="number" id="accumulatedDepreciation" name="accumulatedDepreciation" value= {accumulatedDepreciation}
                          onChange={(e)=>{handleChange(e, "accumulatedDepreciation")}} min="1" maxlength="16" disabled={readonly}/>
                    </div>
                  </div>
              </div>
              {ifassetLandIM &&
                <div className="col-sm-6">
                    <div className="row">
                      <div className="col-sm-6 label-text">
                          <label for="marketValue">Market Value(Rs.)</label>
                      </div>
                      <div className="col-sm-6">
                          <input type="text" id="marketValue" name="marketValue" value= {marketValue}
                            onChange={(e)=>{handleChange(e, "marketValue",/^\d+$/)}} min="1" maxlength="16" disabled={readonly}/>
                      </div>
                    </div>
                </div>
              }
          </div>
        )
      }
    }


		const showCodeonUpdate = function(){

			var type = getUrlVars()["type"];
			if(type==="update" || type==="view"){
				return(
					<div className="col-sm-6">
						<div className="row">
							<div className="col-sm-6 label-text">
								<label for="code">  Code </label>
							</div>
							<div className="col-sm-6">
								<input id="code" name="code" value={code} type="text"
									onChange={(e)=>{handleChange(e,"code")}} readonly/>
							</div>
						</div>
					</div>
				)
			}

		}

    const renderRefBody = function() {
      if (references.length > 0) {
        references.sort(function(item1, item2) {
          return item1.code > item2.code ? 1 : item1.code < item2.code ? -1 : 0;
        });
        return references.map((item, index) => {
              return (<tr key={index} onClick={(e) => handleClick(item)}>
                        <td>{index+1}</td>
                        <td>{item.code}</td>
                        <td>{item.name}</td>
                        <td>{item.assetCategory.name}</td>
                        <td>{getNameById(departments, item.department.id)}</td>
                        <td>{item.status}</td>
                        <td data-label="action">
                          <button className="btn btn-close" onClick={(e) => {selectRef(e, item)}}>Select</button>
                        </td>
                      </tr>
              );
        })
      }
    }

    const showDelBtn = function(item) {
      if(getUrlVars()["type"] != "view")
        return (
          <button type="button" className="btn btn-danger btn-close" onClick={(e) => {removeReferenceConfirm(e, item)}}>Delete</button>
        )
    }

    const renderRelatedBody = function() {
      if (relatedAssets.length > 0) {
        relatedAssets.sort(function(item1, item2) {
          return item1.code > item2.code ? 1 : item1.code < item2.code ? -1 : 0;
        });
        return relatedAssets.map((item, index) => {
              return (<tr key={index} onClick={(e) => handleClick(item)}>
                        <td>{index+1}</td>
                        <td>{item.code}</td>
                        <td>{item.name}</td>
                        <td>{item.assetCategory.name}</td>
                        <td>{getNameById(departments, item.department.id)}</td>
                        <td>{item.status}</td>
                        <td>
                          {showDelBtn(item)}
                        </td>
                      </tr>
              );
        })
      }
    }

    const renderRelatedTable = function() {
      if(relatedAssets) {
        return (
          <table id="relatedTable" className="table table-bordered">
              <thead>
              <tr>
                  <th>Sr. No.</th>
                  <th>Code</th>
                  <th>Name</th>
                  <th>Asset Category Type</th>
                  <th>Department</th>
                  <th>Status</th>
                  <th>Action</th>
              </tr>
              </thead>
              <tbody id="tblRef">
                  {
                      renderRelatedBody()
                  }
              </tbody>
         </table>
            )
      }
    }

    const renderRefTable = function() {
      if(references) {
        return (
          <table id="refTable" className="table table-bordered">
              <thead>
              <tr>
                  <th>Sr. No.</th>
                  <th>Code</th>
                  <th>Name</th>
                  <th>Asset Category Type</th>
                  <th>Department</th>
                  <th>Status</th>
                  <th>Action</th>
              </tr>
              </thead>
              <tbody id="tblRef">
                  {
                      renderRefBody()
                  }
              </tbody>
         </table>
            )
      }
    }

    const showRelatedAssetsBtn = function() {
      if(["update", "view"].indexOf(getUrlVars()["type"]) > -1) {
        return (
          <button className="btn btn-submit" onClick={(e)=>{openRelatedAssetMdl(e)}}>Related Assets</button>
        );
      }
    }

    const showAddNewBtn = function() {
      if(getUrlVars()["type"] != "view") {
        return (
          <button type="button" className="btn btn-primary" data-dismiss="modal" onClick={(e) => openNewRelAssetMdl(e)}>Add New</button>
        )
      }
    }

    const renderYearWiseTable = function () {
      return yearWiseDepreciation.map(function(yr, ind) {
        return (
          <tr key={ind}>
            <td>{ind + 1}</td>
            <td>
              <select required value={yr.financialYear} onChange={(e) => {handleYearChange(e, "financialYear", ind)}}  disabled={readonly}>
                <option value="">Select Financial Year</option>
                {financialYears.map((year)=>(
                      <option value={year.finYearRange}>{year.finYearRange}</option>
                    ))}
              </select>
            </td>
            <td>
              <input required type="number" min="1" value={yr.depreciationRate} onChange={(e) => {handleYearChange(e, "depreciationRate", ind)}}  disabled={readonly}/>
            </td>
            <td>
              {ind == yearWiseDepreciation.length-1 && !readonly && <button className="btn btn-close" type="button" onClick={(e) =>{handleAddNewYearRow()}}>
                <span className="glyphicon glyphicon-plus"></span>
              </button>}&nbsp;&nbsp;
              {ind != 0 && !readonly && <button className="btn btn-close" type="button" onClick={(e) => {handleDelYearRow(ind)}}>
                <span className="glyphicon glyphicon-minus"></span>
              </button>}
            </td>
          </tr>
        );
      })
    }

    const showYearWiseDep = function () {
      let self = this;
      if(enableYearWiseDepreciation) {
        return (
          <table className="table table-bordered">
              <thead>
                <tr>
                    <th>Sr. No.</th>
                    <th>Financial Year</th>
                    <th>Depreciation Rate</th>
                    <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {
                  renderYearWiseTable()
                }
              </tbody>

         </table>
        )
      }
    }

    return (
      <div>
        <h3 > {getType()} Asset  </h3>
        {showAlert(error, success)}
        <form onSubmit={(e)=>
            {addOrUpdate(e)}}>
            <div className="form-section">
              <div className="row">
                <div className="col-md-8 col-sm-8">
                  <h3 className="categoryType">Header Details </h3>
                </div>
                <div className="col-md-4 col-sm-4 text-right">
                  {showRelatedAssetsBtn()}
                </div>
              </div>
              <div className="form-section-inner">
                  <div className="row">
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="department">Department <span> *</span></label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="department" required name="department" value={department.id} onChange={(e)=>
                                    {handleChangeTwoLevel(e,"department","id")}} disabled={readonly}>
                                    <option value="">Select Department</option>
                                    {renderOption(this.state.departments)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>
                    {/*<div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="assetCategoryType">Asset Category Type <span> *</span></label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="assetCategoryType" name="assetCategoryType" value={assetCategoryType} required= "true" onChange={(e)=>
                                    {
                                    handleChange(e,"assetCategoryType")}}>
                                    <option value="">Select Asset Category</option>
                                    {renderOption(this.state.asset_category_type)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>*/}
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="assetCategory">Asset Category <span> *</span> </label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="assetCategory" name="assetCategory" required value={assetCategory.id} onChange={(e)=>
                                    {handleChangeTwoLevel(e,"assetCategory","id")}} disabled={readonly}>
                                    <option value="">Select Asset Category</option>
                                    {renderOption(this.state.assetCategories, true)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="description">Date Of Creation<span> *</span> </label>
                          </div>
                          <div className="col-sm-6">
                              <input type="text" id="dateOfCreation" className="datepicker" name="dateOfCreation" value= {dateOfCreation}
                                onChange={(e)=>{handleChange(e,"dateOfCreation")}}  required disabled={readonly || getUrlVars()["type"] == "update"}/>
                          </div>

                        </div>
                    </div>
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="description">Description</label>
                          </div>
                          <div className="col-sm-6">
                              <textarea id="description" name="description" value= {description} maxLength= "100"
                                onChange={(e)=>{handleChange(e,"description")}} max="1024" disabled={readonly}></textarea>
                          </div>
                        </div>
                    </div>
                  </div>

							<div className="row">
								<div className="col-sm-6">
									<div className="row">
										<div className="col-sm-6 label-text">
												<label for="name">Asset Name <span>* </span></label>
											</div>
											<div className="col-sm-6">
												<input id="name" name="name" value={name} type="text" maxLength= "60"
													onChange={(e)=>{handleChange(e, "name")}} required disabled={readonly}/>
											</div>
										</div>
									</div>
									<div className="col-sm-6">
										<div className="row">
											<div className="col-sm-6 label-text">
												<label for="modeOfAcquisition">Mode Of Acquisition <span>* </span></label>
											</div>
											<div className="col-sm-6">
											<div className="styled-select">
												<select id="modeOfAcquisition" name="modeOfAcquisition" required value={modeOfAcquisition} onChange={(e)=>
														{handleChange(e,"modeOfAcquisition") }} disabled={readonly}>
														<option value="">Select Mode Of Acquisition</option>
														{renderOption(acquisitionList)}
												</select>
											</div>
										</div>
										</div>
									</div>
								</div>
                <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="assetReferenceName">Asset Reference </label>
                        </div>
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-xs-10">
                            <input id="assetReferenceName" name="assetReferenceName" value={assetReferenceName} type="text" disabled/>
                          </div>
                          <div className="col-xs-2">
                            <button className="btn btn-close" onClick={(e) => {loadModal(e)}} disabled={getUrlVars()["type"] == "view"}>
                              <span className="glyphicon glyphicon-search"></span>
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
									{showCodeonUpdate()}
                </div>
                <div className="row">
                  {/* <div className="col-sm-6">
                    <div className="row">
                      <div className="col-sm-6 label-text">
                        <label for="name">Enable Year Wise Depreciation </label>
                      </div>
                      <div className="col-sm-6 label-view-text">
                        <input id="enableYearWiseDepreciation" name="enableYearWiseDepreciation" value={enableYearWiseDepreciation} type="checkbox"
                          onChange={(e)=>{handleChange(e, "enableYearWiseDepreciation")}} disabled={readonly} checked={[true, "true"].indexOf(enableYearWiseDepreciation) > -1}/>
                      </div>
                    </div>
                  </div> */}
                  {/* {!enableYearWiseDepreciation &&  */}
                  <div className="col-sm-6" style={{"display": !enableYearWiseDepreciation ? "block" : "none"}}>
                      <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="name">Depreciation Rate </label>
                        </div>
                        <div className="col-sm-6 label-view-text">
                          <input id="depreciationRate" name="depreciationRate" value={depreciationRate} type="number" step=".01"
                            onChange={(e)=>{handleChange(e, "depreciationRate")}} min="0" disabled={readonly} />
                        </div>
                      </div>
                    </div>
                    
                    <div className="col-sm-6" style={{ display: this.state.readOnly ? 'none' : 'block' }}>
                      <div className="row">
                        <div className="col-sm-6 label-text">
                          <label>Attach Documents</label>
                        </div>
                        <div className="col-sm-6">
                          <div>
                            <input type="file" multiple onChange={(e) => handleChange(e, "documents")} />
                          </div>
                        </div>
                      </div>
                    </div>
                  {/* } */}
                </div>
                <div className="row">
  									<div className="col-sm-6">
  										<div className="row">
  											<div className="col-sm-6 label-text">
  												<label for="function">Function <span>* </span></label>
  											</div>
  											<div className="col-sm-6">
  											<div className="styled-select">
  												<select id="function" name="function" required value={this.state.assetSet.function} onChange={(e)=>
  														{handleChange(e,"function") }} disabled={readonly}>
  														<option value="">Select Function</option>
                              {functions && functions.map((func, index)=>(
                                <option value={func.code}>{`${func.code} - ${func.name}`}</option>
                              ))}
  												</select>
  											</div>
  										</div>
  										</div>
  									</div>
  								</div>
                <div className="col-sm-6">
                    <div className="row">
                      <div className="col-sm-6 label-text">
                        <label>Scheme </label>
                      </div>
                      <div className="col-sm-6" style={{display: this.state.readOnly ? 'none' : 'block' }}>
                        <div>
                          <select value={scheme} onChange={(e) => handleChange(e, "scheme")}>
                            <option value="">Select Scheme</option>
                            {renderOption(schemes)}
                          </select>
                        </div>
                      </div>
                      <div className="col-sm-6 label-view-text" style={{display: this.state.readOnly ? 'block' : 'none' }}>
                          <label>{scheme ? getNameById(schemes, scheme) : ""}</label>
                      </div>
                  </div>
                </div>
              </div>
              <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                      <div className="col-sm-6 label-text">
                        <label>Sub Scheme </label>
                      </div>
                      <div className="col-sm-6" style={{display: this.state.readOnly ? 'none' : 'block' }}>
                        <div>
                          <select value={subScheme} onChange={(e) => handleChange(e, "subScheme")}>
                            <option value="">Select Sub Scheme</option>
                            {renderOption(subSchemes)}
                          </select>
                        </div>
                      </div>
                      <div className="col-sm-6 label-view-text" style={{display: this.state.readOnly ? 'block' : 'none' }}>
                          <label>{subScheme ? getNameById(subSchemes, subScheme) : ""}</label>
                      </div>
                  </div>
                </div>
                {/* {showYearWiseDep()} */}
            </div>
						  </div>
            <div className="form-section" id="allotteeDetailsBlock">
              <h3 className="categoryType">Location Details </h3>
              <div className="form-section-inner">
                  <div className="row">
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="locality"> Location <span> * </span> </label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="locality" name="locality" required="true" value={locationDetails.locality}
                                    onChange={(e)=>
                                    {handleChangeTwoLevel(e,"locationDetails","locality")}} disabled={readonly}>
                                    <option value="">Choose locality</option>
                                    {renderOption(this.state.locality)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="revenueWard"> Revenue Ward  </label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="revenueWard" name="revenueWard" value={locationDetails.revenueWard}
                                    onChange={(e)=>
                                    {  handleChangeTwoLevel(e,"locationDetails","revenueWard")}} disabled={readonly}>
                                    <option value="">Choose Revenue Ward</option>
                                    {renderOption(this.state.revenueWards)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="block"> Block Number  </label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="block" name="block" value={locationDetails.block}
                                    onChange={(e)=>
                                    {  handleChangeTwoLevel(e,"locationDetails","block")}} disabled={readonly}>
                                    <option value="">Choose Block</option>
                                    {renderOption(this.state.revenueBlock)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="street"> Street  </label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="street" name="street" value={locationDetails.street}
                                    onChange={(e)=>
                                    {  handleChangeTwoLevel(e,"locationDetails","street")}} disabled={readonly}>
                                    <option value="">Choose Street</option>
                                    {renderOption(this.state.street)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="electionWard"> Election Ward No  </label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="electionWard" name="electionWard" value={locationDetails.electionWard}
                                    onChange={(e)=>
                                    { handleChangeTwoLevel(e,"locationDetails","electionWard")}} disabled={readonly}>
                                    <option value="">Choose Election Wards</option>
                                    {renderOption(this.state.electionwards)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="doorno"> Door Number  </label>
                          </div>
                          <div className="col-sm-6">
                              <input type="text" name="doorNo" id= "doorNo" value= {locationDetails.doorNo} maxLength= "60"
                                onChange={(e)=>{handleChangeTwoLevel(e,"locationDetails","doorNo")}} disabled={readonly}/>
                          </div>
                        </div>
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="zone"> Zone Number  </label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="zone" name="zone" value={locationDetails.zone}
                                    onChange={(e)=>
                                    { handleChangeTwoLevel(e,"locationDetails","zone")}}>
                                    <option value="">Choose Zone Number</option>
                                    {renderOption(this.state.revenueZone)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>

                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="pin">PIN Code</label>
                          </div>
                          <div className="col-sm-6">
                              <input type="text" name="pinCode" id="pinCode" value={locationDetails.pinCode}
                                onChange={(e)=>{handleChangeTwoLevel(e,"locationDetails","pinCode")}} pattern="[0-9]{6}" title="Six number pin code" disabled={readonly}/>
                          </div>
                        </div>
                    </div>

                  {this.state.ifassetLandIM &&
                    <div className="col-sm-6">
                        <div className="row">
                           <div className="col-sm-6 label-text">
                               <label for="surveyNumber"> Survey Number </label>
                           </div>
                           <div className="col-sm-6">
                               <input type="text" name="surveyNumber" id= "surveyNumber" value= {surveyNumber} maxLength= "15"
                                 onChange={(e)=>{handleChange(e,"surveyNumber")}} disabled={readonly}/>
                           </div>
                       </div>
                    </div>
                  }

                  </div>
              </div>
            </div>
            {showCategorySection()}
            <div className="form-section" id="allotteeDetailsBlock">
              <h3 className="categoryType">Value Summary </h3>
              <div className="form-section-inner">
                  <div className="row">
                      <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="status"> Asset Status <span> *</span></label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="status" name="status" value={status}
                                    onChange={(e)=>
                                    { handleChange(e,"status")}} disabled={readonly} required>
                                    <option value="">Choose Status</option>
                                    {renderOption(this.state.statusList, "", true)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>
                  </div>
                  {renderIfCapitalized(this.state.capitalized, this.state.ifassetLandIM)}
              </div>
            </div>
            <br/>
            {showAttachedFiles()}
            <div className="text-center">
              {showActionButton()} &nbsp;&nbsp;
              <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
            </div>
        </form>
        <div className="modal fade" tabindex="-1" role="dialog" id="refModal">
          <div className="modal-dialog modal-lg" role="document">
            <div className="modal-content">
              <div className="modal-header">
                <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 className="modal-title">Asset Reference Search</h4>
              </div>
              <div className="modal-body">
                <form onSubmit={(e) => {handleRefSearch(e)}}>
                    <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="refSet.department">Department </label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <select id="refSet.department" name="refSet.department" value={refSet.department} onChange={(e) => {handleReferenceChange(e, "department")}}>
                                    <option value="">Select Department</option>
                                    {renderOption(this.state.departments)}
                              </select>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="refSet.assetCategory">Asset Category </label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <select id="refSet.assetCategory" name="refSet.assetCategory" value={refSet.assetCategory} onChange={(e) => {handleReferenceChange(e, "assetCategory")}}>
                                    <option value="">Select Asset Category</option>
                                    {renderOption(this.state.assetCategories)}
                                </select>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="refSet.code">Code </label>
                            </div>
                            <div className="col-sm-6">
                              <input id="refSet.code" name="refSet.code" value={refSet.code} type="text" onChange={(e) => {handleReferenceChange(e, "code")}}/>
                            <div>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="refSet.name">Name </label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <input id="refSet.name" name="refSet.name" value={refSet.name} type="text" onChange={(e) => {handleReferenceChange(e, "name")}}/>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="refSet.status">Status </label>
                            </div>
                            <div className="col-sm-6">
                              <select id="refSet.status" name="refSet.status" value={refSet.status} onChange={(e) => {handleReferenceChange(e, "status")}}>
                                    <option value="">Select Status</option>
                                    {renderOption(this.state.statusList, "", true)}
                              </select>
                            <div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="row text-center">
                      <button type="submit" className="btn btn-submit">Search</button>
                    </div>
                  </form>
                    <br/>
                    {renderRefTable()}
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-default" data-dismiss="modal">Close</button>
              </div>
            </div>
          </div>
        </div>
        <div className="modal fade" tabindex="-1" role="dialog" id="relatedAssetsModal">
          <div className="modal-dialog modal-lg" role="document">
            <div className="modal-content">
              <div className="modal-header">
                <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 className="modal-title">Related Assets</h4>
              </div>
              <div className="modal-body">
                {renderRelatedTable()}
              </div>
              <div className="modal-footer">
                {showAddNewBtn()}
                <button type="button" className="btn btn-default" data-dismiss="modal">Close</button>
              </div>
            </div>
          </div>
        </div>
        <div className="modal fade" tabindex="-1" role="dialog" id="confirmDeleteMdl">
          <div className="modal-dialog" role="document">
            <div className="modal-content">
              <div className="modal-header">
                <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 className="modal-title">Confirm</h4>
              </div>
              <div className="modal-body">
                Are you sure you want to continue ?
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-primary" onClick={(e) => {removeReference(e)}}>Yes</button>
                <button type="button" className="btn btn-default" data-dismiss="modal">Close</button>
              </div>
            </div>
          </div>
        </div>
        <div className="modal fade" tabindex="-1" role="dialog" id="newRelAssetMdl">
          <div className="modal-dialog modal-lg" role="document">
            <div className="modal-content">
              <div className="modal-header">
                <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 className="modal-title">Add New Related Asset</h4>
              </div>
              <div className="modal-body">
                <form onSubmit={(e) => {handleRefSearch(e)}}>
                    <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="refSet.department">Department </label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <select id="refSet.department" name="refSet.department" value={refSet.department} onChange={(e) => {handleReferenceChange(e, "department")}}>
                                    <option value="">Select Department</option>
                                    {renderOption(this.state.departments)}
                              </select>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="refSet.assetCategory">Asset Category </label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <select id="refSet.assetCategory" name="refSet.assetCategory" value={refSet.assetCategory} onChange={(e) => {handleReferenceChange(e, "assetCategory")}}>
                                    <option value="">Select Asset Category</option>
                                    {renderOption(this.state.assetCategories)}
                                </select>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="refSet.code">Code </label>
                            </div>
                            <div className="col-sm-6">
                              <input id="refSet.code" name="refSet.code" value={refSet.code} type="text" onChange={(e) => {handleReferenceChange(e, "code")}}/>
                            <div>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="refSet.name">Name </label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <input id="refSet.name" name="refSet.name" value={refSet.name} type="text" onChange={(e) => {handleReferenceChange(e, "name")}}/>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="refSet.status">Status </label>
                            </div>
                            <div className="col-sm-6">
                              <select id="refSet.status" name="refSet.status" value={refSet.status} onChange={(e) => {handleReferenceChange(e, "status")}}>
                                    <option value="">Select Status</option>
                                    {renderOption(this.state.statusList, "", true)}
                              </select>
                            <div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="row text-center">
                      <button type="submit" className="btn btn-submit">Search</button>
                    </div>
                  </form>
                    <br/>
                    {renderRefTable()}
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-default" data-dismiss="modal">Close</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}


ReactDOM.render(
  <CreateAsset />,
  document.getElementById('root')
);


// //
// //
//
//
//
// //
//
//
//
//     //
//   <div className="form-section" id="agreementDetailsBlockTemplateThree">
//   <h3 className="categoryType">Shopping Complex  Details </h3>
//   <div className="form-section-inner">
//
//         <div className="row">
//         <div className="col-sm-6">
//         <div className="row">
//         <div className="col-sm-6 label-text">
//                   <label for="acuquisition">MOde Of Acquisition <span> * </span> </label>
//          </div>
//          <div className="col-sm-6">
//           <div className="styled-select">
//                       <select name="acuquisition" id="acuquisition">
//                     <option>Purchase</option>
//                     <option>Tender</option>
//                     <option>Connstruction</option>
//                     <option value="Direct">Donation</option>
//
//                     </select>
//             </div>
//             </div>
//             </div>
//             </div>
//
//               <div className="col-sm-6">
//               <div className="row">
//               <div className="col-sm-6 label-text">
//                     <label for="name">Shopping COmplex Name.</label>
//               </div>
//               <div className="col-sm-6">
//                     <input type="text" name="name" id="name" />
//               </div>
//               </div>
//               </div>
//               </div>
//
//               <div className="row">
//               <div className="col-sm-6">
//               <div className="row">
//               <div className="col-sm-6 label-text">
//                   <label for="complexNo">Shopping Complex No  </label>
//               </div>
//               <div className="col-sm-6">
//                   <input type="text" name="complexNO" id="complexNO" />
//               </div>
//               </div>
//               </div>
//
//               <div className="col-sm-6">
//               <div className="row">
//               <div className="col-sm-6 label-text">
//                       <label for="doorNo">Door No  </label>
//               </div>
//               <div className="col-sm-6">
//                 <input type="number" name="doorNO" id="doorNO" />
//               </div>
//               </div>
//               </div>
//
//               <div className="row">
//               <div className="col-sm-6">
//               <div className="row">
//               <div className="col-sm-6 label-text">
//                     <label for="complexNo"> Number of floor  </label>
//               </div>
//               <div className="col-sm-6">
//                     <input type="text" name="floorNO" id="floorNO" />
//               </div>
//               </div>
//               </div>
//
//               <div className="col-sm-6">
//               <div className="row">
//               <div className="col-sm-6 label-text">
//                   <label for="noofShop">Total Number of Shop  </label>
//             </div>
//             <div className="col-sm-6">
//                 <input type="text" name="noofShop" id="noofShop" />
//             </div>
//             </div>
//             </div>
//             </div>
//
//             <div className="row">
//             <div className="col-sm-6">
//             <div className="row">
//             <div className="col-sm-6 label-text">
//                 <label for="floorNo">Floor No </label>
//             </div>
//             <div className="col-sm-6">
//             <div className="styled-select">
//                   <select name="floorNo" id="floorNo">
//                   <option>1</option>
//                   <option>2</option>
//                   <option>3</option>
//                   <option value="Direct">4</option>
//
//                 </select>
//             </div>
//             </div>
//             </div>
//             </div>
//             <div className="col-sm-6">
//             <div className="row">
//             <div className="col-sm-6 label-text">
//                   <label for="noShop">Number Of Shop.</label>
//             </div>
//             <div className="col-sm-6">
//                   <input type="text" name="noShop" id="noShop" />
//             </div>
//             </div>
//             </div>
//             </div>
//
//             <div className="row">
//             <div className="col-sm-6">
//             <div className="row">
//             <div className="col-sm-6 label-text">
//                 <label for="status">Status <span> * </span> </label>
//             </div>
//             <div className="col-sm-6">
//             <div className="styled-select">
//                   <select name="status" id="status">
//                   <option>1</option>
//                   <option>2</option>
//                   <option>3</option>
//                   <option value="Direct">4</option>
//
//                 </select>
//           </div>
//           </div>
//           </div>
//           </div>
//           <div className="col-sm-6">
//           <div className="row">
//           <div className="col-sm-6 label-text">
//                   <label for="value">Value</label>
//            </div>
//            <div className="col-sm-6">
//                   <input type="text" name="value" id="value" />
//           </div>
//           </div>
//           </div>
//           </div>
//
//           <div className="row">
//           <div className="col-sm-6">
//           <div className="row">
//           <div className="col-sm-6 label-text">
//               <label for="remarks">Remarks </label>
//         </div>
//         <div className="col-sm-6">
//               <textarea name="remarks" id="remarks"></textarea>
//       </div>
//       </div>
//       </div>
//       </div>
//
//       <div className="text-center">
//           <button type="button" className="btn btn-submit" >Create</button>
//           <button type="button" className="btn btn-submit">close</button>
//       </div>
//       </div>
//       </div>
