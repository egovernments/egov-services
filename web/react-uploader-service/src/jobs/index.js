import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { fetchUserJobs } from "./actions";
import UserJobFilters from "../job-filters";
import TableUi from "../components/TableUi";
import LoadingIndicator from "../components/LoadingIndicator";

// todo map the header fields with the data keys
class UserJobsContainer extends Component {
  static propTypes = {
    fetchUserJobs: PropTypes.func.isRequired,
    isFetching: PropTypes.bool.isRequired,
    userJobs: PropTypes.arrayOf(
      PropTypes.shape({
        id: PropTypes.string,
        status: PropTypes.oneOf(["completed", "new", "InProgress", "failed"]),
        moduleName: PropTypes.string,
        moduleDefiniton: PropTypes.string,
        download: PropTypes.shape({
          label: PropTypes.string,
          href: PropTypes.string
        })
      })
    )
  };

  // ordering of row in a schema is important to preserve the ordering in the table
  tableSchema = [
    {
      key: "id",
      label: "Job ID",
      fieldType: "label",
      style: {
        width: "180px",
        paddingLeft: "0px",
        paddingRight: "0px",
        textAlign: "center"
      }
    },
    {
      key: "moduleName",
      label: "Module",
      fieldType: "label",
      style: {
        paddingLeft: "0px",
        paddingRight: "0px",
        textAlign: "center"
      }
    },
    {
      key: "startTime",
      label: "Created Date",
      fieldType: "time",
      style: {
        paddingLeft: "0px",
        paddingRight: "0px",
        textAlign: "center"
      }
    },
    {
      key: "successfulRows",
      label: "Successful Rows",
      fieldType: "label",
      style: {
        paddingLeft: "0px",
        paddingRight: "0px",
        textAlign: "center"
      }
    },
    {
      key: "failedRows",
      label: "Failed Rows",
      fieldType: "label",
      style: {
        paddingLeft: "0px",
        paddingRight: "0px",
        textAlign: "center"
      }
    },
    {
      key: "status",
      label: "Status",
      fieldType: "label",
      style: {
        paddingLeft: "0px",
        paddingRight: "0px",
        textAlign: "center"
      }
    },
    {
      key: "download",
      label: "Download",
      fieldType: "hyperlink",
      style: {
        paddingLeft: "0px",
        paddingRight: "0px",
        textAlign: "center"
      }
    }
  ];

  componentDidMount() {
    this.props.fetchUserJobs();
  }

  render() {
    const { tableSchema } = this;
    const { userJobs, isFetching } = this.props;
    return (
      <div className="container">
        <div className="row">
          <div className="col-lg-12">
            <UserJobFilters />
          </div>
          <div className="col-lg-12">
            {isFetching ? (
              <LoadingIndicator />
            ) : (
              <TableUi tableSchema={tableSchema} tableBody={userJobs} />
            )}
          </div>
        </div>
      </div>
    );
  }
}

const filterUserJobs = userJobs => {
  return userJobs.filter(userJob => !userJob.softDelete);
};

const mapDispatchToProps = dispatch => ({
  fetchUserJobs: () => dispatch(fetchUserJobs())
});

const mapStateToProps = (state, ownProps) => ({
  isFetching: state.userJobs.isFetching,
  userJobs: filterUserJobs(state.userJobs.items)
});

export default connect(mapStateToProps, mapDispatchToProps)(UserJobsContainer);
