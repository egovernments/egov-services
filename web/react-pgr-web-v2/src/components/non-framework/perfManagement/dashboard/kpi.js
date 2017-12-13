import React, { Component } from 'react';
import Snackbar from 'material-ui/Snackbar';
import { fetchDepartmentAPI, parseDepartmentResponse } from '../apis/apis';

import LoadingIndicator from '../../../common/LoadingIndicator';
import DashboardCard from './dashboardcard';
import KPIQueryDashboard from './dashboardquery';

export default class Dashboard extends Component {
  constructor(props) {
    super(props);
    this.state = {
      apiLoading: false,
      showDepartmentView: true,
      showQueryView: false,
      toastMsg: '',
      department: null,
    };
    this.departments = [];
  }

  render() {
    return (
      <div>
        {this.renderUIBusy()}
        {this.renderDepartments()}
        {this.renderQueryDashboard()}
        {this.renderToast()}
      </div>
    );
  }

  componentDidMount() {
    this.busyUI(true);
    fetchDepartmentAPI((err, res) => {
      this.busyUI(false);
      if (err || !res) {
        this.setState({
          showDepartmentView: true,
        });
        this.toast('unable to fetch departments');
      } else {
        this.departments = res;
        this.setState({
          showDepartmentView: true,
        });
      }
    });
  }

  processOnClickOnCard = index => {
    let departments = parseDepartmentResponse(this.departments);
    this.setState({
      department: departments[index],
      showDepartmentView: false,
      showQueryView: true,
    });
  };

  processOnClickBackButton = () => {
    this.setState({
      showDepartmentView: true,
      showQueryView: false,
    });
  };

  /**
   * helpers
   */
  toast = msg => {
    this.setState({
      showToast: true,
      toastMsg: msg,
    });
  };

  busyUI = status => {
    this.setState({
      apiLoading: status,
    });
  };

  getDepartmentLogo = department => {
    if (department === 'ADMINISTRATION') {
      return require('../../../../images/pms/Administration.png');
    }
    if (department === 'ACCOUNTS') {
      return require('../../../../images/pms/accounts.png');
    }
    if (department === 'ENGINEERING') {
      return require('../../../../images/pms/Engineering.png');
    }
    if (department === 'TOWN PLANNING') {
      return require('../../../../images/pms/TownPlanning.png');
    }
    if (department === 'REVENUE') {
      return require('../../../../images/pms/Revenue.png');
    }
    if (department === 'PUBLIC HEALTH AND SANITATION') {
      return require('../../../../images/pms/PublicHealthSanitation.png');
    }
    if (department === 'URBAN POVERTY ALLEVIATION') {
      return require('../../../../images/pms/UrbanPovertyAlleviation.png');
    }
    if (department === 'EDUCATION') {
      return require('../../../../images/pms/Education.png');
    }

    return require('../../../../images/pms/kpi-default.png');
  };

  /**
   * render
   * show/hide UI busy
   */
  renderUIBusy = () => {
    return this.state.apiLoading ? <LoadingIndicator status={'loading'} /> : <LoadingIndicator status={'hide'} />;
  };

  /**
   * render
   * present card as per departments
   */
  renderDepartments = () => {
    if (!this.state.showDepartmentView) {
      return <div />;
    }

    let departments = parseDepartmentResponse(this.departments);
    if (departments.length > 0) {
      return departments.map((item, index) => (
        <DashboardCard key={index} index={index} onClick={this.processOnClickOnCard} name={item.name} logo={this.getDepartmentLogo(item.name)} />
      ));
    }
  };

  /**
   * render
   * present query view and hide department view
   */
  renderQueryDashboard = () => {
    if (!this.state.showQueryView) {
      return <div />;
    }
    return <KPIQueryDashboard department={this.state.department} onBackClicked={this.processOnClickBackButton} />;
  };

  /**
   * render
   * display Snackbar to inform user
   */
  renderToast = () => {
    if (!this.state.showToast) {
      return <div />;
    }
    return (
      <Snackbar
        open={this.state.showToast}
        message={this.state.toastMsg}
        autoHideDuration={3000}
        onRequestClose={() => {
          this.setState({
            showToast: false,
          });
        }}
      />
    );
  };
}
