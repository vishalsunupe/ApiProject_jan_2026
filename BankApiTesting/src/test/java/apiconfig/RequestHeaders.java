package apiconfig;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestHeaders {
	
	public  Map<String, String> requestHeaders() {
		Map<String, String> reqHeader = new HashMap<String, String>();
		
		reqHeader.put("Content-Type", "application/json");
		//reqHeader.put("Accept-Encoding", "gzip, deflate, br");
		
		return reqHeader;
	}

}
