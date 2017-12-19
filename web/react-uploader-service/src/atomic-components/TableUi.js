import React from "react";
import {
  Table,
  TableBody,
  TableHeader,
  TableHeaderColumn,
  TableRow,
  TableRowColumn
} from "material-ui/Table";
import FlatButton from "material-ui/FlatButton";

const TableUi = ({ tableHeader, tableBody, styles }) => {
  const renderTableCell = (fieldType, row, fieldName) => {
    const field = row[fieldName];

    switch (fieldType) {
      case "label":
        return field;
      case "hyperlink":
        const label = field.label;
        const href = field.href;
        const buttonProps = { label, href };
        if (href) {
          buttonProps.target = "_blank";
          buttonProps.primary = true;
        } else {
          buttonProps.disabled = true;
        }
        return <FlatButton {...buttonProps} />;
      default:
        break;
    }
  };

  const renderTableHeader = () => {
    return (
      <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
        <TableRow>
          {tableHeader.fields.map((fieldHeader, index) => {
            return (
              <TableHeaderColumn key={index}>{fieldHeader}</TableHeaderColumn>
            );
          })}
        </TableRow>
      </TableHeader>
    );
  };

  const renderTableBody = () => {
    return (
      <TableBody displayRowCheckbox={false}>
        {tableBody.map((row, index) => {
          return (
            <TableRow key={index}>
              {Object.keys(row).map((fieldName, index) => {
                const fieldType = tableHeader.fieldsType[index];
                return (
                  <TableRowColumn key={index}>
                    {renderTableCell(fieldType, row, fieldName)}
                  </TableRowColumn>
                );
              })}
            </TableRow>
          );
        })}
      </TableBody>
    );
  };

  return (
    <Table>
      {renderTableHeader()}
      {renderTableBody()}
    </Table>
  );
};
export default TableUi;
