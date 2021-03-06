/*******************************************************************************
 * Copyright (c) 2010, 2014 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.unit.diagram.layout.data;

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.layoutdata.NodeLayoutData;
import org.eclipse.sirius.diagram.ui.edit.api.part.IAbstractDiagramNodeEditPart;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramContainerEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.AbstractDNodeContainerCompartmentEditPart;
import org.eclipse.sirius.diagram.ui.tools.internal.editor.DDiagramEditorImpl;
import org.eclipse.sirius.diagram.ui.tools.internal.layout.semantic.SemanticNodeLayoutDataKey;
import org.eclipse.sirius.diagram.ui.tools.internal.layout.semantic.SiriusLayoutDataManagerForSemanticElements;
import org.eclipse.sirius.tests.SiriusTestsPlugin;
import org.eclipse.sirius.tests.support.api.SiriusDiagramTestCase;
import org.eclipse.sirius.tests.support.api.TestsUtil;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;

/**
 * Test class for {@link SiriusLayoutDataManagerForSemanticElements}.
 * 
 * @author dlecan
 */
public class SiriusLayoutDataManagerForDDiagramElementWithSameSemanticElementsTest extends SiriusDiagramTestCase {

    private static final int REF_SIZE = 25;

    private static final String WRONG_TESTS_DATA = "Wrong tests data.";

    private static final String PLUGIN_PATH = "/" + SiriusTestsPlugin.PLUGIN_ID + "/";

    private static final String DATA_PATH = "data/unit/layout/vp3089/";

    private static final String FULL_DATA_PATH = PLUGIN_PATH + DATA_PATH;

    private static final String SEMANTIC_MODEL_PATH = FULL_DATA_PATH + "vp-3089.ecore";

    private static final String MODELER_PATH = FULL_DATA_PATH + "vp-3089.odesign";

    private static final String REPRESENTATIONS_PATH = FULL_DATA_PATH + "vp-3089.aird";

    private static final String REPRESENTATION_DESC = "VP-3089";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        genericSetUp(SEMANTIC_MODEL_PATH, MODELER_PATH, REPRESENTATIONS_PATH);
    }

    /**
     * Test method.
     * 
     * @throws Exception
     *             Test error.
     */
    public void testHierarchyWithCommonSemanticTargetStoreLayout() throws Exception {
        DDiagram diagram = (DDiagram) getRepresentations(REPRESENTATION_DESC, semanticModel).toArray()[0];

        // check that all DDiagramElements have the same semantic target.
        EObject commonSemTgt = diagram.getOwnedDiagramElements().iterator().next().getTarget();
        UnmodifiableIterator<DDiagramElement> eAllDDiagramElements = Iterators.filter(diagram.eAllContents(), DDiagramElement.class);
        assertEquals(WRONG_TESTS_DATA, 4, Iterators.size(eAllDDiagramElements));
        for (DDiagramElement dde : Lists.newArrayList(eAllDDiagramElements)) {
            String message = "Wrong data for the current test : there should be on ecommon semantic target for all elements on the diagram.";
            assertEquals(message, commonSemTgt, dde.getTarget());
        }

        DDiagramEditorImpl editor = (DDiagramEditorImpl) DialectUIManager.INSTANCE.openEditor(session, diagram, new NullProgressMonitor());
        TestsUtil.synchronizationWithUIThread();

        // Check that there are 2 top level elements.
        Iterable<IAbstractDiagramNodeEditPart> firstLevelParts = getChildren(editor.getDiagramEditPart());
        assertEquals(WRONG_TESTS_DATA, 2, Iterables.size(firstLevelParts));

        // Check the SiriusLayoutDataManagerForSemanticElements's behavior
        // with them. One is Node with a border node, the other is a container
        // with a child node.
        for (IAbstractDiagramNodeEditPart idep : firstLevelParts) {
            checkLayoutManagerBehaviorWithCommonSemanticElement(idep);
        }
    }

    private void checkLayoutManagerBehaviorWithCommonSemanticElement(IAbstractDiagramNodeEditPart idep) {
        Iterable<IAbstractDiagramNodeEditPart> children = getChildren(idep);

        // Each top level element has one child.
        assertEquals(WRONG_TESTS_DATA, 1, Iterables.size(children));

        // The top level part has "big" bounds.
        Rectangle l1Bounds = idep.getFigure().getBounds().getCopy();
        assertTrue(WRONG_TESTS_DATA, l1Bounds.height > REF_SIZE);
        assertTrue(WRONG_TESTS_DATA, l1Bounds.width > REF_SIZE);

        // The second level part has "small" bounds.
        Rectangle l2Bounds = children.iterator().next().getFigure().getBounds().getCopy();
        assertTrue(WRONG_TESTS_DATA, l2Bounds.height < REF_SIZE);
        assertTrue(WRONG_TESTS_DATA, l2Bounds.width < REF_SIZE);

        // Store the layout data (copy layout) and get the stored values.
        SiriusLayoutDataManagerForSemanticElements mgr = new SiriusLayoutDataManagerForSemanticElements();
        mgr.storeLayoutData(idep);
        Map<SemanticNodeLayoutDataKey, NodeLayoutData> data = mgr.getNodeLayoutData();

        // Check the stored data
        assertEquals("There should be only one top level layout data", 1, data.size());

        Entry<SemanticNodeLayoutDataKey, NodeLayoutData> entry = data.entrySet().iterator().next();
        assertEquals("There layout data do not corresponds to the common semantic target", EcoreUtil.getURI(idep.resolveTargetSemanticElement()).fragment(), entry.getKey().getId());

        // With wrong behavior, the hierarchy was not respected, there was no
        // second level data and the recorded data was the small size.

        NodeLayoutData l1Data = entry.getValue();
        assertEquals(WRONG_TESTS_DATA, l1Bounds.height, l1Data.getHeight());
        assertEquals(WRONG_TESTS_DATA, l1Bounds.width, l1Data.getWidth());

        assertTrue("There should be layout data for the second level", l1Data.getChildren().size() == 1);
        NodeLayoutData l2Data = l1Data.getChildren().iterator().next();
        assertEquals(WRONG_TESTS_DATA, l2Bounds.height, l2Data.getHeight());
        assertEquals(WRONG_TESTS_DATA, l2Bounds.width, l2Data.getWidth());
    }

    private Iterable<IAbstractDiagramNodeEditPart> getChildren(IGraphicalEditPart part) {
        if (part instanceof IDiagramContainerEditPart) {
            return getChildren(Iterables.filter(((IDiagramContainerEditPart) part).getChildren(), AbstractDNodeContainerCompartmentEditPart.class).iterator().next());
        }
        return Iterables.filter(part.getChildren(), IAbstractDiagramNodeEditPart.class);
    }
}
