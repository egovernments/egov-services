import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { createJob, setInputFile } from "../actions/createJob";
import Snackbar from "material-ui/Snackbar";
import LoadingIndicator from "../atomic-components/LoadingIndicator";
import JobCreate from "../app-components/JobCreate";

class FileUploaderContainer extends Component {
  static propTypes = {
    file: PropTypes.object,
    moduleName: PropTypes.string,
    moduleDefinition: PropTypes.string,
    isLoading: PropTypes.bool,
    jobId: PropTypes.string,
    createJob: PropTypes.func.isRequired,
    setInputFile: PropTypes.func.isRequired
  };

  state = {
    message: "",
    messageBarOpen: false,
    errorMessage: ""
  };

  handleOnChange = e => {
    const file = e.target.files[0];
    this.props.setInputFile(file);
  };

  // set messageBar close
  componentWillReceiveProps(nextProps) {
    const currentJobId = this.props.jobId;
    const nextJobId = nextProps.jobId;
    const nextFileInput = nextProps.file;

    if (currentJobId == null && nextJobId) {
      const message = `Job Code - ${nextJobId}`;
      this.setState({ message });
    }

    if (nextFileInput !== null) {
      this.setState({ messageBarOpen: false });
    }
  }

  handleSubmit = e => {
    const { file, moduleName, moduleDefinition } = this.props;
    if (file === null) {
      const errorMessage = "Please choose a file";
      this.setState({ messageBarOpen: true, errorMessage });
      return;
    }
    this.props.createJob(moduleName, moduleDefinition, file);
  };

  render() {
    const { handleSubmit, handleOnChange } = this;
    const { isLoading, history } = this.props;
    const { message, messageBarOpen, errorMessage } = this.state;

    return (
      <div className="container">
        <div className="row">
          <div className="col-lg-12 col-md-12">
            <Snackbar
              open={messageBarOpen}
              message={errorMessage}
              autoHideDuration={2000}
            />
            {isLoading ? (
              <LoadingIndicator />
            ) : (
              <JobCreate
                history={history}
                handleOnChange={handleOnChange}
                handleSubmit={handleSubmit}
                message={message}
              />
            )}
          </div>
        </div>
      </div>
    );
  }
}

const mapDispatchToProps = dispatch => ({
  createJob: (moduleName, moduleDefinition, file) =>
    dispatch(createJob(moduleName, moduleDefinition, file)),
  setInputFile: file => dispatch(setInputFile(file))
});

const mapStateToProps = (state, ownProps) => ({
  moduleName: state.uploadDefinitions.selectedModule,
  moduleDefinition: state.uploadDefinitions.selectedModuleDefinition,
  file: state.jobCreate.inputFile,
  isLoading: state.jobCreate.isFetching,
  jobId: state.jobCreate.jobId
});

export default connect(mapStateToProps, mapDispatchToProps)(
  FileUploaderContainer
);
