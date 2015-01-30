package org.cytoscape.myapp.my_cyaction_app.internal;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.session.CyNetworkNaming;
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
		
		
		// set name for new nodes
		myNet.getDefaultNodeTable().getRow(node1.getSUID()).set("name", "Quercetin");

		
		// Add an edge
		//myNet.addEdge(node1, node2, true);

				
		netMgr.addNetwork(myNet);
		
		
		
		OpenPhacts ops = new OpenPhacts(myNet);
		try {
			String compound = "http://www.conceptwiki.org/concept/index/8565e215-8583-447a-b796-f16938b3e72e";
			ops.pharma4Compound(compound, node1);
			//CyNode blaah = myNet.addNode();
			//myNet.getDefaultNodeTable().getRow(blaah.getSUID()).set("name", "David");
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
