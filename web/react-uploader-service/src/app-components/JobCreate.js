import React from "react";
import PropTypes from "prop-types";
import UploadDefinitionsContainer from "../containers/UploadDefinitions";
import CardUi from "../atomic-components/CardUi";
import ButtonUi from "../atomic-components/ButtonUi";

const styles = {
  fileInput: {
    padding: "20px"
  },
  jobCreationAck: {
    marginTop: "20px",
    padding: "10px",
    color: "red",
    textAlign: "center"
  }
};

const JobCreate = ({ handleOnChange, handleSubmit, message, history }) => {
  return (
    <div>
      <CardUi cardTitle="Upload Definitions">
        <UploadDefinitionsContainer />
      </CardUi>

      <CardUi cardTitle="File Upload">
        <div className="file-input" style={styles.fileInput}>
          <input type="file" onChange={handleOnChange} />
        </div>
      </CardUi>

      <div style={{ textAlign: "center", width: "100%" }} className="col-lg-12">
        <ButtonUi
          style={{ marginRight: "15px" }}
          icon={{ style: { color: "white" }, name: "add" }}
          onClick={handleSubmit}
          label="Create"
          primary={true}
        />
        <ButtonUi
          onClick={() => {
            history.push("/user-jobs");
            window.location.reload();
          }}
          label="My Jobs"
        />
      </div>

      <span>
        {message.length ? <h3 style={styles.jobCreationAck}>{message}</h3> : ""}
      </span>
    </div>
  );
};

JobCreate.propTypes = {
  handleOnChange: PropTypes.func.isRequired,
  handleSubmit: PropTypes.func.isRequired,
  message: PropTypes.string
};

export default JobCreate;
