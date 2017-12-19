// mock data for testing purposes only
export const uploadDefinitionsResponse = () => {
  const response = {
    ResponseInfo: {
      apiId: "string",
      ver: "string",
      ts: 0,
      resMsgId: "string",
      msgId: "string",
      status: "SUCCESSFUL"
    },
    ModuleDefs: [
      {
        code: "223e",
        name: "inventory",
        Definitions: [
          {
            code: "string",
            name: "i1",
            path: "string"
          },
          {
            code: "string",
            name: "i2",
            path: "string"
          },
          {
            code: "string",
            name: "i3",
            path: "string"
          },
          {
            code: "string",
            name: "i4",
            path: "string"
          },
          {
            code: "string",
            name: "i5",
            path: "string"
          }
        ]
      },
      {
        code: "223esa",
        name: "swm",
        Definitions: [
          {
            code: "string",
            name: "s1",
            path: "string"
          },
          {
            code: "string",
            name: "s2",
            path: "string"
          },
          {
            code: "string",
            name: "s3",
            path: "string"
          },
          {
            code: "string",
            name: "s4",
            path: "string"
          },
          {
            code: "string",
            name: "s5",
            path: "string"
          }
        ]
      },
      {
        code: "acd223e",
        name: "assets",
        Definitions: [
          {
            code: "string",
            name: "a1",
            path: "string"
          },
          {
            code: "string",
            name: "a2",
            path: "string"
          },
          {
            code: "string",
            name: "a3",
            path: "string"
          },
          {
            code: "string",
            name: "a4",
            path: "string"
          },
          {
            code: "string",
            name: "a5",
            path: "string"
          }
        ]
      },
      {
        code: "22ax3e",
        name: "propertytax",
        Definitions: [
          {
            code: "string",
            name: "p1",
            path: "string"
          },
          {
            code: "string",
            name: "p2",
            path: "string"
          },
          {
            code: "string",
            name: "p3",
            path: "string"
          },
          {
            code: "string",
            name: "p4",
            path: "string"
          },
          {
            code: "string",
            name: "p5",
            path: "string"
          }
        ]
      }
    ]
  };

  return response;
};

export const createJobResponse = () => {
  const response = {
    ResponseInfo: {
      apiId: "string",
      ver: "string",
      ts: 0,
      resMsgId: "string",
      msgId: "string",
      status: "SUCCESSFUL"
    },
    UploadJobs: [
      {
        tenantId: "default",
        moduleName: "inventory",
        defName: "i2",
        code: "23r434",
        requestFilePath: "sdcksjnc",
        responseFilePath: "string",
        status: "new",
        totalRows: 0,
        successfulRows: 0,
        failedRows: 0,
        requesterName: "string"
      }
    ]
  };

  return response;
};

export const searchUserJobsReponse = () => {
  const response = {
    ResponseInfo: {
      apiId: "string",
      ver: "string",
      ts: 0,
      resMsgId: "string",
      msgId: "string",
      status: "SUCCESSFUL"
    },
    UploadJobs: [
      {
        tenantId: "default",
        moduleName: "inventory",
        defName: "i1",
        code: "1",
        requestFilePath: "sjdncjknsknds",
        responseFilePath: "sdkjncskdcjnjk",
        status: "new",
        totalRows: 0,
        successfulRows: 0,
        failedRows: 0,
        requesterName: "string"
      },
      {
        tenantId: "default",
        moduleName: "inventory",
        defName: "i1",
        code: "2",
        requestFilePath: "sjdncjknsknds",
        responseFilePath: "sdkjncskdcjnjk",
        status: "failed",
        totalRows: 0,
        successfulRows: 0,
        failedRows: 0,
        requesterName: "string"
      },
      {
        tenantId: "default",
        moduleName: "swm",
        defName: "s5",
        code: "3",
        requestFilePath: "sjdncjknsknds",
        responseFilePath: "sdkjncskdcjnjk",
        status: "completed",
        totalRows: 0,
        successfulRows: 0,
        failedRows: 0,
        requesterName: "string"
      },
      {
        tenantId: "default",
        moduleName: "property-tax",
        defName: "p3",
        code: "4",
        requestFilePath: "sjdncjknsknds",
        responseFilePath: "sdkjncskdcjnjk",
        status: "in-progress",
        totalRows: 0,
        successfulRows: 0,
        failedRows: 0,
        requesterName: "string"
      },
      {
        tenantId: "default",
        moduleName: "assets",
        defName: "a4",
        code: "5",
        requestFilePath: "sjdncjknsknds",
        responseFilePath: "sdkjncskdcjnjk",
        status: "completed",
        totalRows: 0,
        successfulRows: 0,
        failedRows: 0,
        requesterName: "string"
      }
    ]
  };

  return response;
};
