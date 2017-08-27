/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2015-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.soteria.test;

import static org.glassfish.soteria.test.ShrinkWrap.mavenWar;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
public class AppJaxRsIT extends ArquillianBase {
    
    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        return mavenWar();
    }

    @Test
    public void testAuthenticated() {
        String response = readFromServer("/rest/resource/callerName?name=reza&password=secret1");
        
        assertTrue(
            "Should be authenticated as user reza but was not",
            response.contains("reza"));
    }
    
    @Test
    public void testNotAuthenticated() {
        String response = readFromServer("/rest/resource/callerName");
        
        assertFalse(
            "Should not be authenticated as user reza but was",
            response.contains("reza"));
    }
    
    @Test
    public void testHasRoleFoo() {
        String response = readFromServer("/rest/resource/hasRoleFoo?name=reza&password=secret1");
        
        assertTrue(
            "Should be in role foo, but was not",
            response.contains("true"));
    }
    
    @Test
    public void testNotHasRoleFoo() {
        String response = readFromServer("/rest/resource/hasRoleFoo");
        
        assertTrue(
            "Should not be in role foo, but was",
            response.contains("false"));
    }
    
    @Test
    public void testNotHasRoleKaz1() {
        String response = readFromServer("/rest/resource/hasRoleKaz?name=reza&password=secret1");
        
        assertFalse(
            "Should not be in role kaz, but was",
            response.contains("true"));
    }
    
    @Test
    public void testNotHasRoleKaz2() {
        String response = readFromServer("/rest/resource/hasRoleKaz");
        
        assertFalse(
            "Should not be in role kaz, but was",
            response.contains("true"));
    }
    
    @Test
    public void testSayHi() {
        String response = readFromServer("/rest/protectedResource/sayHi?name=reza&password=secret1");
        
        assertTrue(
            "Endpoint should have been called, but was not",
            response.contains("saying hi!"));
    }
    
    @Test
    public void testNotSayHi() {
        String response = readFromServer("/rest/protectedResource/sayHi");
        
        assertFalse(
            "Endpoint should not have been called, but was",
            response.contains("saying hi!"));
    }

}
