var base_url = "https://raw.githubusercontent.com/egovernments/egov-services/egov-mr-functional-test-automation/test/postman/"

let pmutiljs = base_url + "pmutils.js?_=" + (new Date).getTime();
let template_folder = "";
let egovutiljs = base_url + "egov_util.js?_=" + (new Date).getTime();

function init() {
    //give enough time for script to execute

    return new Promise(resolve => {
        pmutil.getTemplate(egovutiljs, "egovutiljs").then((res) => {
            console.log("evaluating egov utils")
            eval(res)
            console.log("evaluated egov utils")
            pm.globals.set('egovutiljs', res);
        }).then(() => {
                return pmutil.getTemplate("http://bigstickcarpet.com/postman-bdd/dist/postman-bdd.min.js", 'postman-bdd').then((res) => {
                    pm.globals.set('postman-bdd', res);
                })
            }
        ).then(() => {
            return pmutil.loadGitFolder("https://github.com/egovernments/egov-services/tree/egov-mr-functional-test-automation/test/postman/data_templates")
        }).then(async ()=> {
            console.log("calling login");
            await login()
        }).then(resolve).catch((err) => console.log("error occurred during init", err));
    })
}

async function beforeRequest() {
    console.log("Running beforeRequest")
    await pmutil.processMetadata(pmutil.getRequestMetadata());
}

if (!pm.variables.has("__postman_started")) {
    pm.variables.set("__postman_started", "true")
    pm.sendRequest(pmutiljs, function (err, res) {
        eval(res.text());
        pm.globals.set('pmutiljs', res.text())
        pmutil.getTemplate("http://bigstickcarpet.com/postman-bdd/dist/postman-bdd.min.js").then((res) => {
            pm.globals.set('postman-bdd', res);
        })

        init().then(beforeRequest)
    });

} else {
    beforeRequest().then(()=>{});
}


loader_script = `
eval(pm.globals.get('postman-bdd'));
eval(pm.globals.get('pmutiljs'));
eval(pm.globals.get('egovutiljs'));
`