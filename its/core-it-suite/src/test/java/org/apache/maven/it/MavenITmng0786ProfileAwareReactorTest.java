/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.it;

import java.io.File;

import org.junit.jupiter.api.Test;

/**
 * This is a test set for <a href="https://issues.apache.org/jira/browse/MNG-786">MNG-786</a>.
 *
 * @author John Casey
 *
 */
public class MavenITmng0786ProfileAwareReactorTest extends AbstractMavenIntegrationTestCase {
    public MavenITmng0786ProfileAwareReactorTest() {
        super(ALL_MAVEN_VERSIONS);
    }

    /**
     * Verify that direct invocation of a mojo from the command line still
     * results in the processing of modules included via profiles.
     *
     * @throws Exception in case of failure
     */
    @Test
    public void testitMNG0786() throws Exception {
        File testDir = extractResources("/mng-0786");

        Verifier verifier = newVerifier(testDir.getAbsolutePath());
        verifier.setAutoclean(false);
        verifier.deleteDirectory("sub1/target");
        verifier.deleteDirectory("sub2/target");
        verifier.addCliArgument("-Dexpression.outputFile=target/expression.properties");
        verifier.addCliArgument("-Dactivate=anything");
        verifier.addCliArgument("org.apache.maven.its.plugins:maven-it-plugin-expression:2.1-SNAPSHOT:eval");
        verifier.execute();
        verifier.verifyFilePresent("sub1/target/expression.properties");
        verifier.verifyFilePresent("sub2/target/expression.properties");
        verifier.verifyErrorFreeLog();
    }
}
