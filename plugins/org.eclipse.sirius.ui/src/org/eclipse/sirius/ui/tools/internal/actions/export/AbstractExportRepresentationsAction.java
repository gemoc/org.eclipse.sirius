/*******************************************************************************
 * Copyright (c) 2012 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.tools.internal.actions.export;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.sirius.business.api.query.URIQuery;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.common.tools.api.util.StringUtil;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.sirius.ui.tools.api.actions.export.ExportAction;
import org.eclipse.sirius.ui.tools.api.dialogs.AbstractExportRepresentationsAsImagesDialog;
import org.eclipse.sirius.ui.tools.api.dialogs.ExportOneRepresentationAsImageDialog;
import org.eclipse.sirius.ui.tools.api.dialogs.ExportSeveralRepresentationsAsImagesDialog;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Abstract action to export representations as images.
 * 
 * @author mporhel
 */
public abstract class AbstractExportRepresentationsAction extends Action {

    /**
     * Creates a new AbstractExportRepresentationsAction with the given text and
     * image.
     * 
     * @param text
     *            the action's text, or <code>null</code> if there is no text
     * @param image
     *            the action's image, or <code>null</code> if there is no image
     * @see #setText
     * @see #setImageDescriptor
     */
    protected AbstractExportRepresentationsAction(String text, ImageDescriptor image) {
        super(text, image);
    }

    /**
     * Collect the diagrams to export, get the corresponding sessionn compute
     * the export path and then show the path and file format dialog to the user
     * before exporting export the diagrams.
     */
    @Override
    public final void run() {
        Collection<DDiagram> collectedDiagrams = getDDiagramsToExport();
        Iterable<DDiagram> dDiagramsToExport = Iterables.filter(collectedDiagrams, Predicates.notNull());
        if (!Iterables.isEmpty(dDiagramsToExport)) {
            DDiagram firstDDiagramToExport = dDiagramsToExport.iterator().next();
            Session session = getSession(firstDDiagramToExport);
            if (session != null) {
                IPath exportPath = getExportPath(firstDDiagramToExport, session);

                if (exportPath != null) {
                    exportDiagram(exportPath, dDiagramsToExport, session);
                }
            }
        }
    }

    /**
     * Collect the diagrams to export.
     * 
     * @return the diagrams to export.
     */
    protected abstract Collection<DDiagram> getDDiagramsToExport();

    /**
     * Get the corresponding session.
     * 
     * @param diagram
     *            a seleted diagram.
     * @return the session.
     */
    protected abstract Session getSession(DDiagram diagram);

    /**
     * Display the export path and file format dialog and then export the
     * diagrams.
     * 
     * @param exportPath
     *            the default export path.
     * @param dDiagramsToExport
     *            the diagrams to export.
     * @param session
     *            the corresponding session.
     */
    protected void exportDiagram(IPath exportPath, Iterable<DDiagram> dDiagramsToExport, Session session) {
        final Shell shell = Display.getCurrent().getActiveShell();

        final AbstractExportRepresentationsAsImagesDialog dialog;
        if (Iterables.size(dDiagramsToExport) > 1) {
            dialog = new ExportSeveralRepresentationsAsImagesDialog(shell, exportPath);
        } else {
            dialog = new ExportOneRepresentationAsImageDialog(shell, exportPath, dDiagramsToExport.iterator().next().getName());
        }

        if (dialog.open() == Window.OK) {
            List<DRepresentation> toExport = Lists.<DRepresentation> newArrayList(dDiagramsToExport);
            final ExportAction exportAction = new ExportAction(session, toExport, dialog.getOutputPath(), dialog.getImageFormat(), dialog.isExportToHtml());
            final ProgressMonitorDialog pmd = new ProgressMonitorDialog(shell);
            try {
                pmd.run(false, false, exportAction);
            } catch (final InvocationTargetException e) {
                MessageDialog.openError(shell, "Error", e.getTargetException().getMessage());
            } catch (final InterruptedException e) {
                MessageDialog.openInformation(shell, "Cancelled", e.getMessage());
            } finally {
                pmd.close();
            }
        } else {
            dialog.close();
        }
    }

    /**
     * Compute the default export path.
     * 
     * @param diagram
     *            the first selected diagram.
     * @param session
     *            the corresponding session.
     * @return the export path.
     */
    protected IPath getExportPath(DDiagram diagram, Session session) {
        URI representationResourceURI = diagram.eResource().getURI();
        URIQuery uriQuery = new URIQuery(representationResourceURI);
        Option<IResource> iResourceOption = uriQuery.getCorrespondingResource();
        if (iResourceOption.some()) {
            return iResourceOption.get().getParent().getLocation();
        }

        IPath exportPath = Platform.getLocation();
        URI mainSessionResourceURI = session.getSessionResource().getURI();
        uriQuery = new URIQuery(mainSessionResourceURI);
        iResourceOption = uriQuery.getCorrespondingResource();
        if (iResourceOption.some()) {
            exportPath = iResourceOption.get().getParent().getLocation();
        } else if (uriQuery.isInMemoryURI()) {
            String opaquePart = mainSessionResourceURI.opaquePart();
            if (!StringUtil.isEmpty(opaquePart)) {
                String[] segments = opaquePart.split(Pattern.quote("/"));
                if (segments != null && segments.length > 0) {
                    String projectName = segments[0];
                    IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
                    if (project.exists()) {
                        exportPath = project.getLocation();
                    }
                }
            }
        }
        return exportPath;
    }
}
