/* Copyright (c) 2016  Egon Willighagen <egonw@user.sf.net>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package net.bioclipse.wikidata.domain;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IResource;

import net.bioclipse.cdk.business.Activator;
import net.bioclipse.cdk.domain.ICDKMolecule;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.BioObject;
import net.bioclipse.core.domain.IMolecule;
import net.bioclipse.core.domain.IStringMatrix;

public class WikidataMolecule extends BioObject implements IWikidataMolecule {

	private ICDKMolecule cdkMol;
	private String entityID;
	private String SMILES;
	
	/*
     * Needed by Spring
     */
	WikidataMolecule() {
        super();
    }

	public WikidataMolecule(String identifier) {
		this.entityID = identifier;
	}
	
	@Override
	public List<IMolecule> getConformers() {
		return Collections.emptyList();
	}

	@Override
	public String toSMILES() throws BioclipseException {
		if (this.SMILES != null) return this.SMILES;
		String hasMoleculeByInChI =
	        "PREFIX wdt: <http://www.wikidata.org/prop/direct/>"
	        + "SELECT ?smiles WHERE {"
	        + "  <" + this.entityID + "> wdt:P233 ?smiles . "
	        + "}";
	    IStringMatrix results = net.bioclipse.rdf.Activator.getDefault().getJavaManager().sparqlRemote(
	    	"https://query.wikidata.org/sparql", hasMoleculeByInChI
	    );
	    if (results.getRowCount() == 0) {
	    	this.SMILES = null;
	    	return "";
	    }
	    String smiles = results.get(1, "smiles");
	    if (smiles == null || smiles.length() == 0) {
	    	this.SMILES = null;
	    	return "";
	    }
	    this.SMILES = smiles;
	    return smiles;
	}

	@Override
	public String toCML() throws BioclipseException {
		return asCDKMolecule().toCML();
	}

	@Override
	public IResource getResource() {
		// TODO Auto-generated method stub
		return null;
	}

	public ICDKMolecule asCDKMolecule() throws BioclipseException {
		if (cdkMol == null) {
			cdkMol = Activator.getDefault().getJavaCDKManager().fromSMILES(this.toSMILES());
		}
		return cdkMol;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == ICDKMolecule.class){
			try {
				return asCDKMolecule();
			} catch (BioclipseException e) {
				// could not create a CDK molecule
			}
        }
		if (adapter == IWikidataMolecule.class){
			return this;
		}

		return super.getAdapter(adapter);
	}

	@Override
	public String getId() {
		return this.entityID;
	}

	
}
