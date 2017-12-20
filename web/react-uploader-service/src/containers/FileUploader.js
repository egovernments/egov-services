import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { Link } from "react-router-dom";
import { createJob, setInputFile } from "../actions/createJob";
import RaisedButton from "material-ui/RaisedButton";
import Snackbar from "material-ui/Snackbar";
import UploadDefinitionsContainer from "./UploadDefinitions";
import LoadingIndicator from "../atomic-components/LoadingIndicator";
import { Card, CardActions, CardText } from "material-ui/Card";
import FlatButton from "material-ui/FlatButton";

const styles = {
  fileInput: {
    padding: "20px"
  },
  cardHeaderStyle: {
    textAlign: "center",
    padding: "5px",
    textTransform: "uppercase"
  },
  jobCreationAck: {
    padding: "10px",
    color: "red",
    textAlign: "center"
  }
};

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
    const { isLoading } = this.props;
    const { message, messageBarOpen, errorMessage } = this.state;

    return (
      <div className="container">
        <div className="row">
          <div className="col-lg-4 col-md-4 col-lg-offset-4 col-md-offset-4">
            <Snackbar
              open={messageBarOpen}
              message={errorMessage}
              autoHideDuration={2000}
            />
            {isLoading ? (
              <LoadingIndicator />
            ) : (
              <Card>
                <div style={styles.cardHeaderStyle} className="card-header">
                  <h3>Create Job</h3>
                </div>
                <CardText>
                  <UploadDefinitionsContainer />
                  <div className="file-input" style={styles.fileInput}>
                    <input type="file" onChange={handleOnChange} />
                  </div>
                  <RaisedButton
                    onClick={handleSubmit}
                    label="Create"
                    secondary={true}
                    fullWidth={true}
                  />
                </CardText>
                <CardActions>
                  <Link to="/user-jobs">
                    <FlatButton
                      fullWidth={true}
                      primary={true}
                      label="My Jobs"
                    />
                  </Link>
                </CardActions>
                {message.length ? (
                  <h3 style={styles.jobCreationAck}>{message}</h3>
                ) : (
                  ""
                )}
              </Card>
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
