import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import DatePicker from "material-ui/DatePicker";
import { applyUserJobFilters } from "../actions/userJobs";

class UserJobsDateFilterContainer extends Component {
  static propTypes = {
    applyUserJobFilters: PropTypes.func.isRequired
  };

  maxDate = new Date();
  render() {
    const { applyUserJobFilters } = this.props;
    const { maxDate } = this;

    return (
      <div>
        <h5>By Date</h5>
        <DatePicker
          onChange={(event, date) => {
            applyUserJobFilters({ startDate: date });
          }}
          floatingLabelText="From Date"
          maxDate={maxDate}
        />
        <DatePicker
          onChange={(event, date) => {
            applyUserJobFilters({ endDate: date });
          }}
          floatingLabelText="To Date"
        />
      </div>
    );
  }
}

const mapDispatchToProps = dispatch => ({
  applyUserJobFilters: filter => dispatch(applyUserJobFilters(filter))
});

export default connect(null, mapDispatchToProps)(UserJobsDateFilterContainer);
