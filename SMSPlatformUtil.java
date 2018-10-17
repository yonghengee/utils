package com.yqh.shop.utils;

import org.apache.commons.lang.time.StopWatch;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 短信群发接口
 */
public class SMSPlatformUtil {
	
	private static Logger logger = LogManager.getLogger(SMSPlatformUtil.class.getName());
	
	
	public static void sendSMS(final String mobiles,final String content) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Thread a = new Thread() {
			@Override
			public void run() {
				String SMS_URL = PropUtils.getValue("ISMS_URL");
				String account = PropUtils.getValue("ISMS_ACCOUNT");;
				String passwd =  PropUtils.getValue("ISMS_PWD");
//				SMS_URL = "http://www.106800.com.cn:9885/c123/sendsms";
//				account = "200236";
//				passwd = "yqhzzh6";
				
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(SMS_URL);
				try {
					passwd = new String(MD5Util.encrypt(passwd));
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("uid", account));
					params.add(new BasicNameValuePair("pwd", passwd));
					params.add(new BasicNameValuePair("mobile", mobiles));
					params.add(new BasicNameValuePair("content", content));
					
					/*String URL = SMS_URL + "?uid=" + account + "&pwd=" + passwd + "&mobile=" + mobiles + "&content=" + URLEncoder.encode(content, "GBK");
					System.out.println(URL);*/

					UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(params, "GBK");
					post.setEntity(uefEntity);

					// 设置请求表单
					HttpResponse response = httpclient.execute(post);
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						String jsonResult = EntityUtils.toString(entity);				
						logger.debug(mobiles + "--发送信息:"+content+",返回状态码"+jsonResult);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					httpclient.getConnectionManager().shutdown();
				}
			}
		};
		try {
			a.start();
			a.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stopWatch.stop();
		//System.out.println("花费：" + (stopWatch.getTime() / 1000) + "s");
	}
	
	public static String sendValidateCode(final String mobiles) {
		String code="";
        for (int j = 0; j < 6; j++) {
        	code+=(int)((Math.floor(Math.random()*10)));
		}
        final String validateCode = code; 
        
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Thread a = new Thread() {
			@Override
			public void run() {
				String SMS_URL = PropUtils.getValue("ISMS_URL");
				String account = PropUtils.getValue("ISMS_ACCOUNT");;
				String passwd =  PropUtils.getValue("ISMS_PWD");
				String SMS_SIGN = "";
//				SMS_URL = "http://www.106800.com.cn:9885/c123/sendsms";
//				account = "200236";
//				passwd = "yqhzzh6";




				String content =  "【" + SMS_SIGN + "】 您好，您的验证码："+ validateCode;
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost post = new HttpPost(SMS_URL);
				try {
					passwd = new String(MD5Util.encrypt(passwd));
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("uid", account));
					params.add(new BasicNameValuePair("pwd", passwd));
					params.add(new BasicNameValuePair("mobile", mobiles));
					params.add(new BasicNameValuePair("content", content));
					
					/*String URL = SMS_URL + "?uid=" + account + "&pwd=" + passwd + "&mobile=" + mobiles + "&content=" + URLEncoder.encode(content, "GBK");
					System.out.println(URL);*/

					UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(params, "GBK");
					post.setEntity(uefEntity);

					// 设置请求表单
					HttpResponse response = httpclient.execute(post);
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						//String jsonResult = EntityUtils.toString(entity);				
						//JSONObject jsonObject = JSONObject.parseObject(jsonResult);
						//System.out.println(jsonResult);
						//ResultInfoUtils.errorData(BaseMessageEnum.UNKNOW_ERROR.getRetCode(), "发送失败");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					httpclient.getConnectionManager().shutdown();
				}
			}
		};
		try {
			a.start();
			a.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stopWatch.stop();
		//System.out.println("花费：" + (stopWatch.getTime() / 1000) + "s");
		return validateCode;
	}
	
	public static void main(String[] args) {
		//SMSPlatformUtil.sendValidateCode("13715929522");
//		SMSPlatformUtil.sendSMS("13715929522", "1234578");
		try {
			String txt = sendValidateCodeForChaoyan("13538920684");
		}catch (Exception e){
			e.printStackTrace();
		}
	}


// 符合优先模板：10个线程，每个发送10个号码，耗时22s
// 符合优先模板：10个线程，每个发送1000个号码，耗时


	static public String SMSsend(String url)
	{
		String result = "";
		try{

			URL U = new URL(url);
			URLConnection connection = U.openConnection();
			connection.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine())!= null)
			{
				result += line;
			}
			in.close();
		}catch(Exception e){
			System.out.println("没有结果！"+e);
			result="产生异常";
		}
		return result;
	}

	public static String sendValidateCodeForChaoyan(final String mobiles) {
		String code="";
		for (int j = 0; j < 6; j++) {
			code+=(int)((Math.floor(Math.random()*10)));
		}
		final String validateCode = code;
		String content =  "您好，您的验证码："+ validateCode;
		String CorpID="303516";
		String passwd="Wa281441";
		String LoginName="zhenh";

		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");//可以方便地修改日期格式
		String timeStamp = dateFormat.format(now);
		String strPwsd = MD5Util.encrypt(CorpID+passwd+timeStamp);

		String jhrdh=mobiles;
		String baseUrl = "sms3.mobset.com";//企业CorpID开头数字是3，对应地址是sm3.mobset.com
		String urls="http://"+baseUrl+"/SDK2/Sms_Send.asp?CorpID="+CorpID+"&LoginName="+LoginName+"&TimeStamp="+timeStamp+"&passwd="+strPwsd+"&send_no="+jhrdh+"&Timer=&msg="+content+"";
		System.out.println(urls);
		String txt=SMSsend(urls);
		return validateCode;
	}


	public static String sendActivationCodesForChaoyan(final String mobiles,String codesNum) {
//		String code="";
//		for (int j = 0; j < 6; j++) {
//			code+=(int)((Math.floor(Math.random()*10)));
//		}
//		final String validateCode = code;
		String content =  "您好，您的软件码："+ codesNum;
		String CorpID="303516";
		String passwd="Wa281441";
		String LoginName="zhenh";

		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");//可以方便地修改日期格式
		String timeStamp = dateFormat.format(now);
		String strPwsd = MD5Util.encrypt(CorpID+passwd+timeStamp);

		String jhrdh=mobiles;
		String baseUrl = "sms3.mobset.com";//企业CorpID开头数字是3，对应地址是sm3.mobset.com
		String urls="http://"+baseUrl+"/SDK2/Sms_Send.asp?CorpID="+CorpID+"&LoginName="+LoginName+"&TimeStamp="+timeStamp+"&passwd="+strPwsd+"&send_no="+jhrdh+"&Timer=&msg="+content+"";
		System.out.println(urls);
		String txt=SMSsend(urls);
		return txt;
	}
}