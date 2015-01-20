/* Copyright (C) 2014  Egon Willighagen <egonw@users.sf.net>
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

public class Pathways extends AbstractOPS4JClient {

	private Pathways(String server, String appID, String appKey) throws MalformedURLException {
		super(server, appID, appKey);
	}

	public static Pathways getInstance(String server, String apiID, String appKey) throws MalformedURLException {
		return new Pathways(server, apiID, appKey);
	}

	public static Pathways getInstance(Server server) throws MalformedURLException {
		return new Pathways(server.getServer(), server.getAppID(), server.getAppKey());
	}
	
	public String count(Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		return runRequest(server + "pathways/count", params, objects);
	}

	public String list(int page, int pageSize, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("_page", Integer.toString(page));
		params.put("_pageSize", Integer.toString(pageSize));
		return runRequest(server + "pathways", params, objects);
	}

	public String info(String uri, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		return runRequest(server + "pathway", params, objects);
	}

	public String getCompounds(String uri, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		return runRequest(server + "pathway/getCompounds", params, objects);
	}

	public String getTargets(String uri, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		return runRequest(server + "pathway/getTargets", params, objects);
	}

	public String getPublications(String uri, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		return runRequest(server + "pathway/getReferences", params, objects);
	}

	public String forCompoundCount(String uri, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		return runRequest(server + "pathways/byCompound/count", params, objects);
	}

	public String forCompoundList(String uri, int page, int pageSize, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		params.put("_page", Integer.toString(page));
		params.put("_pageSize", Integer.toString(pageSize));
		return runRequest(server + "pathways/byCompound", params, objects);
	}

	public String forTargetCount(String uri, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		return runRequest(server + "pathways/byTarget/count", params, objects);
	}

	public String forTargetList(String uri, int page, int pageSize, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		params.put("_page", Integer.toString(page));
		params.put("_pageSize", Integer.toString(pageSize));
		return runRequest(server + "pathways/byTarget", params, objects);
	}

	public String forPublicationCount(String uri, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		return runRequest(server + "pathways/byReference/count", params, objects);
	}

	public String forPublicationList(String uri, int page, int pageSize, Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("uri", uri);
		params.put("_page", Integer.toString(page));
		params.put("_pageSize", Integer.toString(pageSize));
		return runRequest(server + "pathways/byReference", params, objects);
	}

	public String organisms(Object... objects) throws IOException {
		Map<String,String> params = new HashMap<String,String>();
		return runRequest(server + "pathways/organisms", params, objects);
	}
}
