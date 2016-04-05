/**
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *
 */
package org.eclipse.sirius.properties.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.sirius.properties.ButtonDescription;
import org.eclipse.sirius.properties.ButtonWidgetConditionalStyle;
import org.eclipse.sirius.properties.ButtonWidgetStyle;
import org.eclipse.sirius.properties.CheckboxDescription;
import org.eclipse.sirius.properties.CheckboxWidgetConditionalStyle;
import org.eclipse.sirius.properties.CheckboxWidgetStyle;
import org.eclipse.sirius.properties.ContainerDescription;
import org.eclipse.sirius.properties.CustomDescription;
import org.eclipse.sirius.properties.CustomExpression;
import org.eclipse.sirius.properties.CustomOperation;
import org.eclipse.sirius.properties.CustomWidgetConditionalStyle;
import org.eclipse.sirius.properties.CustomWidgetStyle;
import org.eclipse.sirius.properties.DynamicMappingFor;
import org.eclipse.sirius.properties.DynamicMappingIf;
import org.eclipse.sirius.properties.FILL_LAYOUT_ORIENTATION;
import org.eclipse.sirius.properties.FillLayoutDescription;
import org.eclipse.sirius.properties.GridLayoutDescription;
import org.eclipse.sirius.properties.GroupDescription;
import org.eclipse.sirius.properties.GroupValidationSetDescription;
import org.eclipse.sirius.properties.LabelDescription;
import org.eclipse.sirius.properties.LabelWidgetConditionalStyle;
import org.eclipse.sirius.properties.LabelWidgetStyle;
import org.eclipse.sirius.properties.MultipleReferencesDescription;
import org.eclipse.sirius.properties.OperationDescription;
import org.eclipse.sirius.properties.PageDescription;
import org.eclipse.sirius.properties.PageValidationSetDescription;
import org.eclipse.sirius.properties.PropertiesFactory;
import org.eclipse.sirius.properties.PropertiesPackage;
import org.eclipse.sirius.properties.PropertyValidationRule;
import org.eclipse.sirius.properties.RadioDescription;
import org.eclipse.sirius.properties.RadioWidgetConditionalStyle;
import org.eclipse.sirius.properties.RadioWidgetStyle;
import org.eclipse.sirius.properties.SelectDescription;
import org.eclipse.sirius.properties.SelectWidgetConditionalStyle;
import org.eclipse.sirius.properties.SelectWidgetStyle;
import org.eclipse.sirius.properties.SingleReferenceDescription;
import org.eclipse.sirius.properties.TextAreaDescription;
import org.eclipse.sirius.properties.TextDescription;
import org.eclipse.sirius.properties.TextWidgetConditionalStyle;
import org.eclipse.sirius.properties.TextWidgetStyle;
import org.eclipse.sirius.properties.ViewExtensionDescription;
import org.eclipse.sirius.properties.WidgetStyle;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!--
 * end-user-doc -->
 *
 * @generated
 */
public class PropertiesFactoryImpl extends EFactoryImpl implements PropertiesFactory {
    /**
     * Creates the default factory implementation. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    public static PropertiesFactory init() {
        try {
            PropertiesFactory thePropertiesFactory = (PropertiesFactory) EPackage.Registry.INSTANCE.getEFactory(PropertiesPackage.eNS_URI);
            if (thePropertiesFactory != null) {
                return thePropertiesFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new PropertiesFactoryImpl();
    }

    /**
     * Creates an instance of the factory. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    public PropertiesFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
        case PropertiesPackage.VIEW_EXTENSION_DESCRIPTION:
            return createViewExtensionDescription();
        case PropertiesPackage.PAGE_DESCRIPTION:
            return createPageDescription();
        case PropertiesPackage.PAGE_VALIDATION_SET_DESCRIPTION:
            return createPageValidationSetDescription();
        case PropertiesPackage.PROPERTY_VALIDATION_RULE:
            return createPropertyValidationRule();
        case PropertiesPackage.GROUP_DESCRIPTION:
            return createGroupDescription();
        case PropertiesPackage.GROUP_VALIDATION_SET_DESCRIPTION:
            return createGroupValidationSetDescription();
        case PropertiesPackage.CONTAINER_DESCRIPTION:
            return createContainerDescription();
        case PropertiesPackage.FILL_LAYOUT_DESCRIPTION:
            return createFillLayoutDescription();
        case PropertiesPackage.GRID_LAYOUT_DESCRIPTION:
            return createGridLayoutDescription();
        case PropertiesPackage.TEXT_DESCRIPTION:
            return createTextDescription();
        case PropertiesPackage.BUTTON_DESCRIPTION:
            return createButtonDescription();
        case PropertiesPackage.LABEL_DESCRIPTION:
            return createLabelDescription();
        case PropertiesPackage.CHECKBOX_DESCRIPTION:
            return createCheckboxDescription();
        case PropertiesPackage.SELECT_DESCRIPTION:
            return createSelectDescription();
        case PropertiesPackage.DYNAMIC_MAPPING_FOR:
            return createDynamicMappingFor();
        case PropertiesPackage.DYNAMIC_MAPPING_IF:
            return createDynamicMappingIf();
        case PropertiesPackage.TEXT_AREA_DESCRIPTION:
            return createTextAreaDescription();
        case PropertiesPackage.RADIO_DESCRIPTION:
            return createRadioDescription();
        case PropertiesPackage.SINGLE_REFERENCE_DESCRIPTION:
            return createSingleReferenceDescription();
        case PropertiesPackage.OPERATION_DESCRIPTION:
            return createOperationDescription();
        case PropertiesPackage.MULTIPLE_REFERENCES_DESCRIPTION:
            return createMultipleReferencesDescription();
        case PropertiesPackage.CUSTOM_DESCRIPTION:
            return createCustomDescription();
        case PropertiesPackage.CUSTOM_EXPRESSION:
            return createCustomExpression();
        case PropertiesPackage.CUSTOM_OPERATION:
            return createCustomOperation();
        case PropertiesPackage.WIDGET_STYLE:
            return createWidgetStyle();
        case PropertiesPackage.TEXT_WIDGET_STYLE:
            return createTextWidgetStyle();
        case PropertiesPackage.LABEL_WIDGET_STYLE:
            return createLabelWidgetStyle();
        case PropertiesPackage.CHECKBOX_WIDGET_STYLE:
            return createCheckboxWidgetStyle();
        case PropertiesPackage.RADIO_WIDGET_STYLE:
            return createRadioWidgetStyle();
        case PropertiesPackage.BUTTON_WIDGET_STYLE:
            return createButtonWidgetStyle();
        case PropertiesPackage.SELECT_WIDGET_STYLE:
            return createSelectWidgetStyle();
        case PropertiesPackage.CUSTOM_WIDGET_STYLE:
            return createCustomWidgetStyle();
        case PropertiesPackage.TEXT_WIDGET_CONDITIONAL_STYLE:
            return createTextWidgetConditionalStyle();
        case PropertiesPackage.LABEL_WIDGET_CONDITIONAL_STYLE:
            return createLabelWidgetConditionalStyle();
        case PropertiesPackage.CHECKBOX_WIDGET_CONDITIONAL_STYLE:
            return createCheckboxWidgetConditionalStyle();
        case PropertiesPackage.RADIO_WIDGET_CONDITIONAL_STYLE:
            return createRadioWidgetConditionalStyle();
        case PropertiesPackage.BUTTON_WIDGET_CONDITIONAL_STYLE:
            return createButtonWidgetConditionalStyle();
        case PropertiesPackage.SELECT_WIDGET_CONDITIONAL_STYLE:
            return createSelectWidgetConditionalStyle();
        case PropertiesPackage.CUSTOM_WIDGET_CONDITIONAL_STYLE:
            return createCustomWidgetConditionalStyle();
        default:
            throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
        case PropertiesPackage.FILL_LAYOUT_ORIENTATION:
            return createFILL_LAYOUT_ORIENTATIONFromString(eDataType, initialValue);
        default:
            throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
        case PropertiesPackage.FILL_LAYOUT_ORIENTATION:
            return convertFILL_LAYOUT_ORIENTATIONToString(eDataType, instanceValue);
        default:
            throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ViewExtensionDescription createViewExtensionDescription() {
        ViewExtensionDescriptionImpl viewExtensionDescription = new ViewExtensionDescriptionImpl();
        return viewExtensionDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PageDescription createPageDescription() {
        PageDescriptionImpl pageDescription = new PageDescriptionImpl();
        return pageDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PageValidationSetDescription createPageValidationSetDescription() {
        PageValidationSetDescriptionImpl pageValidationSetDescription = new PageValidationSetDescriptionImpl();
        return pageValidationSetDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PropertyValidationRule createPropertyValidationRule() {
        PropertyValidationRuleImpl propertyValidationRule = new PropertyValidationRuleImpl();
        return propertyValidationRule;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public GroupDescription createGroupDescription() {
        GroupDescriptionImpl groupDescription = new GroupDescriptionImpl();
        return groupDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public GroupValidationSetDescription createGroupValidationSetDescription() {
        GroupValidationSetDescriptionImpl groupValidationSetDescription = new GroupValidationSetDescriptionImpl();
        return groupValidationSetDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ContainerDescription createContainerDescription() {
        ContainerDescriptionImpl containerDescription = new ContainerDescriptionImpl();
        return containerDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FillLayoutDescription createFillLayoutDescription() {
        FillLayoutDescriptionImpl fillLayoutDescription = new FillLayoutDescriptionImpl();
        return fillLayoutDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public GridLayoutDescription createGridLayoutDescription() {
        GridLayoutDescriptionImpl gridLayoutDescription = new GridLayoutDescriptionImpl();
        return gridLayoutDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TextDescription createTextDescription() {
        TextDescriptionImpl textDescription = new TextDescriptionImpl();
        return textDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ButtonDescription createButtonDescription() {
        ButtonDescriptionImpl buttonDescription = new ButtonDescriptionImpl();
        return buttonDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public LabelDescription createLabelDescription() {
        LabelDescriptionImpl labelDescription = new LabelDescriptionImpl();
        return labelDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CheckboxDescription createCheckboxDescription() {
        CheckboxDescriptionImpl checkboxDescription = new CheckboxDescriptionImpl();
        return checkboxDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SelectDescription createSelectDescription() {
        SelectDescriptionImpl selectDescription = new SelectDescriptionImpl();
        return selectDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DynamicMappingFor createDynamicMappingFor() {
        DynamicMappingForImpl dynamicMappingFor = new DynamicMappingForImpl();
        return dynamicMappingFor;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DynamicMappingIf createDynamicMappingIf() {
        DynamicMappingIfImpl dynamicMappingIf = new DynamicMappingIfImpl();
        return dynamicMappingIf;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TextAreaDescription createTextAreaDescription() {
        TextAreaDescriptionImpl textAreaDescription = new TextAreaDescriptionImpl();
        return textAreaDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public RadioDescription createRadioDescription() {
        RadioDescriptionImpl radioDescription = new RadioDescriptionImpl();
        return radioDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SingleReferenceDescription createSingleReferenceDescription() {
        SingleReferenceDescriptionImpl singleReferenceDescription = new SingleReferenceDescriptionImpl();
        return singleReferenceDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public OperationDescription createOperationDescription() {
        OperationDescriptionImpl operationDescription = new OperationDescriptionImpl();
        return operationDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public MultipleReferencesDescription createMultipleReferencesDescription() {
        MultipleReferencesDescriptionImpl multipleReferencesDescription = new MultipleReferencesDescriptionImpl();
        return multipleReferencesDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CustomDescription createCustomDescription() {
        CustomDescriptionImpl customDescription = new CustomDescriptionImpl();
        return customDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CustomExpression createCustomExpression() {
        CustomExpressionImpl customExpression = new CustomExpressionImpl();
        return customExpression;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CustomOperation createCustomOperation() {
        CustomOperationImpl customOperation = new CustomOperationImpl();
        return customOperation;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public WidgetStyle createWidgetStyle() {
        WidgetStyleImpl widgetStyle = new WidgetStyleImpl();
        return widgetStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TextWidgetStyle createTextWidgetStyle() {
        TextWidgetStyleImpl textWidgetStyle = new TextWidgetStyleImpl();
        return textWidgetStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public LabelWidgetStyle createLabelWidgetStyle() {
        LabelWidgetStyleImpl labelWidgetStyle = new LabelWidgetStyleImpl();
        return labelWidgetStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CheckboxWidgetStyle createCheckboxWidgetStyle() {
        CheckboxWidgetStyleImpl checkboxWidgetStyle = new CheckboxWidgetStyleImpl();
        return checkboxWidgetStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public RadioWidgetStyle createRadioWidgetStyle() {
        RadioWidgetStyleImpl radioWidgetStyle = new RadioWidgetStyleImpl();
        return radioWidgetStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ButtonWidgetStyle createButtonWidgetStyle() {
        ButtonWidgetStyleImpl buttonWidgetStyle = new ButtonWidgetStyleImpl();
        return buttonWidgetStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SelectWidgetStyle createSelectWidgetStyle() {
        SelectWidgetStyleImpl selectWidgetStyle = new SelectWidgetStyleImpl();
        return selectWidgetStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CustomWidgetStyle createCustomWidgetStyle() {
        CustomWidgetStyleImpl customWidgetStyle = new CustomWidgetStyleImpl();
        return customWidgetStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TextWidgetConditionalStyle createTextWidgetConditionalStyle() {
        TextWidgetConditionalStyleImpl textWidgetConditionalStyle = new TextWidgetConditionalStyleImpl();
        return textWidgetConditionalStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public LabelWidgetConditionalStyle createLabelWidgetConditionalStyle() {
        LabelWidgetConditionalStyleImpl labelWidgetConditionalStyle = new LabelWidgetConditionalStyleImpl();
        return labelWidgetConditionalStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CheckboxWidgetConditionalStyle createCheckboxWidgetConditionalStyle() {
        CheckboxWidgetConditionalStyleImpl checkboxWidgetConditionalStyle = new CheckboxWidgetConditionalStyleImpl();
        return checkboxWidgetConditionalStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public RadioWidgetConditionalStyle createRadioWidgetConditionalStyle() {
        RadioWidgetConditionalStyleImpl radioWidgetConditionalStyle = new RadioWidgetConditionalStyleImpl();
        return radioWidgetConditionalStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ButtonWidgetConditionalStyle createButtonWidgetConditionalStyle() {
        ButtonWidgetConditionalStyleImpl buttonWidgetConditionalStyle = new ButtonWidgetConditionalStyleImpl();
        return buttonWidgetConditionalStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SelectWidgetConditionalStyle createSelectWidgetConditionalStyle() {
        SelectWidgetConditionalStyleImpl selectWidgetConditionalStyle = new SelectWidgetConditionalStyleImpl();
        return selectWidgetConditionalStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CustomWidgetConditionalStyle createCustomWidgetConditionalStyle() {
        CustomWidgetConditionalStyleImpl customWidgetConditionalStyle = new CustomWidgetConditionalStyleImpl();
        return customWidgetConditionalStyle;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public FILL_LAYOUT_ORIENTATION createFILL_LAYOUT_ORIENTATIONFromString(EDataType eDataType, String initialValue) {
        FILL_LAYOUT_ORIENTATION result = FILL_LAYOUT_ORIENTATION.get(initialValue);
        if (result == null) {
            throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        }
        return result;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public String convertFILL_LAYOUT_ORIENTATIONToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PropertiesPackage getPropertiesPackage() {
        return (PropertiesPackage) getEPackage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @deprecated
     * @generated
     */
    @Deprecated
    public static PropertiesPackage getPackage() {
        return PropertiesPackage.eINSTANCE;
    }

} // PropertiesFactoryImpl
