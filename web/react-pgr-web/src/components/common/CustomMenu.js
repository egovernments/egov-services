import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import Menu from 'material-ui/Menu';
import MenuItem from 'material-ui/MenuItem';
import DropDownMenu from 'material-ui/DropDownMenu';

import TextField from 'material-ui/TextField';

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
  margin: '16px 32px 16px 0',
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
    }
  }



  componentDidUpdate()
  {
    let {menuItems}=this.props;

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




  handleChange=(e)=>
  {
      this.setState({
        searchText:e.target.value
      })
  }

  menuChange=(nextLevel,parentLevel)=>
  {
    this.setState({
      level:nextLevel,
      parentLevel
    })
  }

  changeLevel=(level)=>{
    this.setState({
      level,
      parentLevel:level-1
    })
  }




  render() {
    // console.log(this.state.searchText);
    let {menuItems}=this.props;
    let {searchText,filterMenu,level,parentLevel}=this.state;
    let {menuChange,changeLevel}=this;
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
    console.log(menuItems);
    console.log(parentLevel);
    const showMenu=()=>{

      if(searchText.length==0)
      {
        return menuItems.map((item,index)=>{
            if (item.level==level) {
              if (item.url) {
                return(
                  <Link   key={index} to={item.url} >
                    <MenuItem

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
                           onTouchTap={()=>{menuChange(item.nextLevel,item.level)}}
                        />
                    )
              }

            }
        })
        // return(
        //   <div>
        //     <MenuItem
        //          leftIcon={<i className="material-icons">view_module</i>}
        //          primaryText={menuItems.length>0?menuItems[0].title:""}
        //          rightIcon={<ArrowDropRight />}
        //           />
        //       {/*
        //         <MenuItem
        //              leftIcon={<i className="material-icons">view_module</i>}
        //              primaryText={menuItems.length>0?menuItems[0].title:""}
        //              rightIcon={<ArrowDropRight />}
        //              menuItems={constructMenu(menuItems.length>0?menuItems[0].items:[])}
        //            />
        //         */}
        //
        //          {/*<MenuItem
        //             leftIcon={<i className="material-icons">favorite</i>}
        //             primaryText="Favourites"
        //             rightIcon={<ArrowDropRight />}
        //             menuItems={constructMenu(menuItems.length>0?menuItems[0].items:[])}
        //           />*/}
        //     </div>
        // )
      }
      else {
        return (
            <span>Searching</span>
        )
      }
    }
    // console.log(constructMenu(menuItems.length>0?menuItems[0].items:[]));
    return (
      <div className="custom-menu" style={style}>
          {/*
            <TextField
               hintText="Quick Find"
               onChange={this.handleChange}
               value={searchText}
             />
            */}






        <Menu desktop={true} width={320}>
        {level>1 &&    <FloatingActionButton onTouchTap={()=>{changeLevel(0)}}  mini={true} style={style}>
              <i className="material-icons">dashboard</i>
          </FloatingActionButton>}
        { level>0 &&   <FloatingActionButton onTouchTap={()=>{changeLevel(parentLevel)}} mini={true} style={style}>
              <i className="material-icons">fast_rewind</i>
          </FloatingActionButton>}

          {/*
            <MenuItem
              primaryText="Application"
              leftIcon={<i className="material-icons">view_module</i>}
              rightIcon={<ArrowDropRight />}
              />
            */}
          {showMenu()}
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

export default CustomMenu;
