package org.apache.maven.plugin.coreit;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Injects artifacts from the plugin into the dependency artifacts of the project.
 * 
 * @goal inject
 * 
 * @author Benjamin Bentmann
 * @version $Id$
 */
public class InjectMojo
    extends AbstractMojo
{

    /**
     * The version-less keys in the form <code>groupId:artifactId</code> of the plugin artifacts to inject into
     * dependency artifacts of the project.
     * 
     * @parameter
     */
    private String[] artifacts;

    /**
     * @parameter default-value="${plugin.artifacts}"
     * @readonly
     */
    private Collection pluginArtifacts;

    /**
     * The current Maven project.
     * 
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * Runs this mojo.
     * 
     * @throws MojoExecutionException If an error occured.
     */
    public void execute()
        throws MojoExecutionException
    {
        Set artifactKeys = new LinkedHashSet();

        if ( artifacts != null )
        {
            artifactKeys.addAll( Arrays.asList( artifacts ) );
        }

        Set dependencyArtifacts = project.getDependencyArtifacts();

        if ( dependencyArtifacts != null )
        {
            dependencyArtifacts = new LinkedHashSet( dependencyArtifacts );
        }
        else
        {
            dependencyArtifacts = new LinkedHashSet();
        }

        for ( Iterator it = pluginArtifacts.iterator(); it.hasNext(); )
        {
            Artifact artifact = (Artifact) it.next();

            String artifactKey = artifact.getGroupId() + ':' + artifact.getArtifactId();

            if ( artifactKeys.remove( artifactKey ) )
            {
                getLog().info( "[MAVEN-CORE-IT-LOG] Injecting dependency artifact " + artifact );

                dependencyArtifacts.add( artifact );
            }
        }

        project.setDependencyArtifacts( dependencyArtifacts );

        getLog().info( "[MAVEN-CORE-IT-LOG] Set dependency artifacts to " + dependencyArtifacts );

        if ( !artifactKeys.isEmpty() )
        {
            getLog().warn( "[MAVEN-CORE-IT-LOG] These artifacts were not found " + artifactKeys );
        }
    }

}
