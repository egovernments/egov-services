import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import Menu from 'material-ui/Menu';
import MenuItem from 'material-ui/MenuItem';

// import MenuItem from 'material-ui/MenuItem';
import Divider from 'material-ui/Divider';
import ArrowDropRight from 'material-ui/svg-icons/navigation-arrow-drop-right';

// import {brown500} from 'material-ui/styles/colors';
// import { stack as Menu } from 'react-burger-menu'

// import '../../styles/jquery.multilevelpushmenu.min.css';
// import './jquery.multilevelpushmenu.min.js';
//
// import './custom-menu.js';


class CustomMenu extends Component {
  constructor(props) {
    super(props);
  }


  render() {
    let {menuItems}=this.props;
    console.log(menuItems);
    const constructMenu=()=>{
      return(
        <MenuItem>
          <Link to="/PropertyTaxSearch">
              Property Tax
          </Link>
        </MenuItem>
      )
    }
    return (
      <div className="custom-menu">
        <Menu>
          {constructMenu()}
        </Menu>
      </div>
    );
  }
}

export default CustomMenu;
