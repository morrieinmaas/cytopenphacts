/* Copyright (C) 2013  Egon Willighagen <egonw@users.sf.net>
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
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class Compounds extends AbstractOPS4JClient {

	private Compounds(String server, String appID, String appKey) throws MalformedURLException {
		super(server, appID, appKey);
	}

	public static Compounds getInstance(String server, String apiID, String appKey) throws MalformedURLException {
		return new Compounds(server, apiID, appKey);
	}

	public static Compounds getInstance(Server server) throws MalformedURLException {
		return new Compounds(server.getServer(), server.getAppID(), server.getAppKey());
	}
	
	public String info(String uri, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		return runRequest(server + "compound", params, objects);
	}

	public String classifications(String uri, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		return runRequest(server + "compound/classifications", params, objects);
	}

	public String pharmacologyCount(String uri, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		return runRequest(server + "compound/pharmacology/count", params, objects);
	}

	public String pharmacologyList(String uri, int page, int pageSize, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		params.put("_page", Integer.toString(page));
		params.put("_pageSize", Integer.toString(pageSize));
		return runRequest(server + "compound/pharmacology/pages", params, objects);
	}

	public String compoundByClassCount(String uri, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		return runRequest(server + "compound/members/count", params, objects);
	}

	public String compoundByClassList(String uri, int page, int pageSize, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		params.put("_page", Integer.toString(page));
		params.put("_pageSize", Integer.toString(pageSize));
		return runRequest(server + "compound/members/pages", params, objects);
	}

	public String classPharmacologyCount(String uri, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		return runRequest(server + "compound/tree/pharmacology/count", params, objects);
	}

	public String classPharmacologyList(String uri, int page, int pageSize, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		params.put("_page", Integer.toString(page));
		params.put("_pageSize", Integer.toString(pageSize));
		return runRequest(server + "compound/tree/pharmacology/pages", params, objects);
	}

	public final static IParameter TREE = new Parameter("tree");
	public String targetClassificationsFor(String uri, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		return runRequest(server + "compound/classificationsForTargets", params, objects);
	}

}
