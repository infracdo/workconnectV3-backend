package com.tracker_app.tracker;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

import com.tracker_app.tracker.Helper.prometheus_helper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SmokeTest {

    @LocalServerPort
	private int port;

    @Autowired
	private TestRestTemplate restTemplate;

	@Autowired
    private prometheus_helper prometheus_help;

	@Test
	void shouldReturnDefaultMessage() throws Exception {
		System.out.println("Starting test unit test");
		//prometheus();
		//snmp_in();
		//snmp_out();
		//test1();
		//test2();
		
	}
	void prometheus() throws Exception{
		Object data = prometheus_help.get_circuit_data("ST-1413","2021-12-08",Long.valueOf(24));
		System.out.println(data);
	}
	void snmp_in() throws Exception{
		Object data = prometheus_help.get_site_in_network_traffic("ST-1413","2021-12-08",24,5);
		System.out.println(data);
	}
	void snmp_out() throws Exception{
		Object data = prometheus_help.get_site_out_network_traffic("ST-1413","2021-12-08",24,5);
		System.out.println(data);
	}
	/*
	void test1() throws Exception{
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJwRjIzeHV2OXlEV3h5TzlYbjdRMXZpOGo0ZEtNejBkWl9tUHVMMVhqXzJjIn0.eyJleHAiOjE2NDA3NDU1NTcsImlhdCI6MTY0MDc0NTI1NywiYXV0aF90aW1lIjoxNjQwNzM5NDM2LCJqdGkiOiJmNzBlNjJlMy02OWZmLTRhYTQtYjcwMS0xY2MzZDQzNzI4N2QiLCJpc3MiOiJodHRwczovL2RldmVsb3BlcnMuYXBvbGxvLmNvbS5waDo4NDQzL2F1dGgvcmVhbG1zL3dvcmtjb25uZWN0LXN0YWdpbmciLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiZDE3M2Q2MjEtNjE2NC00ZjkzLWFlOTktYzY0N2ZlNzk0YTk3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYXBvbGxvIiwibm9uY2UiOiJkMDNiM2I0ZS0yMGZiLTQwNzUtOTA4NS0wNjcxNzJjOGFhOTUiLCJzZXNzaW9uX3N0YXRlIjoiNjAyNzcyZWYtYThmMS00YzBmLTllZmItMmYzNDc4MjY2MzU2IiwiYWNyIjoiMCIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3dvcmtjb25uZWN0LXVhdC5hcG9sbG9nbG9iYWwubmV0LyoiLCJodHRwOi8vd29ya2Nvbm5lY3QtdWF0LmFwb2xsb2dsb2JhbC5uZXQvKiIsIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIlJPTEVfVVNFUkFETUlOX1ZJRVdfVVNFUiIsIlJPTEVfU0lURVNfVklFVyIsIlJPTEVfU0lURVNfSU5URVJGQUNFU19WSUVXIiwiR1JPVVBfQVBPTExPX1VTRVIiLCJST0xFX1VTRVJBRE1JTl9DUkVBVEVfVVNFUiIsImRlZmF1bHQtcm9sZXMtd29ya2Nvbm5lY3Qtc3RhZ2luZyIsIlJPTEVfQUdFTlRUUkFDS0lOR19WSUVXIiwiUk9MRV9TSVRFU19BU1NFVFNfVklFVyIsIm9mZmxpbmVfYWNjZXNzIiwiUk9MRV9TSVRFU19ERVZJQ0VTX1ZJRVciLCJST0xFX05FVEJPWF9WSUVXX0NJUkNVSVRTIiwiUk9MRV9TSVRFU19ORVRXT1JLX1ZJRVciLCJ1bWFfYXV0aG9yaXphdGlvbiIsIlJPTEVfVVNFUkFETUlOX1VQREFURV9VU0VSIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgZW1haWwgcHJvZmlsZSIsInNpZCI6IjYwMjc3MmVmLWE4ZjEtNGMwZi05ZWZiLTJmMzQ3ODI2NjM1NiIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6Im5lbXJvZEBhcG9sbG9nbG9iYWwubmV0IEJhcXVpcmFuIiwicHJlZmVycmVkX3VzZXJuYW1lIjoibmVtcm9kQGFwb2xsb2dsb2JhbC5uZXQiLCJnaXZlbl9uYW1lIjoibmVtcm9kQGFwb2xsb2dsb2JhbC5uZXQiLCJmYW1pbHlfbmFtZSI6IkJhcXVpcmFuIiwiZW1haWwiOiJuZW1yb2RAYXBvbGxvZ2xvYmFsLm5ldCJ9.qUpCDw53W0mIXysy55mwon8zZdMbiIRJryK6t9SwIy-_VnmwiZbUK_je4osonOHLN6ubB7Uo-H7gvJvbIgd3-uMTFU9zcujOBPFX-rPVA4711wDqBSm_yhyxmxmpUD9KgpdrMhXApnLEfEeDR2M_5Rg9Z0cFxic5rWe3E8G_-L8aJlT9jmzDcP0Tsdp08_DFuOQyJtKg6gJReFKEokGoiiE5LDQLNpMCpaTzG9BcDtQDd6Sedhwy2LjBvUYJdwQxlgz2E-mL2kYroQolMPZ5zaBCdWnU-ytTEJzujamaaviYZ76r_xCsQYI-bHAWbSpXxpwrq9k48ZXMr5PohqaCzg");
		String url = "http://localhost:" + port + "/api/atis/site/list?site_id=&siteName=Tuburan Town Plaza&tenant=&no_links_up=&circuit_provider=&siteStatus=&region=&group=&pageNo=0&pageSize=10";
		HttpEntity<String> request = new HttpEntity<String>(headers);

		ResponseEntity<String> test1 = this.restTemplate.exchange(url, HttpMethod.GET, request, String.class);
		JSONObject response = new JSONObject(test1.getBody());
		JSONArray content = response.getJSONArray("content");
		if(content.length()>0){
			for(int i=0; i<content.length(); i++){
				System.out.println("TEST1 SITE NAME: "+content.getJSONObject(i).getString("name"));
			}
		}
		System.out.println("test1: "+response.getJSONArray("content").length());
	}
	void test2() throws Exception{
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJwRjIzeHV2OXlEV3h5TzlYbjdRMXZpOGo0ZEtNejBkWl9tUHVMMVhqXzJjIn0.eyJleHAiOjE2NDA3NDU1NTcsImlhdCI6MTY0MDc0NTI1NywiYXV0aF90aW1lIjoxNjQwNzM5NDM2LCJqdGkiOiJmNzBlNjJlMy02OWZmLTRhYTQtYjcwMS0xY2MzZDQzNzI4N2QiLCJpc3MiOiJodHRwczovL2RldmVsb3BlcnMuYXBvbGxvLmNvbS5waDo4NDQzL2F1dGgvcmVhbG1zL3dvcmtjb25uZWN0LXN0YWdpbmciLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiZDE3M2Q2MjEtNjE2NC00ZjkzLWFlOTktYzY0N2ZlNzk0YTk3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYXBvbGxvIiwibm9uY2UiOiJkMDNiM2I0ZS0yMGZiLTQwNzUtOTA4NS0wNjcxNzJjOGFhOTUiLCJzZXNzaW9uX3N0YXRlIjoiNjAyNzcyZWYtYThmMS00YzBmLTllZmItMmYzNDc4MjY2MzU2IiwiYWNyIjoiMCIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3dvcmtjb25uZWN0LXVhdC5hcG9sbG9nbG9iYWwubmV0LyoiLCJodHRwOi8vd29ya2Nvbm5lY3QtdWF0LmFwb2xsb2dsb2JhbC5uZXQvKiIsIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIlJPTEVfVVNFUkFETUlOX1ZJRVdfVVNFUiIsIlJPTEVfU0lURVNfVklFVyIsIlJPTEVfU0lURVNfSU5URVJGQUNFU19WSUVXIiwiR1JPVVBfQVBPTExPX1VTRVIiLCJST0xFX1VTRVJBRE1JTl9DUkVBVEVfVVNFUiIsImRlZmF1bHQtcm9sZXMtd29ya2Nvbm5lY3Qtc3RhZ2luZyIsIlJPTEVfQUdFTlRUUkFDS0lOR19WSUVXIiwiUk9MRV9TSVRFU19BU1NFVFNfVklFVyIsIm9mZmxpbmVfYWNjZXNzIiwiUk9MRV9TSVRFU19ERVZJQ0VTX1ZJRVciLCJST0xFX05FVEJPWF9WSUVXX0NJUkNVSVRTIiwiUk9MRV9TSVRFU19ORVRXT1JLX1ZJRVciLCJ1bWFfYXV0aG9yaXphdGlvbiIsIlJPTEVfVVNFUkFETUlOX1VQREFURV9VU0VSIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgZW1haWwgcHJvZmlsZSIsInNpZCI6IjYwMjc3MmVmLWE4ZjEtNGMwZi05ZWZiLTJmMzQ3ODI2NjM1NiIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6Im5lbXJvZEBhcG9sbG9nbG9iYWwubmV0IEJhcXVpcmFuIiwicHJlZmVycmVkX3VzZXJuYW1lIjoibmVtcm9kQGFwb2xsb2dsb2JhbC5uZXQiLCJnaXZlbl9uYW1lIjoibmVtcm9kQGFwb2xsb2dsb2JhbC5uZXQiLCJmYW1pbHlfbmFtZSI6IkJhcXVpcmFuIiwiZW1haWwiOiJuZW1yb2RAYXBvbGxvZ2xvYmFsLm5ldCJ9.qUpCDw53W0mIXysy55mwon8zZdMbiIRJryK6t9SwIy-_VnmwiZbUK_je4osonOHLN6ubB7Uo-H7gvJvbIgd3-uMTFU9zcujOBPFX-rPVA4711wDqBSm_yhyxmxmpUD9KgpdrMhXApnLEfEeDR2M_5Rg9Z0cFxic5rWe3E8G_-L8aJlT9jmzDcP0Tsdp08_DFuOQyJtKg6gJReFKEokGoiiE5LDQLNpMCpaTzG9BcDtQDd6Sedhwy2LjBvUYJdwQxlgz2E-mL2kYroQolMPZ5zaBCdWnU-ytTEJzujamaaviYZ76r_xCsQYI-bHAWbSpXxpwrq9k48ZXMr5PohqaCzg");
		String url = "http://localhost:" + port + "/api/atis/site/list?site_id=&siteName=Tubu&tenant=&no_links_up=&circuit_provider=&siteStatus=&region=&group=&pageNo=0&pageSize=10";
		HttpEntity<String> request = new HttpEntity<String>(headers);

		ResponseEntity<String> test1 = this.restTemplate.exchange(url, HttpMethod.GET, request, String.class);
		JSONObject response = new JSONObject(test1.getBody());
		JSONArray content = response.getJSONArray("content");
		if(content.length()>0){
			for(int i=0; i<content.length(); i++){
				System.out.println("TEST2 SITE NAME: "+content.getJSONObject(i).getString("name"));
			}
		}
		System.out.println("test2: "+response.getJSONArray("content").length());
	}
	*/
}
