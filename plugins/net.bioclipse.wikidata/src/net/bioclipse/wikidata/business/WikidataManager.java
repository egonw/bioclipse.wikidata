/*******************************************************************************
 * Copyright (c) 2016  Egon Willighagen <egon.willighagen@gmail.com>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contact: http://www.bioclipse.net/
 ******************************************************************************/
package net.bioclipse.wikidata.business;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IMolecule;
import net.bioclipse.core.domain.IStringMatrix;
import net.bioclipse.inchi.InChI;
import net.bioclipse.managers.business.IBioclipseManager;
import net.bioclipse.rdf.Activator;
import net.bioclipse.rdf.business.IRDFManager;
import net.bioclipse.wikidata.domain.WikidataMolecule;

public class WikidataManager implements IBioclipseManager {

    private final IRDFManager rdf = Activator.getDefault().getJavaManager();
    
    /**
     * Gives a short one word name of the manager used as variable name when
     * scripting.
     */
    public String getManagerName() {
        return "wikidata";
    }

    
    public boolean hasMolecule(InChI inchi, IProgressMonitor monitor) throws BioclipseException {
    	if (inchi == null) throw new BioclipseException("You must give an InChI.");
    	if (monitor == null) monitor = new NullProgressMonitor();
    	String hasMoleculeByInChI =
    		"PREFIX wdt: <http://www.wikidata.org/prop/direct/>"
    	   	+ "SELECT ?compound WHERE {"
    	    + "  ?compound wdt:P235 \"" + inchi.getKey() + "\" ."
    	    + "}";
    	IStringMatrix results = rdf.sparqlRemote(
    		"https://query.wikidata.org/sparql", hasMoleculeByInChI
    	);
    	return (results.getRowCount() > 0);
    };

    public String getEntityID(InChI inchi, IProgressMonitor monitor) throws BioclipseException {
    	if (inchi == null) throw new BioclipseException("You must give an InChI.");
    	if (monitor == null) monitor = new NullProgressMonitor();
    	String hasMoleculeByInChI =
        	"PREFIX wdt: <http://www.wikidata.org/prop/direct/>"
        	+ "SELECT ?compound WHERE {"
        	+ "  ?compound wdt:P235  \"" + inchi.getKey() + "\" ."
        	+ "}";
    	IStringMatrix results = rdf.sparqlRemote(
    		"https://query.wikidata.org/sparql", hasMoleculeByInChI
        );
    	if (results.getRowCount() == 0)
    		throw new BioclipseException("No molecule in Wikidata with the InChI: " + inchi);
    	if (results.getRowCount() > 1)
    		throw new BioclipseException("Too many molecules in Wikidata with the InChI: " + inchi);
    	String entityID = results.get(1, "compound");
    	if (entityID == null || entityID.length() == 0)
    		throw new BioclipseException("No Wikidata entity found for the molecule with the InChI: " + inchi);
    	return entityID;
    }

    public IMolecule getMolecule(InChI inchi, IProgressMonitor monitor) throws BioclipseException {
    	if (inchi == null) throw new BioclipseException("You must give an InChI.");
    	if (monitor == null) monitor = new NullProgressMonitor();
    	return new WikidataMolecule(getEntityID(inchi, monitor));
    };
}
