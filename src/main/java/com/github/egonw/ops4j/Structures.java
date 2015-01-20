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

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;

public class Structures extends AbstractOPS4JClient {

	private Structures(String server, String appID, String appKey) throws MalformedURLException {
		super(server, appID, appKey, null);
	}

	public static Structures getInstance(String server, String apiID, String appKey) throws MalformedURLException {
		return new Structures(server, apiID, appKey);
	}

	public static Structures getInstance(Server server) throws MalformedURLException {
		return new Structures(server.getServer(), server.getAppID(), server.getAppKey());
	}
	
	public String smiles2uri(String smiles, Object... objects) throws ClientProtocolException, IOException, HttpException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("smiles", smiles);
		return runRequest(server + "structure", params, objects);
	}

	public String inchi2uri(String inchi, Object... objects) throws ClientProtocolException, IOException, HttpException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("inchi", inchi);
		return runRequest(server + "structure", params, objects);
	}

	public String inchikey2uri(String inchikey, Object... objects) throws ClientProtocolException, IOException, HttpException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("inchi_key", inchikey);
		return runRequest(server + "structure", params, objects);
	}

	public String tanimotoSimilarityFrom(String smiles, float treshold, int start, int count, Object... objects)
	throws ClientProtocolException, IOException, HttpException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("searchOptions.Molecule", smiles);
		params.put("searchOptions.SimilarityType", "0"); // Tanimoto
		params.put("searchOptions.Threshold", Float.toString(treshold));
		params.put("resultOptions.Start", "" + Integer.toString(start));
		params.put("resultOptions.Count", "" + Integer.toString(count));
		return runRequest(server + "structure/similarity", params, objects);
	}

	public String tanimotoSimilarity(String smiles, float treshold, Object... objects)
	throws ClientProtocolException, IOException, HttpException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("searchOptions.Molecule", smiles);
		params.put("searchOptions.SimilarityType", "0"); // Tanimoto
		params.put("searchOptions.Threshold", Float.toString(treshold));
		return runRequest(server + "structure/similarity", params, objects);
	}

	public String similarity(String smiles, Object... objects)
	throws ClientProtocolException, IOException, HttpException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("searchOptions.Molecule", smiles);
		params.put("resultOptions.Count", "" + Integer.toString(25));
		params.put("searchOptions.SimilarityType", "0"); // Tanimoto
		params.put("searchOptions.Threshold", Float.toString(0.5f));
		return runRequest(server + "structure/similarity", params, objects);
	}

	public String substructure(String smiles, int start, int count, Object... objects)
	throws ClientProtocolException, IOException, HttpException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("searchOptions.Molecule", smiles);
		params.put("searchOptions.MolType", "SMILES");
		params.put("resultOptions.Start", "" + Integer.toString(start));
		params.put("resultOptions.Count", "" + Integer.toString(count));
		return runRequest(server + "structure/substructure", params, objects);
	}

	public String smarts(String smarts, int start, int count, Object... objects)
	throws ClientProtocolException, IOException, HttpException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("searchOptions.Molecule", smarts);
		params.put("searchOptions.MolType", "SMARTS");
		params.put("resultOptions.Start", "" + Integer.toString(start));
		params.put("resultOptions.Count", "" + Integer.toString(count));
		return runRequest(server + "structure/substructure", params, objects);
	}

	public String exact(String smarts, Object... objects)
	throws ClientProtocolException, IOException, HttpException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("searchOptions.Molecule", smarts);
		params.put("searchOptions.MatchType", "0");
		return runRequest(server + "structure/exact", params, objects);
	}

	public String allTautomers(String smarts, Object... objects)
	throws ClientProtocolException, IOException, HttpException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("searchOptions.Molecule", smarts);
		params.put("searchOptions.MatchType", "1");
		return runRequest(server + "structure/exact", params, objects);
	}

	public String sameSkeleton(String smarts, Object... objects)
	throws ClientProtocolException, IOException, HttpException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("searchOptions.Molecule", smarts);
		params.put("searchOptions.MatchType", "3");
		return runRequest(server + "structure/exact", params, objects);
	}
}
