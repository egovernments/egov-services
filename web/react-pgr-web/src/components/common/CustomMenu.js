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
      items:[]
    }
    this.handleClickOutside = this.handleClickOutside.bind(this);
    this.setWrapperRef = this.setWrapperRef.bind(this);
  }

  setWrapperRef(node) {
    this.wrapperRef = node;
  }

  componentDidMount() {
    document.addEventListener('mousedown', this.handleClickOutside);
  }

  handleClickOutside(event) {
      if (this.wrapperRef && !this.wrapperRef.contains(event.target) && event.target.innerHTML != "menu") {
          this.props.handleToggle(false);
      }
  }

  componentDidUpdate()
  {
    let {menuItems,actionList}=this.props;
    // console.log(actionList);
    // this.setState({
    //   modules:actionList
    // })

    // this.menuLeaves(menuItems.length>0?menuItems[0].items:[]);
    // console.log(menuItems);
    // this.setState({
    //   menu:[...this.state.menu,
    //       this.menuLeaves(menuItems.length>0?menuItems[0].items:[])]
    // });
    // console.log(this.state.menu);
    // console.log(leaves);
  }

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

  changeLevel=(level)=>{
    let {searchText}=this.state;
    let {setRoute}=this.props;
    this.setState({
      level,
      parentLevel:level-1,
      searchText:!level?"":searchText
    })

    if (!level) {
      console.log("level 0");
      setRoute("/dashboard");
    }
  }




  render() {
    // console.log(this.state.searchText);
    let {menuItems, handleToggle,actionList}=this.props;
    let {searchText,filterMenu,level,parentLevel,modules,items,changeModulesActions}=this.state;
    let {menuChange,changeLevel}=this;
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

    const showMenu=()=>{

      if(searchText.length==0)
      {

        return menuItems.map((item,index)=>{
            if (item.level==level) {
              if (item.url) {
                return(
                  <Link  key={index} to={item.url} >
                    <MenuItem
                        style={{whiteSpace: "initial"}}
                         onTouchTap={()=>{checkUrl(item); document.title=item.name; handleToggle(false)}}
                         leftIcon={<i className="material-icons">{item.leftIcon}</i>}
                         primaryText={item.name}
                      />
                  </Link>


                )

              } else {
                return (
                      <MenuItem

                           key={index}
                           leftIcon={<i className="material-icons">{item.leftIcon}</i>}
                           primaryText={item.name}
                           rightIcon={<i className="material-icons">{item.rightIcon}</i>}
                           onTouchTap={()=>{menuChange(item.nextLevel, item.level)}}
                        />
                    )
              }

            }
        })
        return(
          <div>
            <MenuItem

                 leftIcon={<i className="material-icons">view_module</i>}
                 primaryText={menuItems.length>0?menuItems[0].title:""}
                 rightIcon={<ArrowDropRight />}
                  />

            </div>
        )
      }
      else {

          return menuItems.map((item,index)=>{
                if (item.url && item.name.toLowerCase().indexOf(searchText.toLowerCase()) > -1) {
                  return(
                    <Link   key={index} to={item.url} >
                      <MenuItem
                          style={{whiteSpace: "initial"}}
                           onTouchTap={()=>{handleToggle(false)}}
                           leftIcon={<i className="material-icons">{item.leftIcon}</i>}
                           primaryText={item.name}
                        />
                    </Link>
                  )
                }

          })


      }
    }

    // const showMenuOne=(modules=[],actionList=[])=>{
    //   menuItems=[...modules,...actionList];
    //   // console.log(menuItems);
    //   if(searchText.length==0)
    //   {
    //     return menuItems.map((item,index)=>{
    //         if (item.hasOwnProperty("actionList")) {
    //             <MenuItem
    //                 key={index}
    //                 leftIcon={<i className="material-icons">view_module</i>}
    //                 primaryText={item.displayName}
    //                 rightIcon={<i className="material-icons">keyboard_arrow_right</i>}
    //                 onTouchTap={()=>{changeModulesActions(item.subModules, item.actionList)}}
    //             />
    //         } else {
    //
    //         }
    //     })
    //   }
    //
    //
    //   // if(searchText.length==0)
    //   // {
    //   //   return menuItems.map((item,index)=>{
    //   //
    //   //
    //   //       // if (item.level==level) {
    //   //       //   if (item.url) {
    //   //       //     return(
    //   //       //       <Link   key={index} to={item.url} >
    //   //       //         <MenuItem
    //   //       //
    //   //       //
    //   //       //              onTouchTap={()=>{checkUrl(item); document.title=item.name; handleToggle(false)}}
    //   //       //
    //   //       //              leftIcon={<i className="material-icons">{item.leftIcon}</i>}
    //   //       //              primaryText={item.name}
    //   //       //           />
    //   //       //       </Link>
    //   //       //
    //   //       //
    //   //       //     )
    //   //       //
    //   //       //   }
    //   //       //
    //   //       //   else {
    //   //       //     return (
    //   //       //           <MenuItem
    //   //       //               key={index}
    //   //       //                leftIcon={<i className="material-icons">{item.leftIcon}</i>}
    //   //       //                primaryText={item.name}
    //   //       //                rightIcon={<i className="material-icons">{item.rightIcon}</i>}
    //   //       //                onTouchTap={()=>{menuChange(item.nextLevel, item.level)}}
    //   //       //             />
    //   //       //         )
    //   //       //   }
    //   //       //
    //   //       // }
    //   //   })
    //   //   return(
    //   //     <div>
    //   //       <MenuItem
    //   //            leftIcon={<i className="material-icons">view_module</i>}
    //   //            primaryText={menuItems.length>0?menuItems[0].title:""}
    //   //            rightIcon={<ArrowDropRight />}
    //   //             />
    //   //
    //   //       </div>
    //   //   )
    //   // }
    //   // else {
    //   //
    //   //     return menuItems.map((item,index)=>{
    //   //           if (item.url && item.name.toLowerCase().indexOf(searchText.toLowerCase()) > -1) {
    //   //             return(
    //   //               <Link   key={index} to={item.url} >
    //   //                 <MenuItem
    //   //                      onTouchTap={()=>{handleToggle(false)}}
    //   //                      leftIcon={<i className="material-icons">{item.leftIcon}</i>}
    //   //                      primaryText={item.name}
    //   //                   />
    //   //               </Link>
    //   //             )
    //   //           }
    //   //
    //   //     })
    //   //
    //   //
    //   // }
    // }
    // console.log(constructMenu(menuItems.length>0?menuItems[0].items:[]));
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
        {(level>0 || searchText) && <RaisedButton
                                      primary={true}
                                      icon={<i className="material-icons" style={{"color": "#FFFFFF"}}>home</i>}
                                      style={{...style, "marginLeft": "2px"}}
                                      onTouchTap={()=>{;handleToggle(false); changeLevel(0)}}
                                    />}
        { level>0 &&  <RaisedButton
                        primary={true}
                        icon={<i className="material-icons" style={{"color": "#FFFFFF"}}>fast_rewind</i>}
                        style={{...style, "float": "right", "marginRight": "2px"}}
                        onTouchTap={()=>{changeLevel(parentLevel)}}
                      />}

          {/*
            <MenuItem
              primaryText="Application"
              leftIcon={<i className="material-icons">view_module</i>}
              rightIcon={<ArrowDropRight />}
              />
            */}

            {showMenu()}

            {/*modules.length>0 && showMenuOne(modules,items)*/}

          {/*constructMenu()*/}
        {/*  <MenuItem>
            <Link to="/">
                Favourites
            </Link>
          </MenuItem>
          <MenuItem>
            <Link to="/propertyTaxSearch">
                Property Search
            </Link>
          </MenuItem>
          <MenuItem>
            <Link to="/propertyCreate">
                Property Create
            </Link>
          </MenuItem>*/}
          </Menu>

      {/*  <DropDownMenu value={1}>
          <MenuItem value={1} primaryText="Never" />
          <MenuItem value={2} primaryText="Every Night" />
          <MenuItem value={3} primaryText="Weeknights" />
          <MenuItem value={4} primaryText="Weekends" />
          <MenuItem value={5} primaryText="Weekly" />


            <DropDownMenu value={1}>
              <MenuItem value={1} primaryText="Never" />
              <MenuItem value={2} primaryText="Every Night" />
              <MenuItem value={3} primaryText="Weeknights" />
              <MenuItem value={4} primaryText="Weekends" />
              <MenuItem value={5} primaryText="Weekly" />
            </DropDownMenu>


        </DropDownMenu>
      */}

      </div>
    );
  }
}


const mapStateToProps = state => ({});
const mapDispatchToProps = dispatch => ({
  handleToggle: (showMenu) => dispatch({type: 'MENU_TOGGLE', showMenu}),
  setRoute:(route)=>dispatch({type:'SET_ROUTE',route})
})
export default connect(mapStateToProps,mapDispatchToProps)(CustomMenu);