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

import net.bioclipse.core.business.BioclipseException;
import net.bioclipse.managers.business.IBioclipseManager;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocumentProcessor;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.dumpfiles.DumpProcessingController;

public class WikidataManager implements IBioclipseManager {

    private static final Logger logger = Logger.getLogger(WikidataManager.class);
    
    /**
     * Gives a short one word name of the manager used as variable name when
     * scripting.
     */
    public String getManagerName() {
        return "wdtk";
    }

    public String downloadJSON(String downloadFolder, IProgressMonitor monitor) throws BioclipseException {
    	if (monitor == null) monitor = new NullProgressMonitor();
    	final IProgressMonitor finalMonitor = monitor;
    	finalMonitor.beginTask("Processing the JSON dump", 1000000);

		logger.debug("Downloading into: " + downloadFolder);

    	DumpProcessingController dumpProcessingController = new DumpProcessingController("wikidatawiki");
    	dumpProcessingController.setOfflineMode(true);
    	// create a download folder
    	// TODO: make customizable
    	try {
			dumpProcessingController.setDownloadDirectory(downloadFolder);
		} catch (Exception exception) {
			throw new BioclipseException(
				"Error setting the Wikidata download directory: " + exception.getMessage(), exception
			);
		}
    	dumpProcessingController.registerEntityDocumentProcessor(
    		new EntityDocumentProcessor() {
				
				@Override
				public void processPropertyDocument(PropertyDocument arg0) {
					System.out.println(arg0.toString());
					finalMonitor.worked(1);
				}
				
				@Override
				public void processItemDocument(ItemDocument arg0) {
					System.out.println(arg0.toString());
					finalMonitor.worked(1);
				}
			}, null, true);
		dumpProcessingController.processMostRecentJsonDump();
		monitor.worked(1);
    	return "";
    }

}
