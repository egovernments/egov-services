// var base_url = "https://raw.githubusercontent.com/egovernments/egov-services/egov-mr-functional-test-automation/test/postman/"
//
// let pmutiljs = base_url + "pmutils.js?_=" + (new Date).getTime();
// let template_folder = "";
// let egovutiljs = base_url + "egov_util.js?_=" + (new Date).getTime();

var globalEval = eval

loader_script = `
eval(pm.variables.get('scripts/postman-bdd.min.js'));
eval(pm.variables.get('scripts/pmutils.js'));
eval(pm.variables.get('scripts/egov_util.js'));
`

function init() {
    //give enough time for script to execute

    // return new Promise(resolve => {
    //     pmutil.getTemplate(egovutiljs, "egovutiljs").then((res) => {
    //         console.log("evaluating egov utils")
    //         eval(res)
    //         console.log("evaluated egov utils")
    //         pm.globals.set('egovutiljs', res);
    //     }).then(() => {
    //             return pmutil.getTemplate("http://bigstickcarpet.com/postman-bdd/dist/postman-bdd.min.js", 'postman-bdd').then((res) => {
    //                 pm.globals.set('postman-bdd', res);
    //             })
    //         }
    //     ).then(() => {
    //         return pmutil.loadGitFolder("https://github.com/egovernments/egov-services/tree/egov-mr-functional-test-automation/test/postman/data_templates")
    //     }).then(async ()=> {
    //         console.log("calling login");
    //         await login()
    //     }).then(resolve).catch((err) => console.log("error occurred during init", err));
    // })

    return login()
}

async function beforeRequest() {
    console.log("Running beforeRequest")
    await pmutil.processMetadata(pmutil.getRequestMetadata());
}

if (!pm.variables.has("__postman_started")) {
    pm.variables.set("__postman_started", "true")
    pm.sendRequest("http://localhost:8787/get/all", function (err, res) {
        if (err) {
            console.log("Error occurred. Cannot continue the tests")
            throw err
        }

        let data = res.json();
        // console.log("Got data", data)
        // console.log(data)

        for (const [key, value] of Object.entries(data))
        {
            // console.log("Setting - " + key)
            // console.log(value)
            pm.globals.set(key, value)
        }
        // pm.globals.set('pmutiljs', res.text())
        // pmutil.getTemplate("http://bigstickcarpet.com/postman-bdd/dist/postman-bdd.min.js").then((res) => {
        //     pm.globals.set('postman-bdd', res);
        // })
        console.log("Loading global scripts")
        eval(loader_script)
        console.log("Running init scripts")
        init().then(beforeRequest)
    });

} else {
    eval(loader_script);
    beforeRequest().then(()=>{});
}


/*
initjs_url = "http://localhost:8787/scripts/init.js";
if (!pm.variables.has("__postman_started")) {
        pm.sendRequest(initjs_url, function (err, res) {
        eval(res.text());
    });
} else {
    eval(pm.variables.get('scripts/init.js'))
}
 */