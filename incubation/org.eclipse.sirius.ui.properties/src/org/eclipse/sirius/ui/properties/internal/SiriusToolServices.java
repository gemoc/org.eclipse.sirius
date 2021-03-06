/*******************************************************************************
 * Copyright (c) 2015, 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.ui.properties.internal;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.sirius.business.api.helper.task.ICommandTask;
import org.eclipse.sirius.business.api.helper.task.TaskHelper;
import org.eclipse.sirius.business.api.query.EObjectQuery;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.ecore.extender.business.api.accessor.ModelAccessor;
import org.eclipse.sirius.ext.base.Option;
import org.eclipse.sirius.properties.ViewExtensionDescription;
import org.eclipse.sirius.tools.api.command.SiriusCommand;
import org.eclipse.sirius.tools.api.command.ui.NoUICallback;
import org.eclipse.sirius.ui.properties.internal.tabprovider.SiriusTabDescriptorProvider;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;
import org.eclipse.sirius.viewpoint.description.tool.ModelOperation;

/**
 * This class contains various services provided by the Sirius UI Properties
 * bundle to the interpreter.
 * 
 * @author sbegaudeau
 */
public class SiriusToolServices {

    // TODO To be removed in favor of the standard EMF Edit
    /**
     * Returns the image representing the given EObject.
     * 
     * @param eObject
     *            The EObject
     * @return The image representing the given EObject or <code>null</code> if
     *         none could be found
     */
    public Object image(EObject eObject) {
        ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
        Adapter adapter = composedAdapterFactory.adapt(eObject, IItemLabelProvider.class);
        if (adapter instanceof IItemLabelProvider) {
            IItemLabelProvider labelProvider = (IItemLabelProvider) adapter;
            return labelProvider.getImage(eObject);
        }
        return null;
    }

    /**
     * Executes the operation with the given URI.
     * 
     * @param eObject
     *            The current EObject
     * @param initialCommandUri
     *            the URI of the operation to execute
     * @return the model element on which the tool was executed.
     */
    public EObject executeOperation(EObject eObject, String initialCommandUri) {
        Session session = new EObjectQuery(eObject).getSession();
        ModelOperation modelOperation = findModelOperation(initialCommandUri, session);
        if (modelOperation != null) {
            ModelAccessor modelAccessor = session.getModelAccessor();
            ICommandTask task = new TaskHelper(modelAccessor, new NoUICallback()).buildTaskFromModelOperation(eObject, modelOperation);
            SiriusCommand command = new SiriusCommand(session.getTransactionalEditingDomain(), "SiriusToolServices#executeOperation");
            command.getTasks().add(task);
            session.getTransactionalEditingDomain().getCommandStack().execute(command);
        }
        return eObject;
    }

    /**
     * Resolves the actual {@link ModelOperation} to execute given its URI.
     * 
     * @param initialCommandUri
     *            the URI of the operation to search for.
     * @param session
     *            the Sirius session which determines the scope to search into.
     * @return the {@link ModelOperation} instance found at the specified URI,
     *         either in one of the VSMs for which at least one Viewpoint is
     *         currently enabled in the session, or from the default ruleset, or
     *         <code>null</code> if no matching operation could be located.
     */
    private ModelOperation findModelOperation(String initialCommandUri, Session session) {
        URI commandResourceURI = URI.createURI(initialCommandUri).trimFragment();
        for (Resource res : getResourcesInScope(session)) {
            if (commandResourceURI.equals(res.getURI())) {
                EObject modelOperationEObject = res.getEObject(URI.createURI(initialCommandUri).fragment());
                if (modelOperationEObject instanceof InitialOperation) {
                    return ((InitialOperation) modelOperationEObject).getFirstModelOperations();
                }
            }
        }
        return null;
    }

    /**
     * Returns all the (VSM-like) resources in which to search for the
     * {@link ModelOperation} to execute.
     * 
     * @param session
     *            the Sirius session.
     * @return all the resources in which to look for the ModelOperation, in
     *         order of preference.
     */
    private Set<Resource> getResourcesInScope(Session session) {
        Set<Resource> result = new LinkedHashSet<>();
        Collection<Viewpoint> selectedViewpoints = session.getSelectedViewpoints(true);
        for (Viewpoint viewpoint : selectedViewpoints) {
            Resource eResource = viewpoint.eResource();
            if (eResource != null) {
                result.add(eResource);
            }
        }
        ViewExtensionDescription defaults = SiriusTabDescriptorProvider.getDefaultRules();
        if (defaults != null && defaults.eResource() != null) {
            result.add(defaults.eResource());
        }
        return result;
    }

    /**
     * Returns the value of the given structural feature for the given object.
     * 
     * @param eObject
     *            The EObject
     * @param eStructuralFeature
     *            The EStructuralFeature
     * @return The value
     */
    public Object eGet(EObject eObject, EStructuralFeature eStructuralFeature) {
        return eObject.eGet(eStructuralFeature);
    }

    /**
     * Sets the value of the given structural feature for the given object.
     * 
     * @param eObject
     *            The EObject
     * @param eStructuralFeature
     *            The EStructuralFeature
     * @param value
     *            The new value
     * @return the model element on which the service was executed.
     */
    public EObject eSet(EObject eObject, EStructuralFeature eStructuralFeature, Object value) {
        eObject.eSet(eStructuralFeature, value);
        return eObject;
    }

    /**
     * Returns the {@link SiriusContext} associated with an arbitrary model
     * element.
     * 
     * @param self
     *            an arbitrary model element.
     * @return the element's {@link SiriusContext}.
     */
    public SiriusContext context(EObject self) {
        return SiriusContext.from(self);
    }

    /**
     * Returns the {@link SiriusContext} associated with a
     * {@link SiriusInputDescriptor} (typically the "input" variable of the
     * properties view).
     * 
     * @param sid
     *            a {@link SiriusInputDescriptor} (typically the "input"
     *            variable of the properties view).
     * @return the input's full context.
     */
    public SiriusContext context(SiriusInputDescriptor sid) {
        return sid.getFullContext();
    }

    /**
     * Returns the Sirius session associated to a given context.
     * 
     * @param ctx
     *            a Sirius context.
     * 
     * @return the Sirius session associated to a given context.
     */
    public Session session(SiriusContext ctx) {
        Option<Session> s = ctx.getSession();
        if (s.some()) {
            return s.get();
        } else {
            return null;
        }
    }

    /**
     * Returns the Sirius representation associated to a given context.
     * 
     * @param ctx
     *            a Sirius context.
     * 
     * @return the Sirius representation associated to a given context.
     */
    public DRepresentation representation(SiriusContext ctx) {
        Option<DRepresentation> r = ctx.getDRepresentation();
        if (r.some()) {
            return r.get();
        } else {
            return null;
        }
    }

    /**
     * Returns the Sirius {@link DSemanticDecorator} associated to a given
     * context.
     * 
     * @param ctx
     *            a Sirius context.
     * 
     * @return the Sirius {@link DSemanticDecorator} associated to a given
     *         context.
     */
    public DSemanticDecorator semanticDecorator(SiriusContext ctx) {
        return ctx.getSemanticDecorator();
    }

    /**
     * Returns the main semantic element associated to a given context.
     * 
     * @param ctx
     *            a Sirius context.
     * 
     * @return the main semantic element associated to a given context.
     */
    public EObject mainSemanticElement(SiriusContext ctx) {
        Option<EObject> target = ctx.getMainSemanticElement();
        if (target.some()) {
            return target.get();
        } else {
            return null;
        }
    }

    /**
     * Returns all the semantic elements associated to a given context.
     * 
     * @param ctx
     *            a Sirius context.
     * 
     * @return all the semantic elements associated to a given context.
     */
    public Collection<EObject> allSemanticElements(SiriusContext ctx) {
        Option<Collection<EObject>> elements = ctx.getAdditionalSemanticElements();
        if (elements.some()) {
            return elements.get();
        } else {
            return null;
        }
    }
}
