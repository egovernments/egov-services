import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import DropDown from "../components/DropDownUi";
import {
  fetchUploadDefinitions,
  moduleSelected,
  moduleDefinitionSelected
} from "./actions";

class UploadDefinitionsContainer extends Component {
  static propTypes = {
    moduleItems: PropTypes.array,
    moduleDefinitons: PropTypes.array,
    selectedModule: PropTypes.string,
    selectedModuleDefinition: PropTypes.string,
    fetchUploadDefinitions: PropTypes.func.isRequired,
    moduleSelected: PropTypes.func.isRequired,
    moduleDefinitionSelected: PropTypes.func.isRequired
  };

  componentDidMount() {
    this.props.fetchUploadDefinitions();
  }

  handleModuleDropDownChange = (event, index, value) => {
    this.props.moduleSelected(value);
  };

  handleFileTypeDropDownChange = (event, index, value) => {
    this.props.moduleDefinitionSelected(value);
  };

  render() {
    const {
      selectedModule,
      selectedModuleDefinition,
      moduleItems,
      moduleDefinitons
    } = this.props;

    const { handleFileTypeDropDownChange, handleModuleDropDownChange } = this;

    return (
      <div>
        <DropDown
          style={{ marginRight: "15px" }}
          options={moduleItems}
          label="Module Name"
          selected={selectedModule}
          handleChange={handleModuleDropDownChange}
        />
        <DropDown
          options={moduleDefinitons}
          label="Module Definition"
          selected={selectedModuleDefinition}
          handleChange={handleFileTypeDropDownChange}
        />
      </div>
    );
  }
}

const getModuleItems = uploadDefinitions => {
  return Object.keys(uploadDefinitions);
};

const getModuleDefinitions = (uploadDefinitions, selectedModule) => {
  const moduleDefinitons =
    selectedModule === null ? [] : uploadDefinitions[selectedModule];
  return moduleDefinitons;
};

const setSelectedModule = (moduleItems, selectedModule) => {
  return selectedModule === null
    ? moduleItems.length ? moduleItems[0] : null
    : selectedModule;
};

const setSelectedModuleDefinition = (
  moduleDefinitons = [],
  selectedModuleDefinition
) => {
  return selectedModuleDefinition === null
    ? moduleDefinitons.length > 0 ? moduleDefinitons[0] : null
    : selectedModuleDefinition;
};

const mapDispatchToProps = dispatch => ({
  fetchUploadDefinitions: () => dispatch(fetchUploadDefinitions()),
  moduleSelected: moduleName => dispatch(moduleSelected(moduleName)),
  moduleDefinitionSelected: moduleDefiniton =>
    dispatch(moduleDefinitionSelected(moduleDefiniton))
});

const mapStateToProps = (state, ownProps) => {
  const selectedModule = state.uploadDefinitions.selectedModule;
  const selectedModuleDefinition =
    state.uploadDefinitions.selectedModuleDefinition;
  const uploadDefinitions = state.uploadDefinitions.items;

  //module Items
  const moduleItems = getModuleItems(uploadDefinitions);
  // module definitions
  const moduleDefinitons = getModuleDefinitions(
    uploadDefinitions,
    selectedModule
  );

  return {
    moduleItems,
    moduleDefinitons,
    selectedModule: setSelectedModule(moduleItems, selectedModule),
    selectedModuleDefinition: setSelectedModuleDefinition(
      moduleDefinitons,
      selectedModuleDefinition
    )
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(
  UploadDefinitionsContainer
);
