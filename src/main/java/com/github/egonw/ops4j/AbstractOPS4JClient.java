/* Copyright (C) 2013-2014  Egon Willighagen <egonw@users.sf.net>
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package com.github.egonw.ops4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class AbstractOPS4JClient {

	private HttpClient httpClient = null;
	private String appID;
	private String appKey;

	protected String server;

	public AbstractOPS4JClient(String server, String appID, String appKey, HttpClient httpclient)
	throws MalformedURLException {
		this.server = server;
		if (!this.server.endsWith("/")) this.server += "/";
		new URL(this.server); // validate the server URL
		this.appID = appID;
		this.appKey = appKey;
		this.httpClient = httpclient;
		if (httpclient == null) this.httpClient = new DefaultHttpClient();
	}
	
	protected String runRequest(String call, Map<String, String> params, Object... objects)
	throws ClientProtocolException, IOException, HttpException {
		appID = "ddc7621f&";
		appKey = "6b862cf4119b0baed070f433483b5db8";
		params.put("app_id", appID);
		params.put("app_key", appKey);
		params.put("_format", "ttl"); // the default
		String requestUrl = createRequest(call, params, objects);
		System.out.println("Call: " + requestUrl);
		HttpGet httppost = new HttpGet(requestUrl); 

		HttpResponse response = httpClient.execute(httppost);
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode != 200) throw new HttpException(
			"Expected HTTP 200, but got a " + statusCode + ": " + statusLine.getReasonPhrase()
		);

		HttpEntity responseEntity = response.getEntity();
		InputStream in = responseEntity.getContent();
		StringWriter writer = new StringWriter();
		IOUtils.copy(in, writer, "UTF-8");
		in.close();
		return writer.toString();
	}
	
	private String createRequest(String server, Map<String, String> params, Object... objects)
	throws UnsupportedEncodingException {
		StringBuffer requestURI = new StringBuffer();
		for (int i=0; i<objects.length; i++) {
			Object obj = objects[i];
			if (obj instanceof ResponseFormat) {
				params.put("_format", ((ResponseFormat)obj).getOPSCode());
			} else if (obj instanceof ParameterValue) {
				ParameterValue value = (ParameterValue)obj;
				params.put(value.getParameter().getName(), value.getValue());
			}
		}
		if (!params.isEmpty()) {
			requestURI.append(server).append('?');
			boolean beyondFirst = false;
			for (String key : params.keySet()) {
				if (beyondFirst) requestURI.append('&');
				requestURI.append(key).append('=').append(URLEncoder.encode(params.get(key), "UTF-8"));
				beyondFirst = true;
			}
		}
		return requestURI.toString();
	}

}
