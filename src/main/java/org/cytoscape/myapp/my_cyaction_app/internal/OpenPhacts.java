package org.cytoscape.myapp.my_cyaction_app.internal;

import java.io.IOException;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.egonw.ops4j.*;
/*import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.shared.PrefixMapping;*/

public class OpenPhacts {
	
	private CyNetwork myNetwork;
	
	public OpenPhacts(CyNetwork myNetwork) {
		this.myNetwork = myNetwork;
	}
	
	public void pharma4Compound(String compound, CyNode root) throws IOException {
		
		Compounds client = Compounds.getInstance("https://beta.openphacts.org/1.4/",
				"02a13118", "0d8d74d0559e2d9dd2efb1694805f69a");
		String json = client.pharmacologyCount(
			compound, ResponseFormat.JSON,
			new ParameterValue(new Parameter("min-pChembl"), "5"),
			new ParameterValue(new Parameter("target_organism"), "Homo sapiens")
		); 
		
		System.out.println("json: " + json);
		JSONObject obj = new JSONObject(json);
	    int activityCount = obj.getJSONObject("result")
	       .getJSONObject("primaryTopic")
	       .getInt("compoundPharmacologyTotalResults");
		System.out.println("RESULTS: " + activityCount);
		//CyNode node = myNetwork.addNode();
		//myNetwork.getDefaultNodeTable().getRow(node.getSUID()).set("name", "JANNI");
		
		String json2 = client.pharmacologyList(
				compound, 1, activityCount, ResponseFormat.JSON,  
				new ParameterValue(new Parameter("min-pChembl"), "5"), 
				new ParameterValue(new Parameter("target_organism"), "Homo sapiens")
				);
		
		System.out.println("json2: " + json2);
		JSONArray activities = new JSONObject(json2)
		    .getJSONObject("result")
		    .getJSONArray("items");
		for (int actCounter=0; actCounter < activities.length(); actCounter++) {
			JSONObject activityObj = activities.getJSONObject(actCounter);
			CyNode targetNode = myNetwork.addNode();
			myNetwork.getDefaultNodeTable().getRow(targetNode.getSUID()).set("name", activityObj
				    .getJSONObject("hasAssay")
				    .getJSONObject("hasTarget")
				    .getString("title"));
			myNetwork.addEdge(root, targetNode, true);
		}
	}

}
