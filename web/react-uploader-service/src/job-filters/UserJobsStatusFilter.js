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
      label: "Completed",
      checked: false
    },
    {
      value: "in-progress",
      label: "In Progress",
      checked: false
    },
    {
      value: "new",
      label: "New",
      checked: false
    },
    {
      value: "failed",
      label: "Failed",
      checked: false
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

  getOptions = options => {
    const { statuses } = this.props;
    return options.map(option => {
      if (statuses.indexOf(option.value) !== -1) {
        return { ...option, checked: true };
      }
      return { ...option, checked: false };
    });
  };

  render() {
    const { options, onChecked } = this;
    const updatedOptions = this.getOptions(options);

    return (
      <div>
        <CheckboxUi
          style={{
            display: "inline-block",
            width: "20%"
          }}
          options={updatedOptions}
          onCheck={onChecked}
        />
      </div>
    );
  }
}

const mapStateToProps = state => ({
  statuses: state.userJobs.filter.statuses || []
});

const mapDispatchToProps = dispatch => ({
  updateUserJobFilters: filter => dispatch(updateUserJobFilters(filter))
});

export default connect(mapStateToProps, mapDispatchToProps)(
  UserJobsStatusFilterContainer
);
