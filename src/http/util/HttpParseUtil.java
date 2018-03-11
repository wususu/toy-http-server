package http.util;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import javax.servlet.http.Cookie;

public class HttpParseUtil {

	public static final String COOKIES_KEY = "Cookie";
	
	public static final String SERRSION_ID = "hash_sid";
	
	public static String readLine(String raw){
		int firstLineIndex = raw.indexOf("\n");
		String firstLine = checkNotNull(raw.substring(0, firstLineIndex));
		checkArgument(!firstLine.equals(""));
		return firstLine;
	}
	
	public static String parseMethod(String line){
		int firstSpaceIndex = line.indexOf(' ');
		String method =checkNotNull(line.substring(0, firstSpaceIndex));
		checkArgument(!method.equals(""));
		return method;
	}
	
	public static String parseUri(String line){
		int firstSpaceIndex = line.indexOf(' ');
		int lastSpaceIndex = line.lastIndexOf(' ');
		String uri =checkNotNull(line.substring(firstSpaceIndex+1, lastSpaceIndex));
		checkArgument(!uri.equals(""));
		return uri;
	}
	
	public static String parseProtocol(String line){
		int lastSpaceIndex = line.lastIndexOf(' ');
		String protocol =checkNotNull(line.substring(lastSpaceIndex+1, line.length()));
		checkArgument(!protocol.equals(""));
		return protocol;
	}
	
	public static String parseQueryString(String uri){
		int question = uri.indexOf('?');
		String queryString = null;
		if (question >= 0) {
			queryString = uri.substring(question+1, uri.length());
		}
		return queryString == null ? "":queryString;
	}
	
	public static String parseCookieString(String raw){
		int cookieIndex = raw.indexOf(COOKIES_KEY) + COOKIES_KEY.length() +1;
		String subStr = raw.substring(cookieIndex, raw.length());
		int lineIndex = subStr.indexOf('\n');
		String cookies = subStr.substring(0, lineIndex);
		return cookies != null ? cookies : "";
	}
	
	public static List<Cookie> parseCookie(Map<String, String> headers){
		return parseCookie(headers.get(COOKIES_KEY));
	}
	
	public static List<Cookie> parseCookie(String cookieStr){
		String[] strs = cookieStr.split(";");
		String[] keyValue;
		List<Cookie> cookies = new ArrayList<>();
		for (String str : strs) {
			if ( !str.contains("="))
				continue;
			keyValue = str.split("=");
			if (keyValue.length != 2)
				continue;
			try{
				cookies.add(new Cookie(keyValue[0], keyValue[1]));
			}catch (IllegalArgumentException e) {
				// TODO: handle exception
				continue;
			}
		}
		return cookies;
	}
	
	public static String parseSid(String cookies) {
		String sid = " " + SERRSION_ID + "=";
		int sidIndex = cookies.indexOf(sid);
		String hasdSid = null;
		if (sidIndex > 0) {
			sidIndex += sid.length();
			String subStr = cookies.substring(sidIndex, cookies.length());
			int nextIndex = subStr.indexOf(';');
			hasdSid = subStr.substring(0, nextIndex);
		}
		return hasdSid != null ? hasdSid : "";
	}
	
	public static Map<String, String> parseHeader(String raw){
		String[] strs = raw.split("\n");
		String[] keyVaue;
		HashMap<String, String> headers = Maps.newHashMap();
		for (String str : strs) {
			if (!str.contains(":"))
				continue;
			keyVaue = str.split(":");
			if (keyVaue.length != 2) 
				continue;
			headers.put(keyVaue[0], keyVaue[1]);
		}
		return headers;
	}
	
	public static void parseParameters(String queryStr, ParameterMap parameters, String encoding){
		String[] strs = queryStr.split("&");
		String[] keyValue;
		for (String str : strs) {
			if (str.contains("=")) {
				keyValue = str.split("=");
				if (keyValue.length == 2) {
					parameters.put(keyValue[0], keyValue[1]);
				}
			}
		}
	}
}
