import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import Menu from 'material-ui/Menu';
import MenuItem from 'material-ui/MenuItem';
import DropDownMenu from 'material-ui/DropDownMenu';

// import MenuItem from 'material-ui/MenuItem';
// import Paper from 'material-ui/Paper';

import Divider from 'material-ui/Divider';
import ArrowDropRight from 'material-ui/svg-icons/navigation-arrow-drop-right';
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
  }


  render() {
    let {menuItems}=this.props;
    // console.log(menuItems.length>0?menuItems[0].title:"");
    const constructMenu=(items)=>{
      // console.log(items);
      let menu=[];
      if (items) {
        for (var i=0;i<items.length;i++) {
          if (items[i].hasOwnProperty("items")) {
            console.log("if :");
            console.log(items[i]);
            menu.push(<MenuItem
              primaryText={items[i].name}
              rightIcon={<ArrowDropRight />}
              menuItems={constructMenu(items[i].items.length>0?items[i].items[0].items:[])} />)
          }
          else {
            console.log("else :");
            console.log(items[i]);
            menu.push(<MenuItem primaryText={items[i].name} />)
          }
        }
      }


      return menu;
    }

    // console.log(constructMenu(menuItems.length>0?menuItems[0].items:[]));
    return (
      <div className="custom-menu" style={style}>

          <Menu desktop={true} width={320}>
            <MenuItem
               primaryText={menuItems.length>0?menuItems[0].title:""}
               rightIcon={<ArrowDropRight />}
               menuItems={constructMenu(menuItems.length>0?menuItems[0].items:[])}
             />
          {/*constructMenu()*/}
          <MenuItem>
            <Link to="/">
                Favourites
            </Link>
          </MenuItem>
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
