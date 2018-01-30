if (typeof PMUtil === "undefined") {
    pending = 0;
    printPending = ()=> {
        console.log("Pending operations are " + pending)
    }
    _setTimeout = setTimeout

    function waitForPendingOperationsToComplete(sampling) {
        printPending()
        console.log("Sampling rate - " + sampling)
        if (pending === 0) {
            console.log("We can stop now")
        } else {
            console.log("Giving extension for " + sampling)
            _setTimeout(waitForPendingOperationsToComplete.bind(null, sampling), sampling)
        }
    }

    Promise = class extends Promise {
        constructor(...args) {
            console.log("Promise constructor called")
            let data = super(...args)
            console.log("Promise constructor finished")
            return data;
        }
        then(...args) {
            console.log("then called")
            pending++
            let data = super.then(...args)
            pending--;
            console.log("then finished")
            return data;
        }
        catch(...args) {
            console.log("catch called")
            let data = super.catch(...args)
            pending--;
            console.log("catch finished")
            return data;
        }
    };

    setTimeout = function (fn, timeout, ...args ) {
        pending++;
        printPending()
        _setTimeout((...args)=> {
            printPending()
            fn(...args)
            printPending()
            pending--;
        }, timeout, args)
    }


    let globalEval = eval;

    PMUtil = function () {
        let self = this;
        let debug = false;
        let logs = [];

        const uuidv4 = require('uuid').v4;

        log = function (...msg) {
            if (debug)
                console.log(...msg);
            logs.push(msg);
        };

        this.getLogs = () => logs;

        this.log = log;

        this.fetch = (url) => new Promise((resolve, reject) => pm.sendRequest(url, (err, res) => err ? reject(err) : resolve(res)));

        this.get = this.fetch;

        let cache = {};
        // cache = pm.globals.has("__pmutil_cache") ? JSON.parse(pm.globals.get('__pmutil_cache')) : {}


        const cache_has_key = key => key in cache || pm.variables.has(key)
        const cache_get = key => {
            if (!key in cache && pm.variables.has(key))
                cache[key] = pm.variables.get(key)

            if (key in cache)
                return cache[key];
            throw Error("Key doesn't exist in cache - " + key)
        }
        const cache_set = (key, value) => {
            cache[key] = value
            pm.globals.set( key, value)
        }

        this.cache = {
            get: cache_get,
            set: cache_set,
            has: cache_has_key
        }

        this.getJSON = (url, from_cache) => {
            from_cache = typeof from_cache === "boolean" ? from_cache: true

            return new Promise(resolve => {
                if (from_cache && cache_has_key(url)) {
                    resolve(JSON.parse(cache_get(url)));
                }

                this.get(url).then(res => {
                    cache_set(url, res.text());
                    resolve(res.json());
                })
            });
        };

        this.loadGitFolder = (url, pattern) => {
            return new Promise((resolve) => {
                pattern = pattern || ".*";
                pattern = typeof pattern === "string" ? new RegExp(pattern) : pattern
                let git_api_url = url
                    .replace("//github.com/", "//api.github.com/repos/")
                    .replace(/^((.*)\/tree\/([^/]+)\/(.*)$)/, "$2/contents/$4?ref=$3");


                self.getJSON(git_api_url, false).then(data => {
                    Promise.all(data.map(file => this.getTemplate(file["download_url"], file.name))).then(resolve);
                });
            });
        };

        this.getTemplate = (url, cache_key) => {
            return new Promise( resolve => {
                cache_key = cache_key || url

                if (cache_has_key(cache_key)) {
                    resolve(cache_get(cache_key));
                }
                else {
                    this.get(url).then((res) => {
                        cache_set(cache_key, res.text());
                        resolve(res.text());
                    })
                }
            })
        }

        this.loadScript = url => {
            return this.getTemplate(url).then((code) => Promise.resolve(globalEval(code)));
        }

        this.loadEnvironment = url => {
            return this.getJSON(url).then(data=>{
                let values = data.values;
                for (const key in values)
                    values[key].enabled ? pm.environment.set(values[key].key, values[key].value) : null
                return Promise.resolve(true)
            })
        };

        const moment = require("moment")

        function moment_applyDelta(moment, delta) {
            delta = delta || "";
            let add = true;
            if (delta[0] === "-")
                add = false;

            let deltaArr = delta.split(/[ -+]/g);

            let pattern = /(\d+)([a-z]+)/ig

            for (let i in deltaArr) {
                if (!deltaArr[i])
                    continue
                pattern.lastIndex = 0;

                if (pattern.test(deltaArr[i])) {
                    pattern.lastIndex = 0;
                    let [,num,type] = pattern.exec(delta)
                    num = parseFloat(num)
                    moment = add ? moment.add(num, type) : moment.subtract(num, type)
                }
            }

            return moment;
        }

        this.rand = {
            // Generate time including milliseconds
            $TS: function(delta) {
                return this.$TIME(delta, "epochms")
            },

            // Generate time without milliseconds
            $TSS: function(delta) {
                return this.$TIME(delta, "epoch")
            },

            $TIME: function(delta, format) {
                format = format || "HH:mm:ss"
                let date = moment_applyDelta(moment(), delta)
                if (format === "epoch"){
                    return date.unix()
                } else if (format == "epochms") {
                    return date.valueOf()
                }
                return date.format(format)
            },

            $DATE: function(delta, format) {
                format = format || "DDMMYY"
                return this.$TIME(delta, format)
            },

            $NOW: function(delta, format) {
                format = format || "DDMMYYHHmmss"
                return this.$TIME(delta, format)
            },

            $randomInt: function (min, max) {
                if (!min) {
                    min = 0;
                    max = 100;
                }
                if (!max) {
                    max = min;
                    min = 1;
                }
                return Math.floor(min + Math.random() * (max + 1 - min));
            },
            $randomStringC: function (min, max, set = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ') {
                return this.$randomString(min, max, set)
            },
            $randomDigits: function (min, max, set = '1234567890') {
                return this.$randomString(min, max, set)
            },
            $randomStringS: function (min, max, set = 'abcdefghijklmnopqrstuvwxyz') {
                return this.$randomString(min, max, set)
            },
            $randomString: function (min, max, set = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz') {
                if (!min) {
                    min = 12;
                    max = 12;
                }
                if (!max) {
                    max = min;
                }
                let length = this.$randomInt(min, max);
                let text = "";

                for (let i = 0; i < length; i++)
                    text += set.charAt(Math.floor(Math.random() * set.length));
                return text;
            },
            $randomAlpha: function (min, max, set = 'abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ') {
                return this.$randomString(min, max, set);
            },
            $randomAlphaS: function (min, max, set = 'abcdefghijklmnopqrstuvwxyz0123456789') {
                return this.$randomString(min, max, set)
            },
            $randomAlphaC: function (min, max, set = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ') {
                return this.$randomString(min, max, set)
            },
            $GUID: (length) => uuidv4().substr(0, length),
            $GUIDWD: (length) => uuidv4().replace(/-/g, '').substr(0, length),

        }

        this.resolveParams = (v, resolvePMVariables) => {
            resolvePMVariables = resolvePMVariables || false;
            let paramPattern;
            if (resolvePMVariables) {
                paramPattern = /({{(\$?[a-z_]+)(?:\((['"a-z0-9, /:\\+-]+)\))?}})/ig;
            } else {
                paramPattern = /({{(\$[a-z_]+)(?:\((['"a-z0-9, /:\\+-]+)\))?}})/ig;
            }
            let m;

            do {
                paramPattern.lastIndex = 0;
                if (m = paramPattern.exec(v)) {
                    let [, replacer, funcName, call,] = m;

                    if (funcName in this.rand) {
                        let callParams;

                        if (!call) {
                            callParams = null;
                        }
                        else {
                            callParams = call.split(",").map((data) => {
                                if (!isNaN(data)) {
                                    data = parseFloat(data);
                                }
                                return data;
                            });
                        }

                        let replace_data = this.rand[funcName].apply(this.rand, callParams);
                        v = v.replace(replacer, replace_data)
                    } else {
                        v = v.replace(replacer, pm.variables.get(funcName))
                    }
                }
            } while (m)
            return v;
        }
        this.deepCloneObject = (obj) => JSON.parse(JSON.stringify(obj))

        this.resolveParamsObject = (obj, resolvePMVariables) => {
            return JSON.parse(JSON.stringify(obj, (k, v) => typeof v === "string" ? this.resolveParams(v, resolvePMVariables) : v));
        }

        this.loadRandomEnvironmentVariables = () => {
            for (const variable in pm.environment.toObject()) {
                if (variable.startsWith("_")) {
                    pm.environment.set(variable.substr(1), this.resolveParams(pm.environment.get(variable)));
                }
            }
        };

        const JSONPath = function () {
            this.performOperations= (data, operations) => {
                for (let op in operations){
                    let json_path = operations[op]["json_path"]
                    let value = operations[op]["value"];

                    if (value == "##delete##")
                        this.remove(data, json_path)
                    else
                        this.set(data, json_path, value)

                }
            }

            this.pathToArray = path => path
                .split(/[.\[\]]/)
                .map(f => {
                    let v = f;
                    if (v !== '' && !isNaN(v) && isFinite(v))
                        v = parseFloat(v);
                    else
                        v = f.replace(/^['"]|['"]$/g, '')
                    return v
                })
                .filter(f => f !== '');

            this.get = (obj, path) => {
                let path2 = this.pathToArray(path)
                let data = path2.reduce((a, b) => {
                    if (a == "$")
                        a = obj;
                    return a[b];
                })

                return data;
            }

            this.set = (obj, path, value) => {
                path = this.pathToArray(path)
                let lastObject = obj;
                if (path.length > 2) {
                    lastObject = path.slice(0, -1).reduce((a, b) => {
                        if (a === "$")
                            a = obj;
                        return a[b];
                    })
                }

                lastObject[path[path.length - 1]] = value
                return value;
            }

            this.remove = (obj, path) => {
                path = this.pathToArray(path)
                let lastObject = path.slice(0, -1).reduce((a, b) => {
                    if (a === "$")
                        a = obj;
                    return a[b];
                })

                delete lastObject[path[path.length - 1]]
            }
        }

        this.jp = new JSONPath();
        this.getPostBody = url => {
            if (url) {
                console.log("getting data from " + url)
                return this.getTemplate(url).then((postBody)=> {
                    console.log("postBody returned data from url - ", postBody)
                    return Promise.resolve(JSON.parse(postBody));
                })

            }
            else if (!pm.variables.has("postBody"))
                return Promise.reject(new Error("postBody template has not been defined and neither a url has been provided"))

            let postBody = pm.variables.get("postBody");
            console.log("Returning postBody from variables", postBody)

            return Promise.resolve(JSON.parse(postBody));
        }

        this.setPostBody = (postBody, pm) => {
            console.log("Setting the postBody using pmutil")
            let resolved_body = this.resolveParamsObject(postBody)
            console.log(resolved_body)
            console.log("Stringfied value is")
            let stringyfied_json = JSON.stringify(resolved_body)
            console.log(stringyfied_json)
            pm.variables.set("postBody", stringyfied_json)
            pm.variables.set("dummyVariables", "tarun")

            console.log("Setting the postBody using pmutil completed")
        }


        this.getRequestMetadata = () => {
            let description = postman["__execution"].request.description.content;
            log("description", description)
            let m = /META((.|\r|\n)+)<<<META/i.exec(description)
            log("Match found", m)
            if (!m)
                return {}
            return JSON.parse(m[1]);
        }

        this.processMetadata = async metaData => {
            // if this is the first request then we should process the random variable generation
            if ('template' in metaData) {
                pm.environment.set('postBody', await this.getTemplate(metaData['template']))
            }

            if ('load_random_environment' in metaData) {
                if (metaData['load_random_environment'])
                    this.loadRandomEnvironmentVariables();
            }

            if ('random' in metaData) {
                for (key in metaData['random']) {
                    pm.environment.set(key, this.resolveParams(metaData['random'][key]))
                }
            }
        }
    }


    pmutil = new PMUtil()

    log = pmutil.log;
} else {
    log("PMUtils already loaded");

}

(function test() {
    let rand = pmutil.rand;

    // console.log(rand.$TIME("+2h"))
    console.log(rand.$TS("-2months"))
    console.log(rand.$TS("-1month"))
    // console.log(rand.$TSS())
    //
    // console.log(rand.$NOW())
    // console.log(rand.$TIME())
    // console.log(rand.$TIME("-2h"))
    // console.log(rand.$DATE())
    // console.log(rand.$DATE("+1day"))
    // console.log(rand.$DATE("-1day","DD-MMM-YY"))

})();
/* loading script to be used at collection level
if (typeof pmutil == "undefined") {
    var url = "https://raw.githubusercontent.com/tarunlalwani/postman-utils/master/pmutils.js";
    if (pm.globals.has("pmutiljs"))
        eval(pm.globals.has("pmutiljs"))
    else {
        console.log("pmutil not found. loading from " + url);
        pm.sendRequest(url, function (err, res) {
            eval(res.text());
            pm.globals.set('pmutiljs', res.text())
        });
    }
}
*/
