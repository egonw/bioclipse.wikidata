/*******************************************************************************
 * Copyright (c) 2015  Egon Willighagen <egon.willighagen@gmail.com>
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
import net.bioclipse.managers.business.IBioclipseManager;

@PublishedClass(
    value="Manager that add support for interaction with Wikidata."
)
public interface IWikidataManager extends IBioclipseManager {

	@Recorded
    @PublishedMethod(
    	params="String downloadFolder",
        methodSummary="Download a Wikidata JSON dump into the given folder."
    )
    public String downloadJSON(String downloadFolder) throws BioclipseException;
	
}
