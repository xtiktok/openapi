import java.io.IOException;
import java.util.*;
import com.zby.openapi.*;
import com.alibaba.fastjson.JSON;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Example {
    public static final String secret = "直播云提供的密钥";
    public static final String  applicationID = "直播云提供的应用ID";
    public static final String  url = "直播云提供的url";
    public static void main(String[] args) throws Exception  {
        Map data=new HashMap();
        Map form = new HashMap();
        //表单数据
        data.put("classroomId", "1320447");
        data.put("startTime", "2018-07-29 09:00");
        data.put("endTime", "2018-07-29 12:00");
        data.put("genre", "0");
        form.put("applicationID",applicationID);
        form.put("signMsg",Example.getMsg(secret,data));
        System.out.println(Example.send(url +"/lesson/create",form,"utf-8"));
    }

    public static String getMsg(String secret,Object data) throws Exception  {
        Map compact =new HashMap();
        //用密钥的前16位，生成摘要
        String ivString = secret.substring(0,16);
        byte[] ivByte = ivString.getBytes();

        //对表单数据进行序列化
        String str = new String(PHPSerializer.serialize(data));
        AES aes = new AES(secret,ivByte);
        //进行aes_256_cbc加密
        byte[] crypted = aes.encrypt(str.getBytes());

        //对aes加密结果进行base64编码
        String value = new String(com.zby.openapi.Base64.encode(crypted));

        //对摘要信息进行base64编码
        String iv = com.zby.openapi.Base64.encode(ivString);

        //生成最终的摘要信息
        String ivv = iv + value;

        //生成摘要经过sha256加密后的密文
        String mac = HmacSha256.hashHmac(ivv,secret);

        //将正文信息，摘要信息 放在一块
        compact.put("iv", iv);
        compact.put("value", value);
        compact.put("mac", mac);

        //转换成json格式字符串
        String json = JSON.toJSONString(compact);

        //经过base64编码生成最终的签名
        String msg  = com.zby.openapi.Base64.encode(json);
        return msg;
    }
    public static String send(String url, Map<String,String> map,String encoding) throws ParseException, IOException {
        String body = "";

        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        //装填参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        //设置参数到请求对象中
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));


        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encoding);
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return body;
    }
}
