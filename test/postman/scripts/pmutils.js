if (typeof PMUtil === "undefined") {
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

        this.capture = (fields, pm) => {
            for (let [key, value] of Object.entries(fields)) {
                pm.environment.set(key, this.jp.get(response.body, value))
            }
        }

        let cache = {};
        // cache = pm.globals.has("__pmutil_cache") ? JSON.parse(pm.globals.get('__pmutil_cache')) : {}


        const cache_has_key = key => key in cache || pm.variables.has("___" + key)
        const cache_get = key => {
            if (!key in cache && pm.variables.has("___" + key))
                cache[key] = pm.variables.get("___" + key)

            if (key in cache)
                return cache[key];
            throw Error("Key doesn't exist in cache - " + key)
        }
        const cache_set = (key, value) => {
            cache[key] = value
            pm.globals.set("___" + key, value)
        }

        this.cache = {
            get: cache_get,
            set: cache_set,
            has: cache_has_key
        }

        this.getJSON = async url => {
            if (cache_has_key(url)) {
                return JSON.parse(cache_get(url));
            }

            let res = await this.get(url);
            cache_set(url, res.text());
            return res.json();
        };

        this.loadGitFolder = async (url, pattern) => {
            pattern = pattern || ".*";
            pattern = typeof pattern === "string" ? new RegExp(pattern) : pattern
            let git_api_url = url
                .replace("//github.com/", "//api.github.com/repos/")
                .replace(/^((.*)\/tree\/([^/]+)\/(.*)$)/, "$2/contents/$4?ref=$3");


            setTimeout(function () {

            }, 2000);
            const data = await self.getJSON(git_api_url);

            log("loadJSONGitFolder.data", data);
            for (const file of data) {
                let res = await this.getTemplate(file["download_url"], file.name);
            }

        };

        this.getTemplate = async (url, cache_key) => {
            cache_key = cache_key || url

            if (cache_has_key(cache_key)) {
                return cache_get(cache_key);
            }
            let res = await this.get(url)
            cache_set(cache_key, res.text());
            return res.text();
        }

        this.loadScript = async url => globalEval(await this.getTemplate(url));

        this.loadEnvironment = async url => {
            let data = await this.getJSON(url);
            let values = data.values;
            for (const key in values)
                values[key].enabled ? pm.environment.set(values[key].key, values[key].value) : null
        };
        const moment = require("moment")
        function moment_applyDelta(moment, delta) {
            delta = delta || "";
            let add = true;

            let deltaArr = delta.split(/[ ]/g);

            let pattern = /^([+-]?)(\d+)([a-z]+)$/ig

            for (let i in deltaArr) {
                if (!deltaArr[i])
                    continue
                pattern.lastIndex = 0;

                if (pattern.test(deltaArr[i])) {
                    pattern.lastIndex = 0;
                    let [_, sign,num,type] = pattern.exec(deltaArr[i])
                    num = parseFloat(num)
                    moment = sign === "-" ? moment.subtract(num, type): moment.add(num, type)
                }
            }

            return moment;
        }

        this.rand = {
            // Generate time including milliseconds
            $TS: function(delta) {
                return parseFloat(this.$TIME(delta, "epochms"))
            },

            // Generate date including milliseconds with time set to zero
            $TSD: function(delta) {
                return parseFloat(this.$TIME(delta, "epochms", moment().startOf('day')))
            },

            // Generate time without milliseconds
            $TSS: function(delta) {
                return parseFloat(this.$TIME(delta, "epoch"))
            },

            $TIME: function(delta, format, initial) {
                initial = initial || moment()
                format = format || "HH:mm:ss"
                let date = moment_applyDelta(initial, delta)
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
            let originalValue = v
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
            
            if (/\$TS/.test(originalValue))
                return parseFloat(v);

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
                    console.log("Performing - " + json_path + " - " + value)

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
        this.getPostBody = (url) => {
            let data = null;
            if (pm.globals.has(url))
                data = pm.globals.get(url)
            else if (pm.globals.has("data_templates/" + url))
                data = pm.globals.get("data_templates/" + url)

            return JSON.parse(data)
        }

        this.getPostBodyAsync = async (url) => {
            try {
                if (url) {
                    console.log("getting data from " + url)
                    let postBody = await this.getTemplate("data_templates/" + url)
                    console.log("postBody returned data from url - ", postBody)
                    return JSON.parse(postBody);
                }
                else if (!pm.variables.has("postBody"))
                    throw Error("postBody template has not been defined and neither a url has been provided")
                let postBody = pm.variables.get("postBody");
                console.log("Returning postBody from variables", postBody)

                return JSON.parse(postBody);
            }catch (ex) {
                console.log("Error occurred while getting post body", ex)
                throw (ex)
            }
        }

        this.loadPostBody = (template, pm) => this.setPostBody(this.getPostBody(template), pm)

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

    // console.log(rand.$TS())
    // console.log(rand.$TS("-1month"))
    console.log(rand.$TS("-1month +2days"))

});
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