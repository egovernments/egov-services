import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import TextFieldUi from "../atomic-components/TextFieldUi";
import { updateUserJobFilters } from "../actions/userJobs";

class RequesterFileNamesFilterContainer extends Component {
  static propTypes = {
    updateUserJobFilters: PropTypes.func.isRequired
  };

  onChange = e => {
    this.props.updateUserJobFilters({
      requesterFileNames: e.target.value
        .trim()
        .split(",")
        .map(value => value.trim())
    });
  };

  render() {
    const { onChange } = this;
    const { requesterFileNames } = this.props;
    return (
      <TextFieldUi
        value={requesterFileNames}
        onChange={onChange}
        label="File Names"
      />
    );
  }
}

const mapStateToProps = state => ({
  requesterFileNames: state.userJobs.filter.requesterFileNames
    ? state.userJobs.filter.requesterFileNames.join(",")
    : ""
});

const mapDispatchToProps = dispatch => ({
  updateUserJobFilters: filter => dispatch(updateUserJobFilters(filter))
});

export default connect(mapStateToProps, mapDispatchToProps)(
  RequesterFileNamesFilterContainer
);
