/*******************************************************************************
 * Copyright (c) 2009, 2011 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.business.api.dialect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.business.api.dialect.identifier.RepresentationElementIdentifier;
import org.eclipse.sirius.business.api.helper.SiriusUtil;
import org.eclipse.sirius.business.api.query.ViewpointQuery;
import org.eclipse.sirius.business.api.session.CustomDataConstants;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.common.tools.api.interpreter.EvaluationException;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreter;
import org.eclipse.sirius.common.tools.api.util.StringUtil;
import org.eclipse.sirius.ecore.extender.business.api.accessor.ModelAccessor;
import org.eclipse.sirius.tools.api.interpreter.InterpreterUtil;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.description.RepresentationDescription;
import org.eclipse.sirius.viewpoint.description.Viewpoint;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Basic class to extend for each dialect services implementation.
 * 
 * @author mchauvin
 * @since 0.9.0
 */
public abstract class AbstractRepresentationDialectServices implements DialectServices {

    /**
     * Checks whether a specific representation is supported by this dialect,
     * i.e. it can delete it, copy it, refresh it, etc.
     * 
     * @param representation
     *            the representation.
     * @return <code>true</code> if this dialect supports the representation.
     */
    protected abstract boolean isSupported(DRepresentation representation);

    /**
     * Checks whether a specific representation description is supported by this
     * dialect, i.e. it can create concrete representations from it, and support
     * the result.
     * 
     * @param description
     *            the representation description.
     * @return <code>true</code> if this dialect supports the representation
     *         description.
     */
    protected abstract boolean isSupported(RepresentationDescription description);

    /**
     * {@inheritDoc}
     */
    public void notify(RepresentationNotification notification) {
        // Empty default implementation.
    }

    /**
     * {@inheritDoc}
     */
    public boolean canCreateIdentifier(EObject representationElement) {
        // No support for identifiers by default.
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public RepresentationElementIdentifier createIdentifier(EObject representationElement, Map<EObject, RepresentationElementIdentifier> elementToIdentifier) {
        // No support for identifiers by default.
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void updateRepresentationsExtendedBy(Session session, Viewpoint viewpoint, boolean activated) {
        // No support for representation extension by default.
    }

    /**
     * {@inheritDoc}
     */
    public void refreshEffectiveRepresentationDescription(DRepresentation representation, IProgressMonitor monitor) {
        // Do nothing by default, not all dialects have effective representation
        // descriptions.
    }

    /**
     * {@inheritDoc}
     */
    public Collection<DRepresentation> getRepresentations(EObject semantic, Session session) {
        return getRepresentations(session, CustomDataConstants.DREPRESENTATION, semantic);
    }

    /**
     * {@inheritDoc}
     */
    public Collection<DRepresentation> getAllRepresentations(Session session) {
        return getRepresentations(session, CustomDataConstants.DREPRESENTATION, null);
    }

    /**
     * {@inheritDoc}
     */
    public Collection<DRepresentation> getRepresentations(RepresentationDescription representationDescription, Session session) {
        return getRepresentations(session, CustomDataConstants.DREPRESENTATION_FROM_DESCRIPTION, representationDescription);
    }

    /**
     * {@inheritDoc}
     */
    public boolean canRefresh(DRepresentation representation) {
        return isSupported(representation);
    }

    /**
     * {@inheritDoc}
     */
    public DRepresentation copyRepresentation(final DRepresentation representation, final String name, final Session session, final IProgressMonitor monitor) {
        EcoreUtil.Copier copier = new EcoreUtil.Copier();
        DRepresentation newRepresentation = (DRepresentation) copier.copy(representation);
        copier.copyReferences();

        /* Set the correct name */
        newRepresentation.setName(name);
        return newRepresentation;
    }

    /**
     * {@inheritDoc}
     */
    public boolean deleteRepresentation(DRepresentation representation, Session session) {
        if (isSupported(representation)) {
            SiriusUtil.delete(representation, session);
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public DRepresentation createRepresentation(String name, EObject semantic, RepresentationDescription description, Session session, IProgressMonitor monitor) {
        DRepresentation representation = null;
        try {
            monitor.beginTask("Create representation : " + name, 2);
            representation = createRepresentation(name, semantic, description, new SubProgressMonitor(monitor, 1));
            if (representation != null) {
                session.getServices().putCustomData(CustomDataConstants.DREPRESENTATION, semantic, representation);
            }
            monitor.worked(1);
        } finally {
            monitor.done();
        }
        return representation;
    }

    /**
     * Create a new representation using the representation description. As no
     * session is passed to this method the created representation will not be
     * kept.
     * 
     * @param name
     *            name of the representation to create.
     * 
     * @param semantic
     *            semantic root used to create the representation.
     * @param description
     *            representation description to use.
     * @param monitor
     *            to track progress.
     * @return the new representation .
     */
    protected abstract DRepresentation createRepresentation(String name, EObject semantic, RepresentationDescription description, IProgressMonitor monitor);

    /**
     * {@inheritDoc}
     */
    public Collection<RepresentationDescription> getAvailableRepresentationDescriptions(Collection<Viewpoint> vps, EObject semantic) {
        final Collection<RepresentationDescription> result = Lists.newArrayList();
        for (Viewpoint vp : vps) {
            Iterables.addAll(result, getAvailableRepresentationDescriptions(vp, semantic));
        }
        return result;
    }

    /**
     * Finds all the supported representations in a session which have the
     * specified key/value pair in their custom data.
     * 
     * @param session
     *            the session in which to look for representations.
     * @param key
     *            the key to look for in the representations' custom data.
     * @param value
     *            the value associated to the key in the representations' custom
     *            data.
     * @return all the supported representations in a session which have the
     *         specified key/value pair in their custom data.
     */
    protected Collection<DRepresentation> getRepresentations(Session session, String key, EObject value) {
        Collection<DRepresentation> reps = Lists.newArrayList();
        for (EObject representation : session.getServices().getCustomData(key, value)) {
            if (representation instanceof DRepresentation && isSupported((DRepresentation) representation)) {
                reps.add((DRepresentation) representation);
            }
        }
        return reps;
    }

    /**
     * Return all RepresentationDescription available in the specified viewpoint
     * the user might use to create a new DRepresentation.
     * 
     * @param vp
     *            the viewpoint to look into for candidate
     *            {@link RepresentationDescription}.
     * @param semantic
     *            the semantic element on which the user wants to create a
     *            representation.
     * @return the {@link RepresentationDescription}s defined in the specified
     *         viewpoint which can apply to the semantic element.
     */
    protected Iterable<RepresentationDescription> getAvailableRepresentationDescriptions(Viewpoint vp, final EObject semantic) {
        Iterable<RepresentationDescription> candidates = new ViewpointQuery(vp).getAllRepresentationDescriptions();
        return Iterables.filter(candidates, new Predicate<RepresentationDescription>() {
            public boolean apply(RepresentationDescription input) {
                return canCreate(semantic, input);
            }
        });
    }

    /**
     * Checks that the specified semantic element matches the precondition.
     * 
     * @param semantic
     *            the evaluation context
     * @param condition
     *            the precondition to evaluate, may be <code>null</code> or
     *            empty.
     * @return <code>true</code> if the semantic element matches the
     *         precondition or there is no precondition, <code>false</code>
     *         otherwise.
     */
    protected boolean checkPrecondition(EObject semantic, String condition) {
        boolean canCreate;
        IInterpreter interpreter = InterpreterUtil.getInterpreter(semantic);
        if (StringUtil.isEmpty(condition)) {
            // An empty pre-condition means we can always create a
            // representation.
            canCreate = true;
        } else {
            try {
                canCreate = interpreter.evaluateBoolean(semantic, condition);
            } catch (EvaluationException e) {
                canCreate = false;
            }
        }
        return canCreate;
    }

    /**
     * Checks that the semantic element is compatible with the specified domain
     * class.
     * 
     * @param accessor
     *            the model accessor to use.
     * @param semantic
     *            the semantic element to check.
     * @param domainClass
     *            the name (simple or qualified) of the domain class to check.
     * @return <code>true</code> if the semantic element is considered an
     *         instance of of the designated domain class by the accessor.
     */
    protected boolean checkDomainClass(ModelAccessor accessor, EObject semantic, String domainClass) {
        return !StringUtil.isEmpty(domainClass) && accessor.eInstanceOf(semantic, domainClass);
    }

    /**
     * Checks that we can create new elements inside the specified semantic
     * element.
     * <p>
     * When a representation is created we execute an initial operation if
     * specified in the VSM. This operation is typically used to initialize the
     * semantic model, so here we also make sure than "filling" the semantic
     * model is authorized.
     * 
     * @param accessor
     *            the model accessor to use for the checks.
     * @param semantic
     *            the semantic element to check.
     * @return <code>true</code> if we can create new elements inside the
     *         specified element.
     */
    protected boolean checkSemanticElementCanBeFilled(ModelAccessor accessor, EObject semantic) {
        return accessor.getPermissionAuthority().canCreateIn(semantic);
    }

    /**
     * Create a new {@link Representation} for the specified semantic element
     * and all its children for all {@link RepresentationDescription} of the
     * specified type of the specified {@link Viewpoint}.
     * 
     * @param semantic
     *            the specified semantic element
     * @param vp
     *            the specified {@link Viewpoint}
     * @param representationDescriptionType
     *            the specified {@link RepresentationDescription} type
     */
    protected void initRepresentations(EObject semantic, Viewpoint vp, Class<? extends RepresentationDescription> representationDescriptionType) {
        initRepresentations(semantic, vp, representationDescriptionType, new NullProgressMonitor());
    }

    /**
     * Create a new {@link Representation} for the specified semantic element
     * and all its children for all {@link RepresentationDescription} of the
     * specified type of the specified {@link Viewpoint}.
     * 
     * @param semantic
     *            the specified semantic element
     * @param vp
     *            the specified {@link Viewpoint}
     * @param representationDescriptionType
     *            the specified {@link RepresentationDescription} type
     * @param monitor
     *            a {@link IProgressMonitor} to show progression of
     *            representations initialization
     */
    protected void initRepresentations(EObject semantic, Viewpoint vp, Class<? extends RepresentationDescription> representationDescriptionType, IProgressMonitor monitor) {
        Collection<? extends RepresentationDescription> descriptions = collectRepresentationDescriptions(vp, representationDescriptionType);
        initRepresentations(descriptions, semantic, monitor);
    }

    /**
     * Returns all the {@link RepresentationDescription} contained in the given
     * viewpoint of the given Representation Description type (e.g.
     * DiagramDescription.class).
     * 
     * @param <T>
     *            the expected representation type (e.g. DiagramDescription)
     * @param viewpoint
     *            the viewpoint in which representation descriptions should be
     *            collected
     * @param expectedRepresentationDescriptionType
     *            the expected representation type (e.g.
     *            DiagramDescription.class)
     * @return all the {@link RepresentationDescription} contained in the given
     *         viewpoint of the given Representation Description type (e.g.
     *         DiagramDescription.class)
     */
    private <T extends RepresentationDescription> Collection<T> collectRepresentationDescriptions(final Viewpoint viewpoint, Class<T> expectedRepresentationDescriptionType) {
        final Collection<T> descriptions = new ArrayList<T>();
        for (final RepresentationDescription representationDescription : new ViewpointQuery(viewpoint).getAllRepresentationDescriptions()) {
            if (expectedRepresentationDescriptionType.isAssignableFrom(representationDescription.getClass())) {
                if (representationDescription.isInitialisation()) {
                    descriptions.add((T) representationDescription);
                }
            }
        }
        return descriptions;
    }

    /**
     * Inits all tables with the specified table descriptions.
     * 
     * @param descriptions
     *            the descriptions
     * @param rootSemanticElement
     *            the root model element.
     * @param view
     *            the view.
     */
    private void initRepresentations(final Collection<? extends RepresentationDescription> descriptions, final EObject rootSemanticElement, IProgressMonitor monitor) {
        if (descriptions.isEmpty()) {
            return;
        }
        try {
            monitor.beginTask("Representations initialization : ", descriptions.size());
            for (final RepresentationDescription desc : descriptions) {
                initRepresentationForElement(desc, rootSemanticElement, monitor);
            }
            final Iterator<EObject> iterElements = rootSemanticElement.eAllContents();
            while (iterElements.hasNext()) {
                final EObject currentSemanticElement = iterElements.next();
                for (final RepresentationDescription desc : descriptions) {
                    initRepresentationForElement(desc, currentSemanticElement, monitor);
                }
            }
        } finally {
            monitor.done();
        }
    }

    /**
     * Initialize a new {@link Representation} from a specified
     * {@link RepresentationDescription} on a specified semantic element.
     * 
     * @param representationDescription
     *            the specified {@link RepresentationDescription}
     * @param semanticElement
     *            the specified semantic element
     * @param monitor
     *            a {@link IProgressMonitor} to show progression of
     *            representations initialization
     * @param <T>
     *            the real sub type of {@link RepresentationDescription}
     */
    protected abstract <T extends RepresentationDescription> void initRepresentationForElement(final T representationDescription, final EObject semanticElement, IProgressMonitor monitor);

    /**
     * Indicates whether a representation should be initialized on the given
     * semantic element, according to the given
     * {@link RepresentationDescription}.
     * 
     * @param semanticElement
     *            the semantic element on which representations should be
     *            initialized
     * @param description
     *            the description of the candidate representation for an
     *            initialization
     * @param domainClass
     *            the expected domain class for this
     *            {@link RepresentationDescription}
     * @return true if a representation should be initialized on the given
     *         semantic element, false if :
     *         <ul>
     *         <li>the {@link RepresentationDescription}'s Domain Class does not
     *         match the given semantic element's type</li>
     *         <li>the {@link RepresentationDescription} does not require
     *         initialization (see
     *         {@link RepresentationDescription#isInitialisation()})</li>
     *         <li>a representation with the {@link RepresentationDescription}
     *         name and on the same semantic element already exists</li>
     *         </ul>
     */
    protected boolean shouldInitializeRepresentation(final EObject semanticElement, RepresentationDescription description, String domainClass) {
        boolean shouldInitializeRepresentation = !StringUtil.isEmpty(domainClass) && description.isInitialisation();
        return shouldInitializeRepresentation;
    }

}
