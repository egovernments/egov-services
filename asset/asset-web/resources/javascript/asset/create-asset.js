// const cutf=[
//       {
//         "name": "Description",
//         "type": "SingleValueList",
//         "isActive": null,
//         "isMandatory": null,
//         "values": "ABC,BCD",
//         "localText": null,
//         "regExFormate": null,
//         "url": null,
//         "order": null,
//         "columns": null
//       },
//       {
//         "name": "Floor Details",
//         "type": "Table",
//         "isActive": null,
//         "isMandatory": null,
//         "values": null,
//         "localText": null,
//         "regExFormate": null,
//         "url": null,
//         "order": null,
//         "columns": [
//           {
//             "name": "column1",
//             "type": "string",
//             "isActive": null,
//             "isMandatory": null,
//             "values": null,
//             "localText": null,
//             "regExFormate": null,
//             "url": null,
//             "order": null,
//             "columns": null
//           },
//           {
//             "name": "column2",
//             "type": "number",
//             "isActive": null,
//             "isMandatory": null,
//             "values": null,
//             "localText": null,
//             "regExFormate": null,
//             "url": null,
//             "order": null,
//             "columns": null
//           },
//           {
//             "name": "column2",
//             "type": "number",
//             "isActive": null,
//             "isMandatory": null,
//             "values": null,
//             "localText": null,
//             "regExFormate": null,
//             "url": null,
//             "order": null,
//             "columns": null
//           }
//         ]
//       },
//       {
//         "name": "Details",
//         "type": "text",
//         "isActive": null,
//         "isMandatory": null,
//         "values": null,
//         "localText": null,
//         "regExFormate": null,
//         "url": null,
//         "order": null,
//         "columns": null
//       },
//       {
//         "name": "Amenities Details",
//         "type": "Table",
//         "isActive": null,
//         "isMandatory": null,
//         "values": null,
//         "localText": null,
//         "regExFormate": null,
//         "url": null,
//         "order": null,
//         "columns": [
//           {
//             "name": "column11",
//             "type": "string",
//             "isActive": null,
//             "isMandatory": null,
//             "values": null,
//             "localText": null,
//             "regExFormate": null,
//             "url": null,
//             "order": null,
//             "columns": null
//           },
//           {
//             "name": "column22",
//             "type": "number",
//             "isActive": null,
//             "isMandatory": null,
//             "values": null,
//             "localText": null,
//             "regExFormate": null,
//             "url": null,
//             "order": null,
//             "columns": null
//           },
//           {
//             "name": "column23",
//             "type": "number",
//             "isActive": null,
//             "isMandatory": null,
//             "values": null,
//             "localText": null,
//             "regExFormate": null,
//             "url": null,
//             "order": null,
//             "columns": null
//           }
//         ]
//       }
//     ]

const titleCase = (field) => {
	var newField = field[0].toUpperCase();
	for(let i=1; i<field.length; i++) {
      if(field[i-1] != " " && field[i] != " ") {
      	newField += field.charAt(i).toLowerCase();
      } else {
        newField += field[i]
      }
    }
    return newField;
}

const makeAjaxUpload = function(file, cb) {
    let formData = new FormData();
    formData.append("jurisdictionId", "ap.public");
    formData.append("module", "PGR");
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
}

const uploadFiles = function(body, cb) {
    if(body.Asset.properties && Object.keys(body.Asset.properties).length) {
        var counter1 = Object.keys(body.Asset.properties).length;
        var breakout = 0;
        for(var key in body.Asset.properties) {
            if(body.Asset.properties[key].constructor == FileList) {
                var counter = body.Asset.properties[key].length;
                for(let j=0; j<body.Asset.properties[key].length; j++) {
                    makeAjaxUpload(body.Asset.properties[key][j], function(err, res) {
                        if (breakout == 1)
                            return;
                        else if (err) {
                            cb(err);
                            breakout = 1;
                        } else {
                            counter--;
                            body.Asset.properties[key][j] = `/filestore/v1/files/id?fileStoreId=${res.files[0].fileStoreId}`;
                            if(counter == 0) {
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
    "modeOfAcquisition": "ACQUIRED",
    "status": "CWIP",
    "grossValue": "",
    "accumulatedDepreciation": "",
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
    "remarks": "",
    "length": "",
    "width": "",
    "totalArea": "",
    "assetRefrance": "",
    "assetAttributes":[]
};

class CreateAsset extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
        list: [],
        assetSet: Object.assign({}, defaultAssetSetState),
        assetCategories,
        locality,
        electionwards,
        departments,
        acquisitionList,
        revenueZone,
        street,
        revenueWards,
        revenueBlock,
        customFields:[],
        statusList,
        asset_category_type,
        capitalized: false,
        error: "",
        success: "",
				assetAttribute:{},
        readonly: false
    }
    this.handleChange = this.handleChange.bind(this);
    this.handleChangeTwoLevel = this.handleChangeTwoLevel.bind(this);
    this.addOrUpdate = this.addOrUpdate.bind(this);
    this.handleChangeAssetAttr = this.handleChangeAssetAttr.bind(this);

  }
  close() {
      // widow.close();
      open(location, '_self').close();
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

  handleChange(e, name) {
      if(name === "status") {
        this.state.capitalized = ( e.target.value === "CAPITALIZED");
      }

      this.setState({
          assetSet: {
              ...this.state.assetSet,
              [name]: e.target.value
          }
      })

  }

  addOrUpdate(e) {
      e.preventDefault();
      this.setState({
        ...this.state,
        error: "",
        success: ""
      })
      var tempInfo = this.state.assetSet, _this = this, type = getUrlVars()["type"];
      var body = {
          "RequestInfo": requestInfo,
          "Asset": tempInfo
      };

      uploadFiles(body, function(err, _body) {
        if(err) {
            showError(err);
        } else {
            var response = $.ajax({
                url: baseUrl + "/asset-services/assets/" + (type == "update" ? ("_update/"+ _this.state.assetSet.code) : "_create") + "?tenantId=" + tenantId,
                type: 'POST',
                dataType: 'json',
                data: JSON.ify(_body),
                async: false,
                contentType: 'application/json',
                headers:{
                    'auth-token' :authToken
                }
            });
            if (response["status"] === 201 || response["status"] == 200 || response["status"] == 204) {
                window.location.href=`app/asset/create-asset-ack.html?name=${tempInfo.name}&type=`;
            } else {
                this.setState({
                    ...this.state,
                    error: response["statusText"]
                })
            }
        }
      })
  }

  handleChangeAssetAttr(e, type, key, col, ind, multi) {
    let attr = Object.assign([], this.state.assetAttributes);
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
                [col]: values
              };
            }
          } else {
            if(ind)
              attr[i].value[ind][col] = e.target.value;
            else {
              attr[i].value.push({
                [col]: e.target.value
              })
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
          } else {
            attr[i].value = e.target.value;
          }
        }

        this.setState({
          assetAttributes: Object.assign([], attr)
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
          [col]: values
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
    } else {
      attr.push({
          key: key,
          type: type,
          value: e.target.value
        })
    }
    this.setState({
      assetAttributes: Object.assign([], attr)
    });
  }

  handleChangeTwoLevel(e, pName, name, multi) {
      let text, type, codeNo, innerJSON;
      if (pName == "assetCategory") {
          let el = document.getElementById('assetCategory');
          text = el.options[el.selectedIndex].innerHTML;
          this.setState({
              customFields: this.getCategory(e.target.value),
              properties: {}
          })
          e.target.value = parseInt(e.target.value);
          for(var i=0;i< this.state.assetCategories.length; i++){
            if (e.target.value==this.state.assetCategories[i].id) {
                type = this.state.assetCategories[i].assetCategoryType;
                codeNo = this.state.assetCategories[i].code;
                break;
            }
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
            [name]: values;
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
              [pName]: innerJSON
          }
      })

  }


  componentDidMount() {
      var type = getUrlVars()["type"], _this = this;
      var id = getUrlVars()["id"];
      $(document).on('focus',".custom-date-picker", function(){
            $(this).datetimepicker({
                format: 'DD/MM/YYYY'
            });
      });

      $('#dateOfCreation').datetimepicker({
          format: 'DD/MM/YYYY',
          maxDate: new Date()
      });

      $('#dateOfCreation').on("dp.change", function(e) {
         _this.setState({
            assetSet: {
                ..._this.state.assetSet,
                "dateOfCreation":$("#dateOfCreation").val()
            }
         })
      });

      this.setState({
          readonly: (type === "view")
      });

      if (type === "view" || type === "update") {
          let asset = getCommonMasterById("asset-services", "assets", "Assets", id).responseJSON["Assets"][0];
          this.setState({
              assetSet: asset
          });

          if(asset.status == "CAPITALIZED") {
              this.setState({
                  capitalized: true
              })
          }

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
      }
  }

  render() {
    let {handleChange, addOrUpdate, handleChangeTwoLevel}=this;
    let {isSearchClicked, list, customFields, error, success, acquisitionList, readonly}=this.state;
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
      status
  	} = this.state.assetSet;
    let mode = getUrlVars()["type"];

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

    const renderRadio = function(list, name, isMandatory) {
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

    }

    const renderOption = function(list)
    {
        if(list)
        {
            if (list.length) {
              return list.map((item, ind)=>
              {
                  if(typeof item == "object") {
                    return (<option key={ind} value={item.id}>
                          {item.name}
                    </option>)
                  } else {
                    return (<option key={ind} value={item}>
                          {item}
                    </option>)
                  }
              })

            } else {
              return Object.keys(list).map((k, index)=>
              {
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
					let customFieldsDisply=function(){
							return customFields.map((item, index) => {
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
			return (
				<div className="col-sm-6" key={index}>
					<div className="row">
						<div className="col-sm-6 label-text">
							<label for={item.name}>{titleCase(item.name)}  {showStart(item.isMandatory)}</label>
						</div>
						<div className="col-sm-6">
							<input  name={item.name} type="text" maxLength= "200"
								defaultValue={item.values} onChange={(e)=>{handleChangeAssetAttr(e, "Text", (ifTable ? item.parent : item.name), (ifTable ? item.name : "") (ifTable ? index : ""))}} required={item.isMandatory} disabled={readonly}/>
						</div>
					</div>
				</div>
			);
		}

		const showSelect = function(item, index, multi, ifTable) {
			return (
				<div className="col-sm-6" key={index}>
					<div className="row">
						<div className="col-sm-6 label-text">
							<label for={item.name}>{titleCase(item.name)}  {showStart(item.isMandatory)}</label>
						</div>
						<div className="col-sm-6">
							<select name={item.name} multiple={multi ? true : false}
								onChange={(e)=>{handleChangeAssetAttr(e, (multi ? "Multiselect" : "Select"), (ifTable ? item.parent : item.name), (ifTable ? item.name : "") (ifTable ? index : ""), multi)}} required={item.isMandatory} disabled={readonly}>
								<option value="">Select</option>
								{renderOption(item.values.split(','))}
          </select>
						</div>
					</div>
				</div>
			);
		}

		const showRadioButton = function(item, index, ifTable) {
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
		}

    const showDatePicker = function(item, index, ifTable) {
            return (<div className="col-sm-6" key={index}>
                <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for={item.name}>{titleCase(item.name)}  {showStart(item.isMandatory)}</label>
                    </div>
                    <div className="col-sm-6">
                        <input  className="custom-date-picker" name={item.name} type="text" defaultValue={item.values} onChange={(e)=>{handleChangeAssetAttr(e, "Date", (ifTable ? item.parent : item.name), (ifTable ? item.name : "") (ifTable ? index : ""))}} required={item.isMandatory} disabled={readonly}/>
                    </div>
                </div>
            </div>)
        }

    const showFile = function(item, index, ifTable) {
            return (<div className="col-sm-6" key={index}>
                <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for={item.name}>{titleCase(item.name)}  {showStart(item.isMandatory)}</label>
                    </div>
                    <div className="col-sm-6">
                        <input  name={item.name} type="file" onChange={(e)=>{handleChangeAssetAttr(e, "File", (ifTable ? item.parent : item.name), (ifTable ? item.name : "") (ifTable ? index : ""))}} required={item.isMandatory} disabled={readonly} multiple/>
                    </div>
                </div>
            </div>)
        }

		const showTable=function(item, index)
		{
				let tableColumns =function()
				{
					return item.columns.map((itemOne,index)=>
					{
						return (
							<th key={index}>
									{itemOne.name}
						 	</th>
						)
					})
				}

				let tableRows =function()
				{
					return item.columns.map((itemOne,index)=>
					{
						itemOne.parent = item.name;
						return checkFields(itemOne, index, true);
					})
				}

				// console.log(table);
				return (

					<div className="col-sm-12" key={index}>
						<div className="form-section" >
					  <h3 className="categoryType">{item.name}</h3>


						<div className="row">
						<div className="land-table table-responsive">
								<table className="table table-bordered">
										<thead>
											<tr>
													{tableColumns()}
													<th>Action</th>
											</tr>
										</thead>
										<tbody>

										</tbody>
								</table>
						</div>
						</div>

						<div className="form-section" >
							<h3 className="categoryType">Add </h3>
								<div className="form-section-inner">
										<div className="row">

											{tableRows()}

											<button type="button" className="btn btn-primary" >Add</button>
											{/*<button type="button" className="btn btn-default">Reset</button>*/}

										</div>
									</div>
								</div>

					</div>
					</div>
				)


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

    const renderIfCapitalized = function(capitalized) {
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
                          onChange={(e)=>{handleChange(e,"grossValue")}} min="1" maxlength="16" step="0.01" disabled={readonly}/>
                    </div>
                  </div>
              </div>
              <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for="accumulatedDepreciation">Gross Value</label>
                    </div>
                    <div className="col-sm-6">
                        <input type="number" id="accumulatedDepreciation" name="accumulatedDepreciation" value= {accumulatedDepreciation}
                          onChange={(e)=>{handleChange(e,"accumulatedDepreciation")}} min="1" maxlength="16" step="0.01" disabled={readonly}/>
                    </div>
                  </div>
              </div>
          </div>
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
              <h3 className="categoryType">Header Details </h3>
              <div className="form-section-inner">
                  <div className="row">
                    <div className="col-sm-6">
                        <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="department">Department <span> *</span></label>
                          </div>
                          <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="department" name="department" required value={department.id} onChange={(e)=>
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
                              <label for="description">Date Of Creation</label>
                          </div>
                          <div className="col-sm-6">
                              <input type="text" id="dateOfCreation" name="dateOfCreation" value= {dateOfCreation}
                                onChange={(e)=>{handleChange(e,"dateOfCreation")}} max={getTodaysDate()} disabled={readonly}/>
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
                                    {renderOption(this.state.statusList)}
                                </select>
                              </div>
                          </div>
                        </div>
                    </div>
                  </div>
                  {renderIfCapitalized(this.state.capitalized)}
              </div>
            </div>
            <div className="text-center">
              {showActionButton()} &nbsp;&nbsp;
              <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
            </div>
        </form>





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
