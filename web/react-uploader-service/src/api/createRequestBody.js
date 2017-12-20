export const requestInfo = authToken => {
  const RequestInfo = {
    apiId: "emp",
    ver: "1.0",
    ts: "1234",
    action: "create",
    did: "1",
    key: "abcdkey",
    msgId: "20170310130900",
    requesterId: "rajesh",
    userInfo: { id: "1", userName: "narasappa" },
    authToken
  };
  return RequestInfo;
};

export const jobCreateRequest = (
  authToken,
  tenantId,
  requestFilePath,
  moduleName,
  defName
) => {
  const RequestInfo = requestInfo(authToken);
  const UploadJobs = [
    {
      tenantId,
      moduleName,
      defName,
      requestFilePath,
      status: "new"
    }
  ];

  return { UploadJobs, RequestInfo };
};

// convert the date to epoch here
export const jobSearchRequest = (
  authToken,
  tenantId,
  codes = [],
  statuses = [],
  startDate,
  endDate
) => {
  const RequestInfo = requestInfo(authToken);
  const JobSearchRequest = {
    RequestInfo,
    tenantId,
    codes,
    statuses,
    startDate: startDate ? new Date(startDate).getTime() : startDate,
    endDate: endDate ? new Date(endDate).getTime() : endDate
  };

  return JobSearchRequest;
};

export const uploadDefinitionsRequest = authToken => {
  const RequestInfo = requestInfo(authToken);
  return { RequestInfo };
};
