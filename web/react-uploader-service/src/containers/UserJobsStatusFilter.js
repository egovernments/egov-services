import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import RadioButtonUi from "../atomic-components/RadioButtonUi";
import { applyJobsStatusFilter } from "../actions/filter";

class UserJobsStatusFilterContainer extends Component {
  static propTypes = {
    applyJobsStatusFilter: PropTypes.func.isRequired
  };

  options = [
    {
      value: "completed",
      label: "Completed"
    },
    {
      value: "in-progress",
      label: "In Progress"
    },
    {
      value: "new",
      label: "New"
    },
    {
      value: "failed",
      label: "Failed"
    }
  ];

  handleChange = (e, jobStatus) => {
    this.props.applyJobsStatusFilter(jobStatus);
  };

  render() {
    const { options, handleChange } = this;

    return (
      <div>
        <h5>By Status</h5>
        <RadioButtonUi
          defaultValue="completed"
          name="Completion Status"
          options={options}
          handleChange={handleChange}
        />
      </div>
    );
  }
}

const mapDispatchToProps = dispatch => ({
  applyJobsStatusFilter: jobStatus => dispatch(applyJobsStatusFilter(jobStatus))
});

export default connect(null, mapDispatchToProps)(UserJobsStatusFilterContainer);
