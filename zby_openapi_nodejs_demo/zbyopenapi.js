var serialize = require('php-serialize')
var crypto = require("crypto")
var zbyopenapi = module.exports = {}

zbyopenapi.getmsg = function(data,secret) {
    var iv = secret.substr(0,16) 
    var sedata = serialize.serialize(data)
    var cipherChunks = []
    var cipher = crypto.createCipheriv('aes-256-cbc', secret, iv)
    cipher.setAutoPadding(true)
    cipherChunks.push(cipher.update(sedata, "utf8", "base64"))
    cipherChunks.push(cipher.final("base64"))
    var value = cipherChunks.join('')
    iv = Buffer.from(iv).toString('base64')
	    var ivv = iv + value
    var mac =  crypto.createHmac('SHA256', secret).update(ivv).digest('hex')
    var compact = {
        iv    : iv,
        value : value,
        mac   : mac
    } 
    var json = JSON.stringify(compact)
    return Buffer.from(json).toString('base64') 
}

