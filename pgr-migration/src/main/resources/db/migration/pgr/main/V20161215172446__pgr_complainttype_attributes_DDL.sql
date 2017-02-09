alter table  egpgr_complainttype add column attributes jsonb;

update egpgr_complainttype set attributes = 
'[{"variable":true,"code":"Department","datatype":"singlevaluelist","required":false,"datatype_description":"A set of predefined values (specified in this response) where only one value may be selected","order":1,"description":"The Department To which Complaint Type Belongs to",
"values": [{"key":"A","name":"Accounts"},{"key":"B","name":"Buildings"},{"key":"BR","name":"Roads"},{"key":"D","name":"Storm Water Drains"},{"key":"E","name":"Education"},{"key":"G","name":"General"},{"key":"HD","name":"Family Welfare"},{"key":"H","name":"Health"},{"key":"J","name":"Bridges"},{"key":"L","name":"Electrical"},{"key":"M","name":"Mechanical"},{"key":"P","name":"Parks and Playfields"},{"key":"Q","name":"Solid Waste Management"},{"key":"W","name":"Works"},{"key":"Y","name":"Legal Cell"},
{"key":"ENG","name":"Engineering"},{"key":"REV","name":"Revenue"},{"key":"ADM","name":"Administration"}]},
{"variable":true,"code":"isActive","datatype":"boolean","required":false,"datatype_description":"Two values possible true or false","order":2,"description":"Status of Complaint Type"},
{"variable":true,"code":"SLAhours","datatype":"number","required":true,"datatype_description":"A numeric value","order":3,"description":""},
{"variable":true,"code":"hasFinancialImpact","datatype":"boolean","required":false,"datatype_description":"Two values possible true or false","order":4,"description":""}]';
