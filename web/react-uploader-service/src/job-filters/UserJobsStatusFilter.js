import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { updateUserJobFilters } from "../jobs/actions";
import CheckboxUi from "../components/CheckboxUi";

class UserJobsStatusFilterContainer extends Component {
  static propTypes = {
    updateUserJobFilters: PropTypes.func.isRequired
  };
  state = {
    checkedValues: []
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

  onChecked = (e, value) => {
    const { checkedValues } = this.state;
    let jobStatuses;
    if (checkedValues.indexOf(value) !== -1) {
      jobStatuses = checkedValues.filter(
        checkedValue => checkedValue !== value
      );
    } else {
      jobStatuses = checkedValues.concat(value);
    }
    this.props.updateUserJobFilters({ statuses: jobStatuses });
    this.setState({ checkedValues: jobStatuses });
  };

  // the view can be passed as a parameter
  render() {
    const { options, onChecked } = this;
    const { checkedValues } = this.state;

    return (
      <div>
        <CheckboxUi
          style={{
            display: "inline-block",
            width: "20%"
          }}
          checkedValues={checkedValues}
          options={options}
          onCheck={onChecked}
        />
      </div>
    );
  }
}

// these things are common across components
const mapStateToProps = state => ({
  statuses: state.userJobs.filter.statuses || []
});

const mapDispatchToProps = dispatch => ({
  updateUserJobFilters: filter => dispatch(updateUserJobFilters(filter))
});

export default connect(mapStateToProps, mapDispatchToProps)(
  UserJobsStatusFilterContainer
);
