/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package com.virtualdogbert.extensions

import com.virtualdogbert.artefact.DomainArtefactHandler
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.ClassExpression
import org.codehaus.groovy.ast.expr.MethodCall
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.transform.stc.GroovyTypeCheckingExtensionSupport.TypeCheckingDSL
/**
 * The CriteriaTypeCheckingExtension from Grails.
 */
class CriteriaTypeCheckingExtension extends TypeCheckingDSL {

    @Override
    Object run() {
        setup { newScope() }

        finish { scopeExit() }

        methodNotFound { ClassNode receiver, String name, ArgumentListExpression argList, ClassNode[] argTypes, MethodCall call ->
            print "hdakjhjka"
            def dynamicCall
            if(currentScope.processingCriteriaClosure) {
                dynamicCall = makeDynamic (call)
            }
            dynamicCall
        }

        afterMethodCall { MethodCall call ->
            print "hdakjhjka"
            if(isCriteriaCall(call)) {
                scopeExit()
            }
        }

        beforeMethodCall { MethodCall call ->
            if(isCriteriaCall(call)) {
                newScope {
                    processingCriteriaClosure = true
                }
            }
        }
        null
    }

    protected boolean isCriteriaCall(MethodCall call) {
        call instanceof MethodCallExpression &&
        call.objectExpression instanceof ClassExpression &&
        DomainArtefactHandler.isArtefact(call.objectExpression.type, call.objectExpression.type.name) &&
        (call.method.value == 'withCriteria' || call.method.value == 'createCriteria')
    }
}