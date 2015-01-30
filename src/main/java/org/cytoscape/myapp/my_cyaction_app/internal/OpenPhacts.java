package org.cytoscape.myapp.my_cyaction_app.internal;

import java.io.IOException;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.egonw.ops4j.*;

public class OpenPhacts {

	private CyNetwork myNetwork;

	public OpenPhacts(CyNetwork myNetwork) {
		this.myNetwork = myNetwork;
	}

	public void pharma4Compound(String compound, CyNode root)
			throws IOException {

		Compounds client = Compounds.getInstance(
				"https://beta.openphacts.org/1.4/", "02a13118",
				"0d8d74d0559e2d9dd2efb1694805f69a");

		Targets clientT = Targets.getInstance(
				"https://beta.openphacts.org/1.4/", "02a13118",
				"0d8d74d0559e2d9dd2efb1694805f69a");

		Pathways clientP = Pathways.getInstance(
				"https://beta.openphacts.org/1.4/", "02a13118",
				"0d8d74d0559e2d9dd2efb1694805f69a");

		String jsonPharmacologyCount = client.pharmacologyCount(compound,
				ResponseFormat.JSON, new ParameterValue(new Parameter(
						"min-pChembl"), "5"), new ParameterValue(new Parameter(
						"target_organism"), "Homo sapiens"));

		System.out.println("json: " + jsonPharmacologyCount);
		JSONObject obj = new JSONObject(jsonPharmacologyCount);
		int activityCount = obj.getJSONObject("result")
				.getJSONObject("primaryTopic")
				.getInt("compoundPharmacologyTotalResults");
		System.out.println("RESULTS: " + activityCount);

		String jsonPharmacologyList = client.pharmacologyList(compound, 1,
				activityCount, ResponseFormat.JSON, new ParameterValue(
						new Parameter("min-pChembl"), "5"), new ParameterValue(
						new Parameter("target_organism"), "Homo sapiens"));

		System.out.println("jsonPharmacologyList: " + jsonPharmacologyList);

		JSONArray activities = new JSONObject(jsonPharmacologyList)
				.getJSONObject("result").getJSONArray("items");

		for (int actCounter = 0; actCounter < activities.length(); actCounter++) {
			JSONObject activityObj = activities.getJSONObject(actCounter);

			// Creates all the 1st layer Targets
			CyNode targetNode = myNetwork.addNode();
			myNetwork
					.getDefaultNodeTable()
					.getRow(targetNode.getSUID())
					.set("name",
							activityObj.getJSONObject("hasAssay")
									.getJSONObject("hasTarget")
									.getString("title"));
			myNetwork.addEdge(root, targetNode, true);

			// String jsonPway = " ";

			// find the URI for all the targets
			for (int targetCounter = 0; targetCounter < activities.length(); targetCounter++) {

				String uri = " ";
				String jsonInfo = " ";

				//JSONObject targetObj = activities.getJSONObject(targetCounter);
				uri = getURI(activities); // THIS IS WHERE THE MISTAKE HAPPENS
				// Egon: what is going wrong with the above line? I don't get
				// any error

				// System.out.println("URI: " + uri);

				// find target info
				jsonInfo = clientT.info(uri, ResponseFormat.JSON); // THIS IS
																	// WHERE THE
																	// MISTAKE
																	// HAPPENS
				// Egon: what is going wrong with the above line? I don't get
				// any error

				// System.out.println("jsonURI: " + jsonInfo);

				// create 2nd layer

				// Egon: consider writing comments that are more descriptive...
				// what is this second layer?

				JSONObject NamingObj = new JSONObject(jsonInfo).getJSONObject(
						"result").getJSONObject("primaryTopic");

				JSONArray Name = getName(NamingObj.getJSONArray("perfectMatch"));

				for (int nameCounter = 0; nameCounter < NamingObj.length(); nameCounter++) {

					// JSONObject Name = getName2(NamingObj); // THIS IS WHERE
					// THE
					// MISTAKE HAPPENS

					// Egon: What are you trying to do in the previous line?

					NamingObj.getJSONArray("exactMatch");

					CyNode nameNode = myNetwork.addNode();
					myNetwork.getDefaultNodeTable().getRow(nameNode.getSUID())
							.set("name", getName2(Name));
					myNetwork.addEdge(root, targetNode, true);

				}
			}
		}
	}

	/*
	 * private String getCheck(JSONObject targetURI) { // Egon: what does this
	 * method do? I don't know what a "check" is in // this context... JSONArray
	 * array = targetURI.getJSONArray("exactMatch"); JSONArray interactsWith =
	 * null; for (int i = 0; i < array.length(); i++) { Object obj =
	 * array.get(i); if (obj instanceof JSONObject) { if (((JSONObject)
	 * obj).has("interactsWith")) { // Egon: what do you try to do here?
	 * interactsWith = ((JSONObject) obj) .getJSONArray("interactsWith"); return
	 * interactsWith; } for (int j = 0; j < array.length(); j++) { Object cnt =
	 * interactsWith.get(j); if (cnt instanceof JSONObject) { if (((JSONObject)
	 * cnt).has("label")) { return ((JSONObject) cnt).getString("label"); } } }
	 * } }
	 * 
	 * return "?"; }
	 */

	private String getURI(JSONArray targetArray) {
		for (int i = 0; i < targetArray.length(); i++) {
			Object obj = targetArray.get(i);
			if (obj instanceof JSONObject) {
				if (((JSONObject) obj).has("hasAssay"))
					return ((JSONObject) obj).getJSONObject("hasAssay")
							.getJSONObject("hasTarget")
							.getJSONObject("hasTargetComponent")
							.getJSONObject("exactMatch").getString("_about");
			}
		}
		return "?";
	}

	private JSONArray getName(JSONArray targetArray) {
		for (int i = 0; i < targetArray.length(); i++) {
			Object obj = targetArray.get(i);
			if (obj instanceof JSONObject) {
				if (((JSONObject) obj).has("interactsWith"))
					return ((JSONObject) obj).getJSONArray("interactsWith");
			}
		}
		return targetArray;
	}

	private String getName2(JSONArray targetArray) {
		JSONArray array = getName(targetArray);
		for (int i = 0; i < array.length(); i++) {
			Object obj = array.get(i);
			if (obj instanceof JSONObject) {
				if (((JSONObject) obj).has("label"))
					return ((JSONObject) obj).getString("label");
			}
		}
		return "?";
	}

}