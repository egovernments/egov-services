import React from "react";
import PropTypes from "prop-types";
import RaisedButton from "material-ui/RaisedButton";
import UploadDefinitionsContainer from "../containers/UploadDefinitions";
import { Card, CardActions, CardText } from "material-ui/Card";
import FlatButton from "material-ui/FlatButton";
import CardUi from "../atomic-components/CardUi";

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

const JobCreate = ({ handleOnChange, handleSubmit, message, history }) => {
  return (
    <CardUi>
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
          primary={true}
          fullWidth={true}
        />
      </CardText>
      <CardActions>
        <FlatButton
          onClick={() => {
            history.push("/user-jobs");
            window.location.reload();
          }}
          fullWidth={true}
          primary={true}
          label="My Jobs"
        />
      </CardActions>
      {message.length ? <h3 style={styles.jobCreationAck}>{message}</h3> : ""}
    </CardUi>
  );
};

JobCreate.propTypes = {
  handleOnChange: PropTypes.func.isRequired,
  handleSubmit: PropTypes.func.isRequired,
  message: PropTypes.string
};

export default JobCreate;
