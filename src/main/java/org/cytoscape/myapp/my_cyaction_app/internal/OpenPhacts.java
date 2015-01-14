package org.cytoscape.myapp.my_cyaction_app.internal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.cytoscape.model.CyNetwork;

import com.github.egonw.ops4j.*;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.shared.PrefixMapping;
public class OpenPhacts {
	
	//private CyNetwork myNetwork;
	
	public OpenPhacts(CyNetwork myNetwork) {
	}
	
	private static final String COMPOUND_PHARMA_BY_TARGET =
			"SELECT * WHERE {" +
			" ?item <http://rdf.ebi.ac.uk/terms/chembl#hasMolecule> ?chembl_compound_uri ; " +
			"   <http://rdf.ebi.ac.uk/terms/chembl#hasAssay> ?assay_uri . " +
			" ?chembl_compound_uri <http://www.w3.org/2004/02/skos/core#exactMatch> ?rsc_compound_uri . " +
			" OPTIONAL { ?item <http://rdf.ebi.ac.uk/terms/chembl#publishedType> ?published_type . } " +
			" OPTIONAL { ?item <http://rdf.ebi.ac.uk/terms/chembl#publishedRelation> ?published_relation . } " +
			" OPTIONAL { ?item <http://rdf.ebi.ac.uk/terms/chembl#publishedValue> ?published_value . } " +
			" OPTIONAL { ?item <http://rdf.ebi.ac.uk/terms/chembl#publishedUnits> ?published_unit . } " +
			" OPTIONAL { ?item <http://rdf.ebi.ac.uk/terms/chembl#pChembl> ?pChembl . } " +
			" OPTIONAL { ?item <http://rdf.ebi.ac.uk/terms/chembl#activityComment> ?act_comment . } " +
			" OPTIONAL { ?assay_uri <http://purl.org/dc/terms/description> ?assay_description. } " +
			" OPTIONAL { ?rsc_compound_uri <http://www.openphacts.org/api#smiles> ?smiles . } " +
			"}";
	
	public IStringMatrix pharma4Target() throws ClientProtocolException, IOException, HttpException {
		Targets client = Targets.getInstance("https://dev.openphacts.org/",
				"02a13118", "0d8d74d0559e2d9dd2efb1694805f69a");
		String turtle = client
				.pharmacologyCount("http://www.conceptwiki.org/concept/00059958-a045-4581-9dc5-e5a08bb0c291");
		
		IStringMatrix matrix = sparql(turtle, COMPOUND_PHARMA_BY_TARGET);
		System.out.println("matrix: " + matrix);
		return matrix;
	}


	 
	public StringMatrix sparql(String turtle, String queryString) throws IOException {
        StringMatrix table = null;
        Model model = ModelFactory.createOntologyModel();
        Query query = QueryFactory.create(queryString);
        PrefixMapping prefixMap = query.getPrefixMapping();
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            table = convertIntoTable(prefixMap, results);
        } finally {
            qexec.close();
        }
        return table;
    }

    private StringMatrix convertIntoTable(
            PrefixMapping prefixMap, ResultSet results) {
    	StringMatrix table = new StringMatrix();
    	int rowCount = 0;
    	int colCount = 0;
        while (results.hasNext()) {
        	colCount = 0;
        	rowCount++;
            QuerySolution soln = results.nextSolution();
            Iterator<String> varNames = soln.varNames();
            while (varNames.hasNext()) {
            	colCount++;
            	String varName = varNames.next();
            	table.setColumnName(colCount, varName);
                RDFNode node = soln.get(varName);
                if (node != null) {
                    String nodeStr = node.toString();
                    if (node.isResource()) {
                        Resource resource = (Resource)node;
                        // the resource.getLocalName() is not accurate, so I
                        // use some custom code
                        String[] uriLocalSplit = split(prefixMap, resource);
                        if (uriLocalSplit[0] == null) {
                        	table.set(rowCount, colCount, resource.getURI());
                        } else {
                        	table.set(rowCount, colCount,
                                uriLocalSplit[0] + ":" + uriLocalSplit[1]
                            );
                        }
                    } else {
                    	table.set(rowCount, colCount, nodeStr);
                    }
                }
            }
        }
        return table;
    }
	
    /**
     * Helper method that splits up a URI into a namespace and a local part.
     * It uses the prefixMap to recognize namespaces, and replaces the
     * namespace part by a prefix.
     *
     * @param prefixMap
     * @param resource
     */
    public static String[] split(PrefixMapping prefixMap, Resource resource) {
        String uri = resource.getURI();
        if (uri == null) {
            return new String[] {null, null};
        }
        Map<String,String> prefixMapMap = prefixMap.getNsPrefixMap();
        Set<String> prefixes = prefixMapMap.keySet();
        String[] split = { null, null };
        for (String key : prefixes){
            String ns = prefixMapMap.get(key);
            if (uri.startsWith(ns)) {
                split[0] = key;
                split[1] = uri.substring(ns.length());
                return split;
            }
        }
        split[1] = uri;
        return split;
    }

}
