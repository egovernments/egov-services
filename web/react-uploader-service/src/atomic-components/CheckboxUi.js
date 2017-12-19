import React, { Component } from "react";
import PropTypes from "prop-types"; // ES6
import { Checkbox } from "material-ui";

export default class MultiCheckbox extends Component {
  static propTypes = {
    isDisabled: PropTypes.bool,
    isRequired: PropTypes.bool,
    debug: PropTypes.bool,
    data: PropTypes.arrayOf(
      PropTypes.shape({
        label: PropTypes.string,
        code: PropTypes.string
      })
    ).isRequired,
    defaultValue: PropTypes.arrayOf(PropTypes.string),
    displayLabel: PropTypes.string,
    onChange: PropTypes.func
  };
  static defaultProps = {
    debug: false,
    isDisabled: false,
    isRequired: false,
    defaultValue: [],
    displayLabel: ""
  };
  state = {
    allCheckboxsValues: []
  };

  componentWillMount() {
    this.calculateStateFromProps();
  }

  calculateStateFromProps() {
    var allCheckboxsValues = [];
    this.props.data.forEach((v, i) => {
      allCheckboxsValues[i] = this.props.defaultValue.indexOf(v.code) !== -1;
    });
    this.setState({
      allCheckboxsValues: allCheckboxsValues
    });
  }

  handleCheckboxClick = (e, index) => {
    var allCheckboxsValues = JSON.parse(
      JSON.stringify(this.state.allCheckboxsValues)
    );
    allCheckboxsValues[index] = !allCheckboxsValues[index];

    var selected = this.props.data
      .filter((v, i) => {
        return allCheckboxsValues[i];
      })
      .map(function(v) {
        return v.code;
      });

    var errorMessage = "";

    this.setState(
      {
        allCheckboxsValues: allCheckboxsValues,
        errorMessage: errorMessage
      },
      () => {
        if (typeof this.props.onChange === "function") {
          this.props.onChange(selected);
        }
      }
    );
  };
  render() {
    return (
      <Container>
        <div>{this.props.displayLabel}</div>
        {this.props.data.map((item, index) => {
          return (
            <Checkbox
              key={index}
              id={item.code}
              label={item.label}
              checked={this.state.allCheckboxsValues[index]}
              disabled={this.props.isDisabled}
              onCheck={e => {
                this.handleCheckboxClick(e, index);
              }}
            />
          );
        })}
      </Container>
    );
  }
}
