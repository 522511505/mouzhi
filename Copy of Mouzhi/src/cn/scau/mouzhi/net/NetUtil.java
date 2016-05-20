package cn.scau.mouzhi.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class NetUtil {
	public static String getActivityInfo(int pageNumber,int pageSize){
		String str = null;
		try {
			URL url = new URL("http://121.42.189.168/mouzhi/activity/getActivity");
			Map map = new HashMap();
			map.put("pageNumber", pageNumber);
			map.put("pageSize", pageSize);
			str = submitPostData(url,map);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	public static String submitPostData(URL url, Map params) {

		byte[] data = getRequestData(params).toString().getBytes();// ���������
		try {
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(3000); // �������ӳ�ʱʱ��
			httpURLConnection.setDoInput(true); // �����������Ա�ӷ�������ȡ����
			httpURLConnection.setDoOutput(true); // ����������Ա���������ύ����
			httpURLConnection.setRequestMethod("POST"); // ������Post��ʽ�ύ����
			httpURLConnection.setUseCaches(false); // ʹ��Post��ʽ����ʹ�û���
			// ������������������ı�����
			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// ����������ĳ���
			httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
			// �����������������д������
			OutputStream outputStream = httpURLConnection.getOutputStream();
			outputStream.write(data);

			int response = httpURLConnection.getResponseCode(); // ��÷���������Ӧ��
			if (response == HttpURLConnection.HTTP_OK) {
				InputStream inptStream = httpURLConnection.getInputStream();
				return dealResponseResult(inptStream); // �������������Ӧ���
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static StringBuffer getRequestData(Map params) {
		StringBuffer stringBuffer = new StringBuffer(); // �洢��װ�õ���������Ϣ
		Iterator iter = params.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    stringBuffer.append(entry.getKey()).append("=").append(entry.getValue())
			.append("&");
		} 
		stringBuffer.deleteCharAt(stringBuffer.length() - 1); // ɾ������һ��"&"
		return stringBuffer;
	}
	
	public static String submitPostData(URL url,Map<String, String> params, String encode) {

		byte[] data = getRequestData(params, encode).toString().getBytes();// ���������
		try {
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(3000); // �������ӳ�ʱʱ��
			httpURLConnection.setDoInput(true); // �����������Ա�ӷ�������ȡ����
			httpURLConnection.setDoOutput(true); // ����������Ա���������ύ����
			httpURLConnection.setRequestMethod("POST"); // ������Post��ʽ�ύ����
			httpURLConnection.setUseCaches(false); // ʹ��Post��ʽ����ʹ�û���
			// ������������������ı�����
			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// ����������ĳ���
			httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
			// �����������������д������
			OutputStream outputStream = httpURLConnection.getOutputStream();
			outputStream.write(data);

			int response = httpURLConnection.getResponseCode(); // ��÷���������Ӧ��
			if (response == HttpURLConnection.HTTP_OK) {
				InputStream inptStream = httpURLConnection.getInputStream();
				return dealResponseResult(inptStream); // �������������Ӧ���
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static StringBuffer getRequestData(Map<String, String> params, String encode) {
		StringBuffer stringBuffer = new StringBuffer(); // �洢��װ�õ���������Ϣ
		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				stringBuffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encode))
						.append("&");
			}
			stringBuffer.deleteCharAt(stringBuffer.length() - 1); // ɾ������һ��"&"
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuffer;
	}

	public static String dealResponseResult(InputStream inputStream) {
		String resultData = null; // �洢������
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		try {
			while ((len = inputStream.read(data)) != -1) {
				byteArrayOutputStream.write(data, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			resultData = new String(byteArrayOutputStream.toByteArray(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultData;
	}
}
