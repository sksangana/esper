/*
 ***************************************************************************************
 *  Copyright (C) 2006 EsperTech, Inc. All rights reserved.                            *
 *  http://www.espertech.com/esper                                                     *
 *  http://www.espertech.com                                                           *
 *  ---------------------------------------------------------------------------------- *
 *  The software in this package is published under the terms of the GPL license       *
 *  a copy of which has been included with this distribution in the license.txt file.  *
 ***************************************************************************************
 */
package com.espertech.esper.epl.agg.factory;

import com.espertech.esper.client.EventType;
import com.espertech.esper.codegen.base.CodegenClassScope;
import com.espertech.esper.codegen.base.CodegenMethodNode;
import com.espertech.esper.codegen.core.CodegenCtor;
import com.espertech.esper.epl.agg.access.AggregationAccessorForge;
import com.espertech.esper.epl.agg.access.AggregationAgentForge;
import com.espertech.esper.epl.agg.access.AggregationStateKey;
import com.espertech.esper.epl.agg.aggregator.AggregationMethod;
import com.espertech.esper.epl.agg.aggregator.AggregatorStddev;
import com.espertech.esper.epl.agg.aggregator.AggregatorStddevFilter;
import com.espertech.esper.codegen.base.CodegenMembersColumnized;
import com.espertech.esper.epl.agg.service.common.AggregationMethodFactory;
import com.espertech.esper.epl.agg.service.common.AggregationStateFactoryForge;
import com.espertech.esper.epl.agg.service.common.AggregationValidationUtil;
import com.espertech.esper.epl.core.engineimport.EngineImportService;
import com.espertech.esper.epl.expression.baseagg.ExprAggregateNodeBase;
import com.espertech.esper.epl.expression.codegen.ExprForgeCodegenSymbol;
import com.espertech.esper.epl.expression.core.ExprForge;
import com.espertech.esper.epl.expression.core.ExprValidationException;
import com.espertech.esper.epl.expression.methodagg.ExprMethodAggUtil;
import com.espertech.esper.epl.expression.methodagg.ExprStddevNode;

public class AggregationMethodFactoryStddev implements AggregationMethodFactory {
    protected final ExprStddevNode parent;
    protected final Class aggregatedValueType;

    public AggregationMethodFactoryStddev(ExprStddevNode parent, Class aggregatedValueType) {
        this.parent = parent;
        this.aggregatedValueType = aggregatedValueType;
    }

    public boolean isAccessAggregation() {
        return false;
    }

    public Class getResultType() {
        return Double.class;
    }

    public AggregationStateKey getAggregationStateKey(boolean isMatchRecognize) {
        throw new IllegalStateException("Not an access aggregation function");
    }

    public AggregationStateFactoryForge getAggregationStateFactory(boolean isMatchRecognize) {
        throw new IllegalStateException("Not an access aggregation function");
    }

    public AggregationAccessorForge getAccessorForge() {
        throw new IllegalStateException("Not an access aggregation function");
    }

    public AggregationMethod make() {
        AggregationMethod method = makeStddevAggregator(parent.isHasFilter());
        if (!parent.isDistinct()) {
            return method;
        }
        return AggregationMethodFactoryUtil.makeDistinctAggregator(method, parent.isHasFilter());
    }

    public ExprAggregateNodeBase getAggregationExpression() {
        return parent;
    }

    public void validateIntoTableCompatible(AggregationMethodFactory intoTableAgg) throws ExprValidationException {
        AggregationValidationUtil.validateAggregationType(this, intoTableAgg);
        AggregationMethodFactoryStddev that = (AggregationMethodFactoryStddev) intoTableAgg;
        AggregationValidationUtil.validateAggregationInputType(aggregatedValueType, that.aggregatedValueType);
        AggregationValidationUtil.validateAggregationFilter(parent.isHasFilter(), that.parent.isHasFilter());
    }

    public AggregationAgentForge getAggregationStateAgent(EngineImportService engineImportService, String statementName) {
        return null;
    }

    public ExprForge[] getMethodAggregationForge(boolean join, EventType[] typesPerStream) throws ExprValidationException {
        return ExprMethodAggUtil.getDefaultForges(parent.getPositionalParams(), join, typesPerStream);
    }

    public void rowMemberCodegen(int column, CodegenCtor ctor, CodegenMembersColumnized membersColumnized, ExprForge[] forges, CodegenClassScope classScope) {
        AggregatorStddev.rowMemberCodegen(parent.isDistinct(), column, ctor, membersColumnized);
    }

    public void applyEnterCodegen(int column, CodegenMethodNode method, ExprForgeCodegenSymbol symbols, ExprForge[] forges, CodegenClassScope classScope) {
        AggregatorStddev.applyEnterCodegen(parent.isDistinct(), parent.isHasFilter(), column, method, symbols, forges, classScope);
    }

    public void applyLeaveCodegen(int column, CodegenMethodNode method, ExprForgeCodegenSymbol symbols, ExprForge[] forges, CodegenClassScope classScope) {
        AggregatorStddev.applyLeaveCodegen(parent.isDistinct(), parent.isHasFilter(), column, method, symbols, forges, classScope);
    }

    public void clearCodegen(int column, CodegenMethodNode method, CodegenClassScope classScope) {
        AggregatorStddev.clearCodegen(parent.isDistinct(), column, method);
    }

    public void getValueCodegen(int column, CodegenMethodNode method, CodegenClassScope classScope) {
        AggregatorStddev.getValueCodegen(column, method);
    }

    private AggregationMethod makeStddevAggregator(boolean hasFilter) {
        if (!hasFilter) {
            return new AggregatorStddev();
        }
        return new AggregatorStddevFilter();
    }
}