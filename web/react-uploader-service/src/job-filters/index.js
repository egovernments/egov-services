import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { applyUserJobFilters, resetUserJobFilters } from "../jobs/actions";
import View from "./view";

class Filters extends Component {
  static propTypes = {
    applyUserJobFilters: PropTypes.func.isRequired,
    resetUserJobFilters: PropTypes.func.isRequired
  };

  handleApplyFilter = e => {
    this.props.applyUserJobFilters();
  };

  handleResetFilter = e => {
    this.props.resetUserJobFilters();
  };

  render() {
    const { handleApplyFilter, handleResetFilter } = this;
    return (
      <View
        handleApplyFilter={handleApplyFilter}
        handleResetFilter={handleResetFilter}
      />
    );
  }
}

const mapDispatchToProps = dispatch => ({
  applyUserJobFilters: () => dispatch(applyUserJobFilters()),
  resetUserJobFilters: () => dispatch(resetUserJobFilters())
});

export default connect(null, mapDispatchToProps)(Filters);
