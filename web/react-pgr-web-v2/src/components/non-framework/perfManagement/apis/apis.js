import Api from '../../../../api/api';
var jp = require('jsonpath');

/**
 * defines all the api required to achieve functionality for
 * PMS module
 */
export const fetchDepartmentAPI = cb => {
  Api.commonApiPost('egov-mdms-service/v1/_get?moduleName=common-masters&masterName=Department', [], {}, false, true).then(
    function(res) {
      if (
        res &&
        res.MdmsRes &&
        res.MdmsRes['common-masters'] &&
        res.MdmsRes['common-masters'].Department &&
        res.MdmsRes['common-masters'].Department[0]
      ) {
        cb(null, res);
      } else {
        cb(null, null);
      }
    },
    function(err) {
      cb(err, null);
    }
  );
};

export const fetchDepartmentKPIsAPI = (departmentId, cb) => {
  Api.commonApiPost(`perfmanagement/v1/kpimaster/_search?departmentId=${departmentId}`, [], {}, false, true).then(
    function(res) {
      if (res && res.KPIs) {
        cb(null, res);
      } else {
        cb(null, null);
      }
    },
    function(err) {
      cb(err, null);
    }
  );
};

export const fetchULBsAPI = cb => {
  Api.commonApiPost('egov-mdms-service/v1/_get?masterName=tenants&moduleName=tenant', [], {}, false, false).then(
    function(res) {
      if (res.MdmsRes && res.MdmsRes['tenant'] && res.MdmsRes['tenant'].tenants) {
        cb(null, res);
      } else {
        cb(null, null);
      }
    },
    function(err) {
      cb(err, null);
    }
  );
};

export const fetchFinancialYearsAPI = cb => {
  Api.commonApiPost('egov-mdms-service/v1/_get?masterName=financialYears&moduleName=egf-master', [], {}, false, false).then(
    function(res) {
      if (res.MdmsRes && res.MdmsRes['egf-master']) {
        cb(null, res);
      } else {
        cb(null, null);
      }
    },
    function(err) {
      cb(err, null);
    }
  );
};

export const fetchCompareSearchAPI = (finYears, kpis, ulbs, cb) => {
  // OBJECTIVE TYPE TEST
  // Api.commonApiPost(`perfmanagement/v1/kpivalue/_comparesearch?tenantId=default&kpiCodes=OTK1,OTK2,OTK3,OTK4&finYear=2017-18&ulbs=default`, [], {}, false, true).then(function(res) {
  // Api.commonApiPost(`perfmanagement/v1/kpivalue/_comparesearch?tenantId=default&kpiCodes=OTK1&finYear=2017-18&ulbs=default,mh.roha,mh.rohatest,mh.aliba`, [], {}, false, true).then(function(res) {

  // VALUE TYPE TEST
  // Api.commonApiPost(`perfmanagement/v1/kpivalue/_comparesearch?finYear=2017-18,2018-19&ulbs=default&kpiCodes=PFP`, [], {}, false, true).then(function(res) {

  // TEXT TYPE TEST
  // Api.commonApiPost(`perfmanagement/v1/kpivalue/_comparesearch?finYear=2017-18&kpiCodes=TPV2&ulbs=default&tenantId=default`, [], {}, false, true).then(
  
  // ACTUAL API CALLING
  Api.commonApiPost(`perfmanagement/v1/kpivalue/_comparesearch?finYear=${finYears}&kpiCodes=${kpis}&ulbs=${ulbs}`, [], {}, false, true).then(
    function(res) {
      if (res && res.ulbs) {
        cb(null, res);
      } else {
        cb(null, null);
      }
    },
    function(err) {
      cb(err, null);
    }
  );
};

export const parseDepartmentResponse = res => {
  return jp.query(res, '$.MdmsRes["common-masters"].Department[*]').map((department, index) => {
    return {
      code: department.code,
      name: department.name,
      id: department.id,
    };
  });
};

export const parseULBResponse = res => {
  return jp.query(res, '$.MdmsRes["tenant"].tenants[*]').map((tenant, index) => {
    return {
      code: tenant.code,
      name: tenant.name,
    };
  });
};

export const parseFinancialYearResponse = res => {
  // return jp.query(res, '$.financialYears[*]').map((finYear, index) => {
  //     return {
  //         code: finYear.finYearRange,
  //         name: finYear.finYearRange
  //     }
  // });
  return jp
    .query(res, '$.MdmsRes["egf-master"].financialYears[*]')
    .filter(el => {
      if (new Date(el.startingDate) <= Date.now()) {
        return el;
      }
    })
    .map((item, index) => {
      return {
        id: item.id,
        startingDate: item.startingDate,
        endingDate: item.endingDate,
        finYearRange: item.finYearRange,
        name: item.finYearRange,
      };
    });
};

export const parseDepartmentKPIsAsPerKPIType = (res, type) => {
  return jp.query(res, `$.KPIs[?(@.targetType==\"${type}\")]`).map((kpi, index) => {
    return {
      code: kpi.code,
      name: kpi.name,
      type: kpi.targetType,
    };
  });
};
