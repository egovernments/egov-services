import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import Menu from 'material-ui/Menu';
import MenuItem from 'material-ui/MenuItem';
import DropDownMenu from 'material-ui/DropDownMenu';
import {connect} from 'react-redux';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
// import MenuItem from 'material-ui/MenuItem';
// import Paper from 'material-ui/Paper';

import Divider from 'material-ui/Divider';
import ArrowDropRight from 'material-ui/svg-icons/navigation-arrow-drop-right';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import jp from "jsonpath";
import _ from "lodash";

// import {brown500} from 'material-ui/styles/colors';
// import { stack as Menu } from 'react-burger-menu'

// import '../../styles/jquery.multilevelpushmenu.min.css';
// import './jquery.multilevelpushmenu.min.js';
//
// import './custom-menu.js';

const style = {
  display: 'inline-block',
  margin: '14px 32px 16px 0',
};


class CustomMenu extends Component {
  constructor(props) {
    super(props);
    this.state={
      searchText:"",
      menu:[],
      filterMenu:[],
      level:0,
      parentLevel:0,
      modules:[],
      items:[],
      path:"",
      menuItems:[]
    }
    this.handleClickOutside = this.handleClickOutside.bind(this);
    this.setWrapperRef = this.setWrapperRef.bind(this);
  }

  setWrapperRef(node) {
    this.wrapperRef = node;
  }

  componentWillReceivePropsMount()
  {
    this.resetMenu();
  }

  componentDidMount() {

    document.addEventListener('mousedown', this.handleClickOutside);
    // console.log(actionList);
    // duplicteMenuItems=jp.query(actionList,'$...path');
    // console.log(duplicteMenuItems);

    this.resetMenu();

  }

  resetMenu=()=>{
    let {actionList}=this.props;
    let menuItems=[];
    for (var i = 0; i < actionList.length; i++) {
      if (actionList[i].path!="") {
        let splitArray=actionList[i].path.split(".");
        if (splitArray.length>1) {
            if (!_.some(menuItems,{ 'name':splitArray[0]} )) {
              menuItems.push({path:"",name:splitArray[0],url:"",queryParams:actionList[i].queryParams,orderNumber:actionList[i].orderNumber});
            }
        } else{
          menuItems.push({path:"",name:actionList.displayName,url:actionList.url,queryParams:actionList[i].queryParams,orderNumber:actionList[i].orderNumber});
        }
      }
    }


    // console.log(menuItems);
    this.setState({
      menuItems,
      path:""
    })
  }

  handleClickOutside(event) {
      if (this.wrapperRef && !this.wrapperRef.contains(event.target) && event.target.innerHTML != "menu") {
          this.props.handleToggle(false);
      }
  }


  changeModulesActions(modules,items)
  {
    this.setState({
      modules,
      items
    })
  }

  handleChange=(e)=>
  {
      this.setState({
        searchText:e.target.value
      })
  }

  menuChange=(nextLevel, parentLevel) => {
    this.setState({
      level:nextLevel,
      parentLevel
    });
  }

  menuChangeTwo=(path) => {
    // let tempPath=path;
    let {actionList}=this.props;
    let menuItems=[];
    for (var i = 0; i < actionList.length; i++) {
      // actionList[i].path.startsWith(path)
      if (actionList[i].path!="" && actionList[i].path.startsWith(path+".")) {
        let splitArray=actionList[i].path.split(path)[1].split(".");
        if (splitArray.length>2) {
            if (!_.some(menuItems,{ 'name':splitArray[1]} )) {
              menuItems.push({path:path+"."+splitArray[1],name:splitArray[1],url:"",queryParams:actionList[i].queryParams,orderNumber:actionList[i].orderNumber});
            }
            // tempPath=path+"."+splitArray[1];
        } else{
          menuItems.push({path:path+"."+splitArray[1],name:actionList[i].displayName,url:actionList[i].url,queryParams:actionList[i].queryParams,orderNumber:actionList[i].orderNumber});
        }
      }
    }


    // console.log(menuItems);
    this.setState({
      menuItems,
      path
    })
  }

  changeLevel=(path)=>{
    let {searchText}=this.state;
    let {setRoute}=this.props;




    if (!path) {
      this.resetMenu();
      // console.log("level 0");
      setRoute("/prd/dashboard");
    }
    else {
      let splitArray=_.split(path, '.');
      var x = splitArray.slice(0, splitArray.length - 1).join(".") ;
      if (x!="" && splitArray.length>1) {
            this.menuChangeTwo(x);
      } else {
            this.resetMenu();
      }

    }
  }




  render() {
    // console.log(this.state.searchText);
    let {handleToggle,actionList,menuConvention}=this.props;
    let {searchText,filterMenu,level,parentLevel,modules,items,changeModulesActions,path,menuItems}=this.state;
    let {menuChange,changeLevel,menuChangeTwo}=this;

    const checkUrl = function(item) {
      if(item.url == '/pgr/createReceivingCenter' && window.location.href.indexOf("/pgr/createReceivingCenter")>-1) {
          window.urlCheck = true;
      }

      if(item.url == '/pgr/receivingModeCreate' && window.location.href.indexOf("/pgr/receivingModeCreate/update")>-1) {
          window.urlCheck = true;
      }

      if(item.url == '/pgr/createServiceType' && window.location.href.indexOf("/pgr/serviceTypeCreate/edit")>-1) {
          window.urlCheck = true;
      }

      if(item.url == '/pgr/createServiceGroup' && window.location.href.indexOf("/pgr/updateServiceGroup")>-1) {
          window.urlCheck = true;
      }
    }



    const showMenuTwo=()=>{

      if(searchText.length==0)
      {

        return menuItems.map((item,index)=>{
            if (!item.url) {
              return (
                        <MenuItem
                             style={{whiteSpace: "initial"}}
                             key={index}
                             leftIcon={<i className="material-icons">view_module</i>}
                             primaryText={item.name}
                             rightIcon={<i className="material-icons">keyboard_arrow_right</i>}
                             onTouchTap={()=>{menuChangeTwo(!item.path?item.name:item.path)}}
                          />
                      )

            }
            else {
              if (menuConvention.hasOwnProperty(item.path)) {
                return(
                      <Link  key={index} to={menuConvention[item.path]} >
                        <MenuItem
                            style={{whiteSpace: "initial"}}
                             onTouchTap={()=>{checkUrl(item); document.title=item.name; handleToggle(false)}}
                             leftIcon={<i className="material-icons">view_module</i>}
                             primaryText={item.name}
                          />
                      </Link>
                    )
              } else {
                let base="";
                if (item.path.search("Employee Management.")>-1 || item.path.search("ess.")>-1) {
                  base=window.location.origin+"/hr-web";
                  // console.log(base);
                }
                else if (item.path.search("Leases And Agreements.")>-1) {
                  base=window.location.origin+"/lams-web";

                }
                else if (item.path.search("Asset Management.")>-1) {
                    base=window.location.origin+"/asset-web";
                }
                return (
                         <a key={index} href={base+item.url+((item.queryParams!="" && item.queryParams)?"?"+item.queryParams:"")} target="_blank">
                           <MenuItem
                                style={{whiteSpace: "initial"}}
                                leftIcon={<i className="material-icons">view_module</i>}
                                primaryText={item.name}
                             />
                          </a>
                        )
              }

            }

        })

      }
      else {

          return actionList.map((item,index)=>{
              if (item.url && item.displayName.toLowerCase().indexOf(searchText.toLowerCase()) > -1) {

            if (menuConvention.hasOwnProperty(item.path)) {
              return(
                    <Link  key={index} to={menuConvention[item.path]} >
                      <MenuItem
                          style={{whiteSpace: "initial"}}
                           onTouchTap={()=>{checkUrl(item); document.title=item.name; handleToggle(false)}}
                           leftIcon={<i className="material-icons">view_module</i>}
                           primaryText={item.name}
                        />
                    </Link>
                  )
            } else {
              let base="";
              if (item.path.search("EIS.")>-1 || item.path.search("ess.")>-1) {
                base=window.location.origin+"/hr-web";
                // console.log(base);
              }
              else if (item.path.search("Leases And Agreements.")>-1) {
                base=window.location.origin+"/lams-web";

              }
              else if (item.path.search("Asset Management.")>-1) {
                  base=window.location.origin+"/asset-web";
              }
              return (
                       <a key={index} href={base+item.url} target="_blank">
                         <MenuItem
                              style={{whiteSpace: "initial"}}
                              leftIcon={<i className="material-icons">view_module</i>}
                              primaryText={item.name}
                           />
                        </a>
                      )
            }

          }

      })


      }
    }

      return (
      <div className="custom-menu" style={style}  ref={this.setWrapperRef}>
          {
            <TextField
               hintText = "&nbsp;&nbsp;Quick Find"
               onChange={this.handleChange}
               value={searchText}
             />
          }






        <Menu desktop={true}>
        {(path|| searchText) && <RaisedButton
                                      primary={true}
                                      icon={<i className="material-icons" style={{"color": "#FFFFFF"}}>home</i>}
                                      style={{...style, "marginLeft": "2px"}}
                                      onTouchTap={()=>{;handleToggle(false); changeLevel("")}}
                                    />}
        { path &&  <RaisedButton
                        primary={true}
                        icon={<i className="material-icons" style={{"color": "#FFFFFF"}}>fast_rewind</i>}
                        style={{...style, "float": "right", "marginRight": "2px"}}
                        onTouchTap={()=>{changeLevel(path)}}
                      />}



            {showMenuTwo()}

          </Menu>


      </div>
    );
  }
}


const mapStateToProps = state => ({menuConvention:state.common.menuConvention});
const mapDispatchToProps = dispatch => ({
  handleToggle: (showMenu) => dispatch({type: 'MENU_TOGGLE', showMenu}),
  setRoute:(route)=>dispatch({type:'SET_ROUTE',route})
})
export default connect(mapStateToProps,mapDispatchToProps)(CustomMenu);

/*showMenu()*/

// const showMenu=()=>{
//
//   if(searchText.length==0)
//   {
//
//     return menuItems.map((item,index)=>{
//         if (item.level==level) {
//           if (item.url) {
//             return(
//               <Link  key={index} to={item.url} >
//                 <MenuItem
//                     style={{whiteSpace: "initial"}}
//                      onTouchTap={()=>{checkUrl(item); document.title=item.name; handleToggle(false)}}
//                      leftIcon={<i className="material-icons">{item.leftIcon}</i>}
//                      primaryText={item.name}
//                   />
//               </Link>
//
//
//             )
//
//           } else {
//             return (
//                   <MenuItem
//
//                        key={index}
//                        leftIcon={<i className="material-icons">{item.leftIcon}</i>}
//                        primaryText={item.name}
//                        rightIcon={<i className="material-icons">{item.rightIcon}</i>}
//                        onTouchTap={()=>{menuChange(item.nextLevel, item.level)}}
//                     />
//                 )
//           }
//
//         }
//     })
//     return(
//       <div>
//         <MenuItem
//
//              leftIcon={<i className="material-icons">view_module</i>}
//              primaryText={menuItems.length>0?menuItems[0].title:""}
//              rightIcon={<ArrowDropRight />}
//               />
//
//         </div>
//     )
//   }
//   else {
//
//       return menuItems.map((item,index)=>{
//             if (item.url && item.name.toLowerCase().indexOf(searchText.toLowerCase()) > -1) {
//               return(
//                 <Link   key={index} to={item.url} >
//                   <MenuItem
//                       style={{whiteSpace: "initial"}}
//                        onTouchTap={()=>{handleToggle(false)}}
//                        leftIcon={<i className="material-icons">{item.leftIcon}</i>}
//                        primaryText={item.name}
//                     />
//                 </Link>
//               )
//             }
//
//       })
//
//
//   }
// }

// console.log(actionList);
// console.log(menuItems.length>0?menuItems[0].title:"");
// const constructMenu=(items)=>{
//   // console.log(items);
//   let menu=[];
//   if (items) {
//     for (var i=0;i<items.length;i++) {
//       if (items[i].hasOwnProperty("items")) {
//         // console.log("if :");
//         // console.log(items[i]);
//         menu.push(<MenuItem
//           primaryText={items[i].name}
//           rightIcon={<ArrowDropRight />}
//           menuItems={constructMenu(items[i].items.length>0?items[i].items[0].items:[])} />)
//       }
//       else {
//         // console.log("else :");
//         // console.log(items[i]);
//         menu.push(<MenuItem primaryText={items[i].name} />)
//       }
//     }
//   }
//
//
//   return menu;
// }
// console.log(menuItems);
// console.log(parentLevel);


// componentDidUpdate()
// {
//
// }

// menuLeaves=(items)=>{
//   // console.log(items);
//   let menu=[];
//   if (items) {
//     for (var i=0;i<items.length;i++) {
//       if (items[i].hasOwnProperty("items")) {
//         // console.log("if :");
//         // console.log(items[i]);
//         this.menuLeaves(items[i].items.length>0?items[i].items[0].items:[]);
//       }
//       else {
//         // console.log("else :");
//         // console.log(items[i]);
//          menu.push(items[i]);
//
//       }
//     }
//   }
//
//   return menu;
// }
