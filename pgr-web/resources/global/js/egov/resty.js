var resty=new $.Resty({
url:'http://172.16.2.202'
});

resty.add({name:'getComplaintTypes', 'url':':8081/pgr/complaintTypeCategories'});
resty.getComplaintTypes({
type:"POST",
options:{ 
crossDomain: true
},
params:{
 "teantId": "test"
},
done:function(data, isSuccess, responseCode){
console.log("isSuccess", isSuccess);
//openInNewTab("http://google.com");
}
});
"use strict";

var Resource, error, defaultConfigs, modules, Module;

function Error(msg)
{
this.msg=msg;
}

error = function(msg) {
 throw new Error("ERROR: jquery.resty: " + msg);
};

defaultConfigs = {
 url: '',
 logger:false
};

/* ACTION URL'S SEPARATED AS MODULE */
Module=function module(config)
{
if(!config.name)
 return error("Please provide name!");
if(!config.url)
 return error("Please provide url!");
this.config=config;
}

Module.prototype.url=function()
{
return this.config.url;
};

Resource=function(config){
if(!config.url)
 return error("Please provide server url!");
/*var res =url.match(/(http(s)?:\/\/.)?(www\.)?[-a-zA-Z0-9@:%._\+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_\+.~#?&//=]*)/g)
console.log('res', res);
if(res==null)
 return error("Invalid url -> "+url);*/
this.config=config;
this.modules=[];
};

Resource.prototype.url=function(){
return this.config.url;
};

Resource.prototype.add=function(config){
this.modules.push(new Module(config));

var resource=this;

this.modules.forEach(function (module) {
resource[module.config.name] = function (request) {
          resource.enqueue(module, request.type, request.params, request.options, request.done);
       };
});
};

Resource.prototype.enqueue=function(module, type, params, options, callback){
var ajaxOptions={
type:type,
data:params,
url:decodeURIComponent(this.config.url)+decodeURIComponent(module.config.url)//TO-DO : add end with slash condition
};

for(var key in options) {
ajaxOptions[key]=options[key];
}

   doRequest(ajaxOptions, callback);
};

function doRequest(options, callback){
options.complete= function(e, xhr, settings){
  if ( e.status >= 200 && e.status < 300 || e.status === 304 ) {
  //success
  callback(xhr.responseText, true, xhr.status);
}else{
  //failed
  callback(xhr, false, e.status);
}
};

console.log(options);

$.ajax(options);

};

Resource.config = defaultConfigs;

$.Resty=Resource;