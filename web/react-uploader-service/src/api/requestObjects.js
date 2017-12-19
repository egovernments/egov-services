export const getRequestInfo = authToken => {
  const RequestInfo = {
    apiId: "org.egov.pt",
    ver: "1.0",
    ts: "27-06-2017 10:30:12",
    action: "asd",
    did: "4354648646",
    key: "xyz",
    msgId: "654654",
    requesterId: "61",
    authToken
  };
  return RequestInfo;
};

export const getJobCreateRequest = (
  authToken,
  tenantId,
  moduleName,
  defName,
  requestFilePath
) => {
  const RequestInfo = getRequestInfo(authToken);
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

export const getJobSearchRequest = (
  tenantId,
  authToken,
  codes = [],
  statuses = [],
  startDate,
  endDate
) => {
  const RequestInfo = getRequestInfo(authToken);
  const JobSearchRequest = {
    codes,
    statuses,
    startDate,
    endDate
  };
  return { RequestInfo, JobSearchRequest };
};
