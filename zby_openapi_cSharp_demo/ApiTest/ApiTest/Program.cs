using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using ZbyOpenApi;
using Newtonsoft.Json;
using System;
namespace ApiTest
{
    class Test
    {
        static void Main(string[] args)
        {
            Hashtable data  =    new Hashtable();
            ArrayList users = new ArrayList();
            Hashtable user1 = new Hashtable();
            Hashtable user2 = new Hashtable();
            user1["userPhone"]  = "13895675049";
            user1["userName"]   = "张一";
            user1["userType"]   = 1;
            user1["userGender"] = 1;
            users.Add(user1);                          //添加第一个人

            user2["userPhone"] = "13895675048";
            user2["userName"] = "张二";
            user2["userType"] = 1;
            user2["userGender"] = 1;
            users.Add(user2);                          //添加第二个人

            data["userData"] = users;

            var applicationID = "applicationID";        //直播云提供的应用ID
            var secret = "secret";                      //直播云提供的密钥
            var url = "http://Host" + "/user/create";   //直播云提供的地址
            var signMsg = Aes.getSign(secret,data);
            var postData = "applicationID=" + applicationID + "&" + "signMsg=" + signMsg;
            Console.WriteLine(postData);
            Console.WriteLine(signMsg);
            Console.Write(Test.SendHttpRequest(url,"POST", postData));
            Console.ReadLine();
        }

        public static string SendHttpRequest(string requestURI, string requestMethod, string json)
        {
            try
            {
                string requestData = json;
                string serviceUrl = requestURI;
                HttpWebRequest myRequest = (HttpWebRequest)WebRequest.Create(serviceUrl);
                myRequest.Method = requestMethod;
                byte[] buf = System.Text.Encoding.GetEncoding("utf-8").GetBytes(requestData);
                myRequest.ContentLength = buf.Length;
                myRequest.Timeout = 5000;
                myRequest.ContentType = "application/x-www-form-urlencoded";
                myRequest.MaximumAutomaticRedirections = 1;
                myRequest.AllowAutoRedirect = true;
                Stream newStream = myRequest.GetRequestStream();
                newStream.Write(buf, 0, buf.Length);
                newStream.Close();

                HttpWebResponse myResponse = (HttpWebResponse)myRequest.GetResponse();
                StreamReader reader = new StreamReader(myResponse.GetResponseStream(), Encoding.UTF8);
                string ReqResult = reader.ReadToEnd();
                reader.Close();
                myResponse.Close();
                return ReqResult;
            }
            catch (UriFormatException e) {
                return "something error";
            }
        }

    }
}
