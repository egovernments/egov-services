login = async (username, password) => {
    username = username || "{{username}}"
    password = password || "{{password}}"
    console.log("Executing login function")
    const login_request = {
        "url": "{{url}}/user/oauth/token",
        "method": "POST",
        "header": [
            {
                "key": "Content-Type", "value": "application/json"
            },
            {
                "key": "Authorization", "value": "Basic ZWdvdi11c2VyLWNsaWVudDplZ292LXVzZXItc2VjcmV0"
            }
        ],
        "body": {
            "mode": "urlencoded",
            "urlencoded": [
                {"key": "username", "value": username},
                {"key": "password", "value": password},
                {"key": "grant_type", "value": "password"},
                {"key": "scope", "value": "read"},
                {"key": "tenantId", "value": "{{tenantId}}"},
            ]
        }
    }

    try {
        let data = await pmutil.fetch(pmutil.resolveParamsObject(login_request, true))
        let body = JSON.parse(data.text())
        pm.variables.set("access_token", body.access_token);
    } catch (ex) {
        console.log("Exception occurred during login")
        console.log(ex)
    }
}
