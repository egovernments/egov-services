var type = localStorage.getItem("type");
$(document).ready(function()
{
    (function(send) {

        XMLHttpRequest.prototype.send = function(data) {

            var authToken = localStorage.getItem("auth");
            if(authToken) this.setRequestHeader('auth-token', authToken);

            send.call(this, data);
        };

    })(XMLHttpRequest.prototype.send);

});