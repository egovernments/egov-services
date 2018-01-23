#PMUtil

PMUtil is a javascript utility library for use with postman. PMutil provides below features

- Limited JSON Path Support: Get, set and remove values
- Load contents of file from a Git folder link
- Generate Random Data
- Load environment file from a URL
- Load postBody from a template (url)


### How to use or load PMUtils?

Edit the top level collection and add the below code to the `Pre-request` tab

```javascript 1.8
let initjs_url = "https://raw.githubusercontent.com/egovernments/egov-services/egov-mr-functional-test-automation/test/postman/init.js"
if (!pm.variables.has("__postman_started")) {
        pm.sendRequest(initjs_url, function (err, res) {
        eval(res.text());
        pm.variables.set('initjs', res.text())
    });
} else {
    eval(pm.variables.get('initjs'))
}
``` 

 