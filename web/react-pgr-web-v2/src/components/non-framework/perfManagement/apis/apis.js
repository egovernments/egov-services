import Api from '../../../../api/api';
import {
  flattenArray,
  getMonth
} from './helpers';

var jp = require('jsonpath');

/**
 * defines all the api required to achieve functionality for
 * PMS module
 */
export const fetchDepartmentAPI = cb => {
  Api.commonApiPost('egov-mdms-service/v1/_get?moduleName=common-masters&masterName=Department', [], {}, false, true, false, null, null, true).then(
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
  Api.commonApiPost(`perfmanagement/v1/kpimaster/_search?departmentId=${departmentId}`, [], {}, false, true, false, null, null, true).then(
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
  Api.commonApiPost('egov-mdms-service/v1/_get?masterName=tenants&moduleName=tenant', [], {}, false, true, false, null, null, true).then(
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
  Api.commonApiPost('egov-mdms-service/v1/_get?masterName=FinancialYear&moduleName=egf-master', [], {}, false, true, false, null, null, true).then(
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
  // Api.commonApiPost(`perfmanagement/v1/kpivalue/_comparesearch?finYear=2016-17,2017-18&kpiCodes=FFL&ulbs=default,mh.rohatest,mh.aliba&tenantId=default`, [], {}, false, true).then(function(res) {
  
  // VALUE TYPE TEST
  Api.commonApiPost(`perfmanagement/v1/kpivalue/_comparesearch?finYear=2016-17,2017-18&kpiCodes=PCSOW&ulbs=default,mh.rohatest,mh.aliba&tenantId=default`, [], {}, false, true).then(function(res) {

  // TEXT TYPE TEST
  // Api.commonApiPost(`perfmanagement/v1/kpivalue/_comparesearch?finYear=2017-18&kpiCodes=EWOF&ulbs=default&tenantId=default`, [], {}, false, true).then(function(res) {

  // ACTUAL API CALLING
  // Api.commonApiPost(`perfmanagement/v1/kpivalue/_comparesearch?finYear=${finYears}&kpiCodes=${kpis}&ulbs=${ulbs}`, [], {}, false, true).then(function(res) {
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
    .query(res, '$.MdmsRes["egf-master"].FinancialYear[*]')
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

export const parseCompareSearchResponse = (res, isText = false) => {
  return flattenArray(
    jp.query(res, '$.ulbs[*]').map((ulbs, index) => {
      return jp.query(ulbs, '$.finYears[*]').map((finYears, index) => {
          return jp.query(finYears, '$.kpiValues[*]').map((kpis, index) => {
              return jp.query(kpis, '$.valueList[*]').map((values, index) => {
                if (isText) {
                  return {
                    ulbName: jp.query(ulbs, '$.ulbName').join(''),
                    finYear:jp.query(finYears, '$.finYear').join(''),
                    kpiName:jp.query(kpis, '$.kpi.name').join(''),
                    target: jp.query(kpis, '$.kpi.kpiTargets[*].targetValue').join(''),
                    value: jp.query(kpis, '$.consolidatedValue').join(''),
                    period: jp.query(values, '$.period').join('') || 0,
                    name: getMonth(jp.query(values, '$.period').join('') || '1'),
                    monthlyValue: jp.query(values, '$.value').join(''),
                  }
                } else {
                  return {
                    ulbName: jp.query(ulbs, '$.ulbName').join(''),
                    finYear:jp.query(finYears, '$.finYear').join(''),
                    kpiName:jp.query(kpis, '$.kpi.name').join(''),
                    target: parseInt(jp.query(kpis, '$.kpi.kpiTargets[*].targetValue').join('')) || 0,
                    value: parseInt(jp.query(kpis, '$.consolidatedValue').join('')) || 0,
                    period: jp.query(values, '$.period').join('') || 0,
                    name: getMonth(jp.query(values, '$.period').join('') || '1'),
                    monthlyValue: parseInt(jp.query(values, '$.value').join('')) || 0,
                  }
                }
              })
          })
      })
    })
  );
}