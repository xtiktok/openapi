package main

import (
    "fmt"
    "encoding/base64"
  r "crypto/rand"
    "math/rand"
    "time"
    "serialize"
    "hmac"
    "aes"
    "encoding/json"
    "net/http"
    "net/url"
    "io/ioutil"
)

func main() {
      devUrl := "直播云提供的Host/user/create"
      data := make(map[interface{}]interface{})
      user := make(map[interface{}]interface{})
      users := make([]interface{},0,20)

      user["userPhone"]  = "12634658345"
      user["userName"]   = "张星龙"
      user["userType"]   = "1"
      user["userGender"] = "1"
      users = append(users,user)     //添加用户1
      users = append(users,user)     //添加用户2
      data["userData"] = users
      key := []byte("直播云提供的密钥")
      result := getSignMsg(data,key)
      client := &http.Client{}
      post := url.Values{}
      post.Add("applicationID", "直播云提供的应用ID")
      post.Add("signMsg",result)
      resp, err := client.PostForm(devUrl, post)
      defer resp.Body.Close()
      if err != nil {
          fmt.Println(err.Error())
      }
      if resp.StatusCode == 200 {
          body, _ := ioutil.ReadAll(resp.Body)
          fmt.Println(string(body))
      }

}

func getSignMsg(data interface{},key []byte) string {
      ivBase  := randomBytes(16)
      compact := make(map[string]string)
      serializeResult,_ := serialize.Encode(data)
      value := aes.Encode(serializeResult,key,ivBase)     
      iv := base64.StdEncoding.EncodeToString(ivBase)
      inputString := iv + value
      mac := hmac.Encode(inputString,key)
      compact["iv"]    = iv
      compact["value"] = value
      compact["mac"]   = mac
      jsonResult,_ := json.Marshal(compact)
      return  base64.StdEncoding.EncodeToString(jsonResult)
}

func randomBytes(n int, alphabets ...byte) []byte {
    const alphanum = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    var bytes = make([]byte, n)
    var randby bool
    if num, err := r.Read(bytes); num != n || err != nil {
        rand.Seed(time.Now().UnixNano())
        randby = true
    }
    for i, b := range bytes {
        if len(alphabets) == 0 {
            if randby {
                bytes[i] = alphanum[rand.Intn(len(alphanum))]
            } else {
                bytes[i] = alphanum[b%byte(len(alphanum))]
            }
        } else {
            if randby {
                bytes[i] = alphabets[rand.Intn(len(alphabets))]
            } else {
                bytes[i] = alphabets[b%byte(len(alphabets))]
            }
        }
    }
    return bytes
}
