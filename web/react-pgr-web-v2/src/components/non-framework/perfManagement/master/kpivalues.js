import React, {Component} from 'react';
import {connect} from 'react-redux';

import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField'
import RaisedButton from 'material-ui/RaisedButton';
import { RadioButton, RadioButtonGroup } from 'material-ui/RadioButton';
import Menu from 'material-ui/Menu';
import MenuItem from 'material-ui/MenuItem';


import {Collapse,Grid, Row, Col, Table, DropdownButton,Button,OverlayTrigger,Popover,Glyphicon} from 'react-bootstrap';

import {Card, CardHeader, CardText} from 'material-ui/Card';

import _ from "lodash";

import {translate} from '../../../common/common';
import Api from '../../../../api/api';
import UiButton from '../../../framework/components/UiButton';
import {fileUpload} from '../../../framework/utility/utility';



class kpivalues  extends Component{
    constructor(props) {
      super(props);

    this.state = {
            showResult: false,
            data: [],
            header:[],
            KPIs:[],
            Department:[],
            FinantialYear:[],
            collapse:[],

            selectedDeptId:'',
            selectedFinYear:'',
            selectedKpiCode:'',

            viewFirst:true,
            viewSecond:false,
            viewThird:false,
            viewFour:false,
            next:true,
            prev:false,
            KPIResult:[],
            documents:[],

            uploadPane:[],
            anchorEl:[],
            response:[],

            allowedMax:1,            
            search:false,
            errorInput:'No record found'
     };

     //this.handleChange = this.handleChange.bind(this);
     //this.handleSubmit = this.handleSubmit.bind(this);
     //this.searchKPIValues = this.searchKPIValues.bind(this);
     //this.clearSearch = this.clearSearch.bind(this);
     //this.nextSection = this.nextSection.bind(this);
     this.header = this.header.bind(this);

     this.handleSubmit = this.handleSubmit.bind(this);
     this.handleFile   = this.handleFile.bind(this);
     

     this.prepareBodyobject = this.prepareBodyobject.bind(this);
     
     this.renderFileObject = this.renderFileObject.bind(this);
     this.handleChange = this.handleChange.bind(this);
     this.nextSection = this.nextSection.bind(this);
     this.prevSection = this.prevSection.bind(this);
     this.clearSearch = this.clearSearch.bind(this);
     

   }



   componentDidMount() {

     var url = "egov-mdms-service/v1/_get?moduleName=common-masters&masterName=Department";
     var query = [];
     let self = this;
     Api.commonApiPost(url, query, {}, false, false).then(function(res){
         if (res.MdmsRes) {
           //res.MdmsRes.`common-masters
           self.setState({Department:res.MdmsRes['common-masters'].Department});
           //self.state.Department = res.MdmsRes['common-masters'].Department;
         }

       }, function(err){

       });


        url = "egf-master/financialyears/_search";
        query = [];
       Api.commonApiPost(url, query, {}, false, false).then(function(res){
           if (res) {

             //res.MdmsRes.`common-masters
             self.setState({FinantialYear:res.financialYears});
             //self.state.Department = res.MdmsRes['common-masters'].Department;
           }

         }, function(err){

         });

   }




   nextSection()
   {
     this.setState({prev:true})
     if (this.state.viewFirst) {
       this.setState({viewFirst:false,viewSecond:true})
     }
     if (this.state.viewSecond) {
       this.setState({viewSecond:false,viewThird:true,next:false})
     }
   }
   prevSection()
   {
     this.setState({next:true})
     if (this.state.viewThird) {
       this.setState({viewThird:false,viewSecond:true})
     }
     if (this.state.viewSecond) {
       this.setState({viewSecond:false,viewFirst:true,prev:false})
     }

   }
   
   

   

   handleSearch( event )
   {
     let args = [];
     if (this.state.selectedDeptId) {
       args.push('departmentId='+this.state.selectedDeptId);
     }
     if (this.state.selectedFinYear) {
       args.push('finYear='+this.state.selectedFinYear);
       args['finYear'] = this.state.selectedFinYear;
     }
     if (this.state.selectedKpiCode) {
       args.push('kpiCodes='+this.state.selectedKpiCode);
     }

     let self = this;

     /*var res = JSON.parse('{"responseInfo":{"apiId":"org.egov.pt","ver":"1.0","ts":1512034880099,"resMsgId":"uief87324","msgId":"654654","status":"200"},"kpiValues":[{"tenantId":"default","kpi":{"departmentId":2,"department":null,"id":"2","name":"MYKPITWO 2","code":"MKT","remoteSystemId":null,"periodicity":"MONTHLY","targetType":"OBJECTIVE","instructions":"SECOND INS Updated","financialYear":"2017-18","documentsReq":null,"auditDetails":{"createdBy":73,"lastModifiedBy":73,"createdTime":1511867483098,"lastModifiedTime":1511867571861},"kpiTarget":{"id":"10","kpiCode":"MKT","targetValue":"3","targetDescription":"In Progress","tenantId":null,"createdBy":null,"lastModifiedBy":null,"createdDate":null,"lastModifiedDate":null}},"kpiValue":{"id":"66","kpi":null,"kpiCode":"MKT","tenantId":"default","valueList":[{"id":null,"valueid":"66","period":"4","value":"1"},{"id":null,"valueid":"66","period":"5","value":"3"},{"id":null,"valueid":"66","period":"6","value":""},{"id":null,"valueid":"66","period":"7","value":"3"},{"id":null,"valueid":"66","period":"8","value":""},{"id":null,"valueid":"66","period":"9","value":""},{"id":null,"valueid":"66","period":"10","value":""},{"id":null,"valueid":"66","period":"11","value":""},{"id":null,"valueid":"66","period":"12","value":""},{"id":null,"valueid":"66","period":"1","value":""},{"id":null,"valueid":"66","period":"2","value":""},{"id":null,"valueid":"66","period":"3","value":"2"}],"documents":null,"auditDetails":{"createdBy":73,"lastModifiedBy":0,"createdTime":1511867905957,"lastModifiedTime":0}}},{"tenantId":"default","kpi":{"departmentId":2,"department":null,"id":"1","name":"MYKPIONE 1","code":"MKO","remoteSystemId":null,"periodicity":"MONTHLY","targetType":"VALUE","instructions":"ONE Ins Updated","financialYear":"2017-18","documentsReq":null,"auditDetails":{"createdBy":73,"lastModifiedBy":73,"createdTime":1511867483098,"lastModifiedTime":1512030906958},"kpiTarget":{"id":"9","kpiCode":"MKO","targetValue":"1000","targetDescription":"1000","tenantId":null,"createdBy":null,"lastModifiedBy":null,"createdDate":null,"lastModifiedDate":null}},"kpiValue":{"id":"57","kpi":null,"kpiCode":"MKO","tenantId":"default","valueList":[{"id":null,"valueid":"57","period":"4","value":"311"},{"id":null,"valueid":"57","period":"5","value":"200"},{"id":null,"valueid":"57","period":"6","value":"100"},{"id":null,"valueid":"57","period":"7","value":"120"},{"id":null,"valueid":"57","period":"8","value":"130"},{"id":null,"valueid":"57","period":"9","value":"140"},{"id":null,"valueid":"57","period":"10","value":"110"},{"id":null,"valueid":"57","period":"11","value":"120"},{"id":null,"valueid":"57","period":"12","value":"105"},{"id":null,"valueid":"57","period":"1","value":""},{"id":null,"valueid":"57","period":"2","value":""},{"id":null,"valueid":"57","period":"3","value":"10"}],"documents":null,"auditDetails":{"createdBy":73,"lastModifiedBy":0,"createdTime":1511867905957,"lastModifiedTime":0}}},{"tenantId":"default","kpi":{"departmentId":2,"department":null,"id":"3","name":"MYKPITHREE 3","code":"MKTH","remoteSystemId":null,"periodicity":"MONTHLY","targetType":"TEXT","instructions":"THIRD INS Updated","financialYear":"2017-18","documentsReq":null,"auditDetails":{"createdBy":73,"lastModifiedBy":73,"createdTime":1511867483098,"lastModifiedTime":1511867571861},"kpiTarget":{"id":"11","kpiCode":"MKTH","targetValue":"CONFIRMED","targetDescription":"CONFIRMED","tenantId":null,"createdBy":null,"lastModifiedBy":null,"createdDate":null,"lastModifiedDate":null}},"kpiValue":{"id":"75","kpi":null,"kpiCode":"MKTH","tenantId":"default","valueList":[{"id":null,"valueid":"75","period":"4","value":"100"},{"id":null,"valueid":"75","period":"5","value":"200"},{"id":null,"valueid":"75","period":"6","value":""},{"id":null,"valueid":"75","period":"7","value":""},{"id":null,"valueid":"75","period":"8","value":""},{"id":null,"valueid":"75","period":"9","value":""},{"id":null,"valueid":"75","period":"10","value":""},{"id":null,"valueid":"75","period":"11","value":""},{"id":null,"valueid":"75","period":"12","value":""},{"id":null,"valueid":"75","period":"1","value":""},{"id":null,"valueid":"75","period":"2","value":""},{"id":null,"valueid":"75","period":"3","value":"CONFIRMED"}],"documents":null,"auditDetails":{"createdBy":73,"lastModifiedBy":0,"createdTime":1511867905957,"lastModifiedTime":0}}}]}');
     var response = res.kpiValues;

     let header = self.header();
     //var row   = response;//self.prepareBodyobject(response);

     self.setState({data: response,header:header,showResult: true,KPIResult:response});*/

     var url = 'perfmanagement/v1/kpivalue/_search?'+args.join('&')
     this.props.setLoadingStatus('loading');
     Api.commonApiPost(url, {}, {}, false, true).then(function(res){
        self.props.setLoadingStatus('hide');
        var response = res.kpiValues;

         let header = self.header();
        //var row   = self.prepareBodyobject(response);

         let showResult = false;
         if (res.kpiValues.length) {
          showResult = true;
         }

         self.setState({data: res.kpiValues,header:header,showResult: showResult,KPIResult:response});

       }, function(err){
         self.props.setLoadingStatus('hide');
       });

   }

header()
{
  let header = ['April','May','June','July','August','September','October','November','December','January','February','March'];

  return header.map((headerItem,k) =>  {
    //console.log(headerItem,k);
    var className = '';
         // first section
         var className = this.panelVisiblity(k);
    return (        
          <th className={className}>                      
            <div suppressContentEditableWarning="true"   >{headerItem}</div>
          </th> 
          );
  });

}

panelVisiblity(k)
{
  var className = '';
  if(k >= 0 && k <= 3 && !this.state.viewFirst)
   {
     className = 'hidden';
   }
   if(k >= 4 && k <= 7 && !this.state.viewSecond)
   {
     className = 'hidden';
   }
   if(k >= 8 && k <= 11 && !this.state.viewThird)
   {
     className = 'hidden';
   }
  return className;
}



renderFileObject = (item, i) => {
      let self = this;
      if(self.props.readonly) {
      return (
        <a href={window.location.origin + "/filestore/v1/files/id?tenantId=" + localStorage.tenantId + "&fileStoreId=" + self.props.getVal(item.period + "[" + i + "].fileStoreId")} target="_blank">{translate("wc.craete.file.Download")}</a>
      );
    } else {
      return (

        <label class="btn btn-primary" for={"file_"+item.valueid+item.period} style={{"color":"orange"}}>
            <input id={"file_"+item.valueid+item.period} type="file" style={{"display":"none"}}  onChange={e => this.handleFile(e,item.valueid,item.period)}/>
            <span className="glyphicon glyphicon-upload" aria-hidden="true"></span>&nbsp;
            <strong>UPLOAD MORE</strong>
        </label>
      )
    }
  }

prepareUploadPanel(itemValue)
{

  return <div style={{
          ...this.props.style,
          position: 'absolute',
          boxShadow: '0 5px 10px rgba(0, 0, 0, 0.2)',
          border: '1px solid #CCC',
          marginLeft: 78,
          marginTop: 5,
          padding: 10,
          backgroundColor:'white',
          width:170,
          zIndex:1
        }}
      >
        <ul>
          <li>attachment 1</li>
          <li>attachment 2</li>
          <li>attachment 3</li>
        </ul>
        <br/>
        {this.renderFileObject(itemValue)}
      </div>
}

prepareKPIdesc(kpi)
{
  return <Popover id="popover-positioned-bottom">
            <span>{kpi.instructions}</span>
         </Popover>
}

prepareBodyobject(response)
{
  return response.map(item => {
      
    //this.state.uploadPane[item.kpiValue.id] = [];
    return (
          <tr>
            <td>
                <h6><strong>{item.kpi.name}</strong></h6>
                <label>{translate("perfManagement.create.KPIs.groups.type")}</label>:&nbsp;
                <span>{item.kpi.targetType}</span><br/>
                <label>{translate("perfManagement.create.KPIs.groups.kpiTarget")}</label>:&nbsp;
                <span>{item.kpi.kpiTargets[0].targetDescription}</span><br/>

                <OverlayTrigger
                   trigger="click"
                   overlay={this.prepareKPIdesc(item.kpi)} placement="bottom" rootClose>
                    <span style={{"color":"orange"}}>
                        <span className="glyphicon glyphicon-info-sign" aria-hidden="true"></span>&nbsp;
                        <u>{translate("perfManagement.create.KPIs.groups.kpiInfo")}</u>
                    </span>
                </OverlayTrigger>

            </td>
            <td>
            </td> 
            {
              item.kpiValue.valueList.map((itemValue,k) => {
               var className = this.panelVisiblity(k);

                return (
                    <td className={className}>
                      {item.kpi.targetType == 'TEXT' &&

                        <TextField 
                           floatingLabelStyle={{"color": "#696969", "fontSize": "20px", "white-space": "nowrap"}}
                           inputStyle={{"color": "#5F5C57","textAlign":"left"}}
                           style={{"display": 'inline-block',width: 153}}
                           errorStyle={{"float":"left"}}
                           value={itemValue.value}
                            />

                      }

                      {item.kpi.targetType == 'VALUE' &&

                        <TextField 
                           floatingLabelStyle={{"color": "#696969", "fontSize": "20px", "white-space": "nowrap"}}
                           inputStyle={{"color": "#5F5C57","textAlign":"left"}}
                           style={{"display": 'inline-block',width: 153}}
                           errorStyle={{"float":"left"}}
                           value={itemValue.value} />

                      }

                      {item.kpi.targetType == 'OBJECTIVE' &&

                        <SelectField value={itemValue.value}
                           dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                           style={{"display":  'inline-block',width: 153}}
                           errorStyle={{"float":"left"}}
                           fullWidth={true}
                           hintText="Please Select"
                           >
                              <MenuItem value="1"  primaryText="YES" />
                              <MenuItem value="2"  primaryText="NO" />
                              <MenuItem value="3"  primaryText="WIP" />                             
                        </SelectField>

                      }


                      <div>
                          <OverlayTrigger
                             trigger="click"
                             overlay={this.prepareUploadPanel(itemValue)} placement="bottom"  rootClose >
                              <span style={{"color":"orange"}}>
                              <span className="glyphicon glyphicon-upload" aria-hidden="true"></span> &nbsp;&nbsp;
                              <span id={itemValue.valueid+''+itemValue.period} >
                                <strong>{translate('perfManagement.create.KPIs.groups.Uploads')}</strong>
                              </span>
                              </span>
                          </OverlayTrigger>
                          <br/>
                          

                        </div>


                    </td>
                  );

              })
            }
            <td>
            </td>   
          </tr>
          );        
        });
}


  handleChange(kpiId,kpiValueId,event) {
      let KPIsClone = this.state.KPIs.slice();
      console.log(kpiValueId,'kpi value id');
      KPIsClone[kpiId][kpiValueId] = parseInt(event.target.value);

      this.state.KPIResult[kpiId]['kpiValue']['valueList'].map(p =>{
        if (p['period'] == kpiValueId) {
          p['value'] = KPIsClone[kpiId][kpiValueId];
        }
      });
     

      this.setState({KPIs : KPIsClone});


       //console.log(this.state.KPIs);
     }
  

  handleFile(event,valueid,period)
  {
    let files = this.state.documents.slice();

    if(!files[valueid])
    {
      files[valueid] = [];
    }

    files[valueid][period] = event.target.files[0];
    this.setState({documents:files});

  }


  handleSubmit(event)
   {
       let {actionName, moduleName } = this.props;
       let fileList = this.state.documents;
       let counter = Object.keys(fileList).length;
       let url = "perfmanagement/v1/kpivalue/_create";
       let fileStorId = [];
       let self = this;

       let breakOut = 0;
       this.props.setLoadingStatus('loading');
       if (counter) {

         for(let key in fileList) {
        let kpiFiles = fileList[key];

        fileStorId[key] = [];

        for(let index in kpiFiles) {

           fileUpload(kpiFiles[index], moduleName, function(err, res) {
            if(breakOut == 1) return;
            if(err) {
              breakOut = 1;
              self.props.setLoadingStatus('hide');
              self.props.toggleSnackbarAndSetText(true, err, false, true);
            } else {
              counter--;
              fileStorId[key][index] = res.files[0].fileStoreId;
              //console.log(res.files[0].fileStoreId);
              //_.set(formData, key, res.files[0].fileStoreId);
              if(counter == 0 && breakOut == 0)
              {

                this.state.KPIResult.map(kpi => {
              if (fileStorId[kpi.kpiValue.id]) {
                    kpi.kpiValue.valueList.map(kpiValue => {
                      console.log(fileStorId);
                      console.log(fileStorId[kpi.kpiValue.id]);
                      if (fileStorId[kpi.kpiValue.id][kpiValue.period]) {
                        console.log(fileStorId[kpi.kpiValue.id][kpiValue.period]); 
                        kpiValue.documents = fileStorId[kpi.kpiValue.id][kpiValue.period];                   
                      }
                    });
                  }
                  

              });
                self.props.setLoadingStatus('hide');
                let body = {'kpiValues' : this.state.KPIResult};
                self.makeAjaxCall(body,url);
                
              }
                console.log('go for form submission');
                //self.makeAjaxCall(formData, _url);
            }
        })

        }
        counter--;
       }

       }
       else
       {
          self.props.setLoadingStatus('hide');
          let body = {'kpiValues' : this.state.KPIResult};
          self.makeAjaxCall(body,url);
       }
       
   }

   makeAjaxCall= (data,url) =>  {
      let self = this;

      let query = [];

      Api.commonApiPost(url, query, data , false, true).then(function(res){
          self.props.setLoadingStatus('hide');
          if (res) {
            self.props.toggleSnackbarAndSetText(true, translate("perfManagement.update.KPIs.groups.updatekpivalue"), true);
          }

        }, function(err){
            self.props.setLoadingStatus('hide');
            self.props.toggleSnackbarAndSetText(true, err.message);
        });

   }

   clearSearch()
   {
     this.setState({selectedDeptId: '',selectedFinYear:'', selectedKpiCode:'',showResult: false, search:false});
   }

    searchKPIValues(event,key,value,type)
    {
      switch (type) {
        case 'DEPT':
         this.setState({selectedDeptId:value,'search':true});
        break;
        case 'FINYEAR':
         this.setState({selectedFinYear:event.target.innerHTML});
          break;
       case 'KPI':
         this.setState({selectedKpiCode:value});
         break;
        default:
      }

      let args = [];
      if (type == 'DEPT') {
        args.push('departmentId='+value);
      }
      else if (this.state.selectedDeptId) {
        args.push('departmentId='+this.state.selectedDeptId);
      }
      if (type == 'FINYEAR') {
        args.push('finYear='+event.target.innerHTML);
      }
      else if (this.state.selectedFinYear) {
        args.push('finYear='+this.state.selectedFinYear);
        //args['finYear'] = this.state.selectedFinYear;
      }


      let self = this;
      let url = 'perfmanagement/v1/kpimaster/_search?'+args.join('&');

      Api.commonApiPost(url, {}, {}, false, true).then(function(res){

         //console.log(res);
         self.setState({KPIs : res.KPIs})

        }, function(err){

        });

    }

   



   render() {
     let {mockData, moduleName, actionName, formData, fieldErrors, isFormValid} = this.props;
     let {create, handleChange, getVal, addNewCard, removeCard, autoComHandler, initiateWF} = this;

     let tableStyle = {
                align:"center"
           };


           let body = this.prepareBodyobject(this.state.data);
           let header = this.header();


     //console.log(list);
     return (
       <div className="SearchResult">
       

         <Card className="uiCard">
           <CardHeader title={<strong>Search Key Performance Indicator </strong>}/>
           <CardText>
              <Row className="show-grid">
                 <Col xs={4} md={4}>
                   <SelectField value={this.state.selectedDeptId}
                   className="custom-form-control-for-select"
                   floatingLabelStyle={{"color": "#696969", "fontSize": "20px", "white-space": "nowrap"}}
                   floatingLabelFixed={true}
                   dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                   style={{"display":  'inline-block'}}
                   errorStyle={{"float":"left"}}
                   fullWidth={true}
                   hintText="Please Select"
                   labelStyle={{"color": "#5F5C57"}}
                   floatingLabelText={<span>Department <span style={{"color": "#FF0000"}}><i>*</i></span></span>}
                   onChange={(event, key, value) => this.searchKPIValues(event, key, value,'DEPT')}>

                     {this.state.Department && this.state.Department.map((dd, index) => (
                         <MenuItem value={dd.id && dd.id.toString()} key={index} primaryText={dd.name} />
                     ))}
                   </SelectField>
                   </Col>

                   <Col xs={4} md={4}>
                   <SelectField  value={this.state.selectedFinYear}
                   className="custom-form-control-for-select"
                   floatingLabelStyle={{"color": "#696969", "fontSize": "20px", "white-space": "nowrap"}}
                   floatingLabelFixed={true}
                   dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                   style={{"display":  'inline-block'}}
                   errorStyle={{"float":"left"}}
                   fullWidth={true}
                   hintText="Please Select"
                   labelStyle={{"color": "#5F5C57"}}
                   floatingLabelText={<span>Financial Year</span>}
                   onChange={(event, key, value) => this.searchKPIValues(event, key, value,'FINYEAR')}>
                       {this.state.FinantialYear && this.state.FinantialYear.map((dd, index) => (
                           <MenuItem value={dd.finYearRange && dd.finYearRange.toString()} key={index} primaryText={dd.finYearRange} />
                       ))}
                   </SelectField>
                   </Col>

                   <Col xs={4} md={4}>

                   <SelectField  value={this.state.selectedKpiCode}
                   className="custom-form-control-for-select"
                   floatingLabelStyle={{"color": "#696969", "fontSize": "20px", "white-space": "nowrap"}}
                   floatingLabelFixed={true}
                   dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                   style={{"display":  'inline-block'}}
                   errorStyle={{"float":"left"}}
                   fullWidth={true}
                   hintText="Please Select"
                   labelStyle={{"color": "#5F5C57"}}
                   floatingLabelText={<span>KPI Code</span>}
                   onChange={(event, key, value) => this.searchKPIValues(event, key, value,'KPI')}>

                     {this.state.KPIs && this.state.KPIs.map((dd, index) => (
                         <MenuItem value={dd.code && dd.code.toString()} key={index} primaryText={dd.code} />
                     ))}
                   </SelectField>


                   </Col>

                  </Row>
             </CardText>
           </Card>

           <Row>
           <Col xs={6} md={6}>
             <h3 style={{paddingLeft: 15, "marginBottom": "0"}}>{!_.isEmpty(mockData) && moduleName && actionName && mockData[`${moduleName}.${actionName}`] && mockData[`${moduleName}.${actionName}`].title ? translate(mockData[`${moduleName}.${actionName}`].title) : ""}</h3>
           </Col>
           <Col xs={6} md={6}>
             <div style={{"textAlign": "right", "color": "#FF0000", "marginTop": "15px", "marginRight": "15px", "paddingTop": "8px"}}><i>( * ) {translate("framework.required.note")}</i></div>
           </Col>
         </Row>

           <Row className="show-grid">

                <Col xs={6} xsOffset={5}>
                 <UiButton item={{"label": "Search","uiType":"button", "primary": true,"isDisabled": !this.state.search}} ui="google" handler={(e) => this.handleSearch(e)} />&nbsp;&nbsp;
                 <RaisedButton icon={<i style={{color:"black"}} className="material-icons">backspace</i>} label="Reset" primary={false} onClick={this.clearSearch}/>
                 &nbsp;&nbsp;
               </Col>
            </Row>


          {this.state.showResult  &&
          <Card className="uiCard">
              <CardHeader titleStyle={{marginLeft: 638}} title={<strong> {translate("ui.table.title")} </strong>} />

              <CardText>                  
                <div className="cntdatatable">
                  <div className="cntdatatable1">

                       <Table id="searchTable" className="table table-striped table-bordered table dataTable no-footer" cellspacing="0" width="100%" style={tableStyle} responsive>
                           
                        <thead>
                          <th>
                            KPI                          
                          </th>

                          <th>
                            {this.state.prev && <span className="glyphicon glyphicon-menu-left" aria-hidden="true" style={{"cursor":"pointer"}} title="Prev" onClick={(e) => this.prevSection(e)}></span>}
                          </th>
                          {header}
                          <th>
                            {this.state.next && <span className="glyphicon glyphicon-menu-right" aria-hidden="true" style={{"cursor":"pointer"}} title="Next" onClick={(e) => this.nextSection(e)}></span>}
                          </th>
                        </thead>
                        <tbody>
                          {body}
                        </tbody>
                      </Table>
                  </div>
                </div>

                    <br/>
                    <Row>
                      <Col xs={2} xsOffset={10}>
                         <UiButton item={{"label": "Save","uiType":"button", "primary": true}} ui="google" handler={(e) => this.handleSubmit(e)} />&nbsp;&nbsp;
                       </Col>
                     </Row>
          </CardText>
          </Card>}
         

       </div>
     );
   }
 }

const mapStateToProps = state => ({
  metaData:state.framework.metaData,
  mockData: state.framework.mockData,
  moduleName:state.framework.moduleName,
  actionName:state.framework.actionName,
  formData:state.frameworkForm.form,
  fieldErrors: state.frameworkForm.fieldErrors,
  isFormValid: state.frameworkForm.isFormValid,
  requiredFields: state.frameworkForm.requiredFields,
  dropDownData: state.framework.dropDownData
});

const mapDispatchToProps = dispatch => ({
  initForm: (requiredFields) => {
    dispatch({
      type: "SET_REQUIRED_FIELDS",
      requiredFields
    });
  },
  setMetaData: (metaData) => {
    dispatch({type:"SET_META_DATA", metaData})
  },
  setMockData: (mockData) => {
    dispatch({type: "SET_MOCK_DATA", mockData});
  },
  setFormData: (data) => {
    dispatch({type: "SET_FORM_DATA", data});
  },
  setModuleName: (moduleName) => {
    dispatch({type:"SET_MODULE_NAME", moduleName})
  },
  setActionName: (actionName) => {
    dispatch({type:"SET_ACTION_NAME", actionName})
  },
  handleChange: (e, property, isRequired, pattern, requiredErrMsg, patternErrMsg)=>{
    dispatch({type:"HANDLE_CHANGE_FRAMEWORK", property,value: e.target.value, isRequired, pattern, requiredErrMsg, patternErrMsg});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg, isSuccess, isError) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg, isSuccess, isError});
  },
  setDropDownData:(fieldName, dropDownData) => {
    dispatch({type: "SET_DROPDWON_DATA", fieldName, dropDownData})
  },
  setRoute: (route) => dispatch({type: "SET_ROUTE", route}),
  delRequiredFields: (requiredFields) => {
    dispatch({type: "DEL_REQUIRED_FIELDS", requiredFields})
  },
  addRequiredFields: (requiredFields) => {
    dispatch({type: "ADD_REQUIRED_FIELDS", requiredFields})
  },
  removeFieldErrors: (key) => {
    dispatch({type: "REMOVE_FROM_FIELD_ERRORS", key})
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(kpivalues);
