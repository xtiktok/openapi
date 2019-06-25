package com.zby.openapi;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package com.qingqing.util;

/* Serializable.java
*
* Author:       Ma Bingyao 
* Copyright:    CoolCode.CN
* Version:      2.1
* LastModified: 2006-08-09
* This library is free.  You can redistribute it and/or modify it.
* http://www.coolcode.cn/?p=202
* http://www.coolcode.org/?action=show&id=202
*/
interface Serializable {
    byte[] serialize();
    void unserialize(byte[] ss);
}
