<?php

function getSignMsg($data,$secret) {   //生成signMsg函数,该函数可直接使用
     $iv     = random_bytes(16);    //生成随机16个字节，用于加密摘要 ，该函数仅对php7用
     //$iv =substr($secret,0,16);      //若是php7，可用 substr($secret,0,16)    
     $cipher = 'AES-256-CBC';       //
     $value  = openssl_encrypt(serialize($data), $cipher, $secret,0,$iv);
     $mac    = hash_hmac('sha256',($iv =  base64_encode($iv)).$value, $secret);
     $json = json_encode(compact('iv', 'value', 'mac'));
     return base64_encode($json);
}

function doPost($url,$applicationID,$signMsg) {

    $curl = curl_init();
    curl_setopt($curl, CURLOPT_URL, $url);
    //curl_setopt($curl, CURLOPT_HEADER, 1);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($curl, CURLOPT_POST, 1);
    $post_data = array(
        "applicationID" => $applicationID,
        "signMsg" => $signMsg
        );
    curl_setopt($curl, CURLOPT_POSTFIELDS, $post_data);
    $result = curl_exec($curl);
    curl_close($curl);
    return $result;
}

$data  = array(                                      // 表单参数
             'lessonId' => "12345",
             'diskId'   => "[356]"
         );

$url           = "直播云提供的URL"; 
$secret        = "直播云提供的密钥";           // secret , 密钥
$applicationID = "直播云提供的applicationID";
$route = "/lesson/add/disk";
$signMsg =  getSignMsg($data,$secret);                         // 生成signMsg

echo doPost($url . $route,$applicationID,$signMsg);

?>
