
/* this is only to flush pgr related data */ 

truncate table submission ;

truncate table submission_attribute ;  

truncate table cs_wf_states ; 

truncate table cs_wf_state_history ;


/*POST service-request/_delete_by_query
{
  "query": {
    "match_all": {}
  }
}*/

    


