import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import DatePicker from "material-ui/DatePicker";
import { updateUserJobFilters } from "../actions/userJobs";

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
        <div className="col-lg-4">
          <DatePicker
            value={startDate}
            className="custom-form-control-for-datepicker"
            onChange={(event, date) => {
              updateUserJobFilters({ startDate: date });
            }}
            floatingLabelText="From Date"
            maxDate={maxDate}
          />
        </div>
        <DatePicker
          value={endDate}
          className="custom-form-control-for-datepicker"
          onChange={(event, date) => {
            updateUserJobFilters({ endDate: date });
          }}
          floatingLabelText="To Date"
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
