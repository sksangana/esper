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
package com.espertech.esper.supportregression.epl;

import java.util.Map;

public class SupportMethodInvocationJoinInvalid {

    public static Map readRowNoMetadata() {
        return null;
    }

    public static Map readRowWrongMetadata() {
        return null;
    }

    public static Object readRowWrongMetadataMetadata() {
        return null;
    }
}
