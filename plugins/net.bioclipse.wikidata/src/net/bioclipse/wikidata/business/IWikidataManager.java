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

import net.bioclipse.core.PublishedClass;
import net.bioclipse.core.PublishedMethod;
import net.bioclipse.core.Recorded;
import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.core.domain.IMolecule;
import net.bioclipse.inchi.InChI;
import net.bioclipse.managers.business.IBioclipseManager;

@PublishedClass(
    value="Manager for interacting with the Wikidata database (mostly via the SPARQL end point)."
)
public interface IWikidataManager extends IBioclipseManager {

    @Recorded
    @PublishedMethod(
        methodSummary="Return true if Wikidata contains a molecule with the given InChI.",
        params="InChI inchi"
    )
    public boolean hasMolecule(InChI inchi) throws BioclipseException;

    @Recorded
    @PublishedMethod(
        methodSummary="Return true the Wikidata entity ID for the molecule with the given InChI.",
        params="InChI inchi"
    )
    public String getEntityID(InChI inchi) throws BioclipseException;

    @Recorded
    @PublishedMethod(
        methodSummary="Return a molecule with the given InChI, or throws an BioclipseException"
        		+ " when it does not exist.",
        params="InChI inchi"
    )
    public IMolecule getMolecule(InChI inchi) throws BioclipseException;
}
