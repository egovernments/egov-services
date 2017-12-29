import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import TextFieldUi from "../atomic-components/TextFieldUi";
import { updateUserJobFilters } from "../actions/userJobs";

class RequesterNamesFilterContainer extends Component {
  static propTypes = {
    updateUserJobFilters: PropTypes.func.isRequired
  };

  onChange = e => {
    this.props.updateUserJobFilters({
      requesterNames: e.target.value
        .trim()
        .split(",")
        .map(value => value.trim())
    });
  };

  render() {
    const { onChange } = this;
    const { requesterNames } = this.props;
    return (
      <TextFieldUi
        value={requesterNames}
        onChange={onChange}
        label="Requester Names"
      />
    );
  }
}

const mapStateToProps = state => ({
  requesterNames: state.userJobs.filter.requesterNames
    ? state.userJobs.filter.requesterNames.join(",")
    : ""
});

const mapDispatchToProps = dispatch => ({
  updateUserJobFilters: filter => dispatch(updateUserJobFilters(filter))
});

export default connect(mapStateToProps, mapDispatchToProps)(
  RequesterNamesFilterContainer
);
