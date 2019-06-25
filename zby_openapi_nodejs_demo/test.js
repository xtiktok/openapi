var zbyopenapi = require("./zbyopenapi")
var http       = require("http")
var querystring = require('querystring');
var url           = "直播云提供的url"
var secret        = "直播云提供的密钥"
var applicationID = "直播云提供的应用id"
var path          = "/lesson/add/disk"
var data          = {
    "lessonId" : "12345",
    "diskId"   : "[356]"
}

var post_data = querystring.stringify({
    "applicationID" : applicationID,
    "signMsg"       : zbyopenapi.getmsg(data,secret)
});

var post_options = {
    host: url,
    path: path,
    method: 'POST',
    headers: {
       'Content-Type': 'application/x-www-form-urlencoded'
    }
};
var post_req = http.request(post_options, function(res) {
    res.setEncoding('utf8');
    res.on('data', function (chunk) {
        console.log(chunk);
    });
});
post_req.write(post_data);
post_req.end();
