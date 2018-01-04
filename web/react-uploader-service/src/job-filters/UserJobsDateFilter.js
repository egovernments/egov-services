import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { updateUserJobFilters } from "../jobs/actions";
import DatePickerUi from "../components/DatePickerUi";

class UserJobsDateFilterContainer extends Component {
  static propTypes = {
    updateUserJobFilters: PropTypes.func.isRequired
  };

  maxDate = new Date();
  render() {
    const { updateUserJobFilters, startDate, endDate } = this.props;
    const { maxDate } = this;

    return (
      <div>
        <div className="col-lg-4 col-md-4">
          <DatePickerUi
            value={startDate}
            onChange={(event, date) => {
              updateUserJobFilters({ startDate: date });
            }}
            label="From Date"
            maxDate={maxDate}
          />
        </div>
        <DatePickerUi
          value={endDate}
          onChange={(event, date) => {
            updateUserJobFilters({ endDate: date });
          }}
          label="To Date"
        />
      </div>
    );
  }
}

const mapStateToProps = state => ({
  startDate: state.userJobs.filter.startDate,
  endDate: state.userJobs.filter.endDate
});

const mapDispatchToProps = dispatch => ({
  updateUserJobFilters: filter => dispatch(updateUserJobFilters(filter))
});

export default connect(mapStateToProps, mapDispatchToProps)(
  UserJobsDateFilterContainer
);
