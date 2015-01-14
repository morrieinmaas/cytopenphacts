package org.cytoscape.myapp.my_cyaction_app.internal;

import java.awt.Color;
import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class CreateNetworkTask extends AbstractTask {
	
	private final CyNetworkManager netMgr;
	private final CyNetworkFactory cnf;
	private final CyNetworkNaming namingUtil; 
	
	public CreateNetworkTask(final CyNetworkManager netMgr, final CyNetworkNaming namingUtil, final CyNetworkFactory cnf){
		this.netMgr = netMgr;
		this.cnf = cnf;
		this.namingUtil = namingUtil;
	}
	
	public void run(TaskMonitor monitor) {
		
		// Create an empty network
		CyNetwork myNet = cnf.createNetwork();
		myNet.getRow(myNet).set(CyNetwork.NAME,
				      namingUtil.getSuggestedNetworkTitle("My Network"));
		
		// Add two nodes to the network
		CyNode node1 = myNet.addNode();
		CyNode node2 = myNet.addNode();
		CyNode node3 = myNet.addNode();
		
		// set name for new nodes
		myNet.getDefaultNodeTable().getRow(node1.getSUID()).set("name", "Moritz");
		myNet.getDefaultNodeTable().getRow(node2.getSUID()).set("name", "Fred");
		myNet.getDefaultNodeTable().getRow(node3.getSUID()).set("name", "David");
		
		// Add an edge
		myNet.addEdge(node1, node2, true);
		myNet.addEdge(node2, node3, true);
		myNet.addEdge(node3, node1, true);
				
		netMgr.addNetwork(myNet);
		
		
		
		OpenPhacts ops = new OpenPhacts(myNet);
		try {
			ops.pharma4Target();
			CyNode blaah = myNet.addNode();
			myNet.getDefaultNodeTable().getRow(blaah.getSUID()).set("name", "blaah");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		// Set the variable destroyNetwork to true, the following code will destroy a network
		boolean destroyNetwork = false;
		if (destroyNetwork){
			// Destroy it
			 netMgr.destroyNetwork(myNet);			
		}
		
	}

}
