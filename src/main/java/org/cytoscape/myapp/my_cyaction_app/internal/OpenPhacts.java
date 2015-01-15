package org.cytoscape.myapp.my_cyaction_app.internal;

import java.io.IOException;

import org.cytoscape.model.CyNetwork;
import org.json.JSONObject;

import com.github.egonw.ops4j.IStringMatrix;
import com.github.egonw.ops4j.ResponseFormat;
import com.github.egonw.ops4j.StringMatrix;
import com.github.egonw.ops4j.Targets;

public class OpenPhacts {
	
	private CyNetwork myNetwork;
	
	public OpenPhacts(CyNetwork myNetwork) {
		this.myNetwork = myNetwork;
	}
	
	public IStringMatrix pharma4Target() throws IOException {
		Targets client = Targets.getInstance("https://beta.openphacts.org/1.4/",
				"02a13118", "0d8d74d0559e2d9dd2efb1694805f69a");
		String json = client.pharmacologyCount(
			"http://www.conceptwiki.org/concept/00059958-a045-4581-9dc5-e5a08bb0c291", ResponseFormat.JSON
		);
		
		System.out.println("json: " + json);
		JSONObject obj = new JSONObject(json);
		System.out.println("format: " + obj.getString("format"));
		return new StringMatrix();
	}

}
