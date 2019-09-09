const Pool = require('pg').Pool
import logger from "./config/logger";
import producer from "./kafka/producer";
import envVariables from "./EnvironmentVariables";

  const pool = new Pool({
  user: envVariables.DB_USER,
  host: envVariables.DB_HOST,
  database: envVariables.DB_NAME,
  password: envVariables.DB_PASSWORD,
  port: envVariables.DB_PORT
})

let createJobKafkaTopic=envVariables.KAFKA_CREATE_JOB_TOPIC;
export const getFileStoreIds = (jobid,tenantId,callback) => {
      var searchquery="";
      var queryparams=[];
      if((tenantId==undefined)||(tenantId.trim()===""))
      {
        searchquery="SELECT * FROM egov_pdf_gen WHERE jobid  = ANY ($1)";
        queryparams=[jobid];
      }
      else
      {
        searchquery="SELECT * FROM egov_pdf_gen WHERE jobid  = ANY ($1) and tenantid = $2";
        queryparams=[jobid,tenantId];
      }
      pool.query(searchquery, queryparams, (error, results) => {
      if (error) {
        logger.error(error.stack || error);
        callback({status:500,message:`error occured while searching records in DB : ${error.message}`});
      }
      else
      {

        if(results && results.rows.length>0)
        {
          var searchresult=[];
          results.rows.map(crow=>{
            searchresult.push({filestoreids:crow.filestoreids,jobid:crow.jobid,tenantid:crow.tenantid,createdtime:crow.createdtime,endtime:crow.endtime,totalcount:crow.totalcount})
          }
          );
          logger.info(results.rows.length+" matching records found in search");
          callback({status:200,message:"Success",searchresult});
        }
        else
        {
          logger.error("no result found in DB search");
          callback({status:404,message:"no matching result found"});
        }
      }
    });
  }

  export const insertStoreIds = (jobid,filestoreids,tenantId,starttime,successCallback,errorCallback,totalcount) => {

      var payloads = [];
      var endtime=new Date().getTime();
      payloads.push({
        topic: createJobKafkaTopic,
        messages: JSON.stringify({jobid,filestoreids,tenantId,createdtime:starttime,endtime,totalcount})
      });
      producer.send(payloads, function(err, data) {

        if(err){
          logger.error(err.stack || err);
          errorCallback({message:`error while publishing to kafka: ${err.message}`});
        }
        else{
          logger.info("jobid: "+jobid+": published to kafka successfully");
          successCallback({message:"Success",jobid:jobid,filestoreIds:filestoreids,tenantid:tenantId,starttime,endtime,totalcount});
        }
      });
  }