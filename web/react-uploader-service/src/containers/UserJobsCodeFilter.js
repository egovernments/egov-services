import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import TextFieldUi from "../atomic-components/TextFieldUi";
import { updateUserJobFilters } from "../actions/userJobs";

class UserJobsCodeFilterContainer extends Component {
  static propTypes = {
    updateUserJobFilters: PropTypes.func.isRequired
  };

  onChange = e => {
    this.props.updateUserJobFilters({
      codes: e.target.value
        .trim()
        .split(",")
        .map(value => value.trim())
    });
  };

  render() {
    const { onChange } = this;
    const { codes } = this.props;
    return <TextFieldUi value={codes} onChange={onChange} label="Job Codes" />;
  }
}

const mapStateToProps = state => ({
  codes: state.userJobs.filter.codes
    ? state.userJobs.filter.codes.join(",")
    : ""
});

const mapDispatchToProps = dispatch => ({
  updateUserJobFilters: filter => dispatch(updateUserJobFilters(filter))
});

export default connect(mapStateToProps, mapDispatchToProps)(
  UserJobsCodeFilterContainer
);
