import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { updateUserJobFilters } from "../jobs/actions";
import TextFieldUi from "../components/TextFieldUi";

const userFilters = (filterKey, label) => {
  class UserFilters extends Component {
    onChange = e => {
      this.props.updateUserJobFilters({
        [filterKey]: e.target.value
          .trim()
          .split(",")
          .map(value => value.trim())
      });
    };

    render() {
      const { onChange } = this;
      const { codes } = this.props;
      return <TextFieldUi value={codes} onChange={onChange} label={label} />;
    }
  }

  const mapStateToProps = state => ({
    [filterKey]: state.userJobs.filter[filterKey]
      ? state.userJobs.filter[filterKey].join(",")
      : ""
  });

  const mapDispatchToProps = dispatch => ({
    updateUserJobFilters: filter => dispatch(updateUserJobFilters(filter))
  });

  return connect(mapStateToProps, mapDispatchToProps)(UserFilters);
};

export default userFilters;
