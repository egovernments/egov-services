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
    codes,
    statuses,
    startDate,
    endDate
  };
  return { RequestInfo, JobSearchRequest };
};

export const uploadDefinitionsRequest = authToken => {
  const RequestInfo = requestInfo(authToken);
  return { RequestInfo };
};
