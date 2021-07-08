package com.exasol.adapter.document.files.ciisolation;

import com.exasol.ciisolation.aws.PolicyReader;
import com.exasol.ciisolation.aws.ciuser.CiUserStack;

import software.amazon.awscdk.core.App;

/**
 * This class defines a CloudFormation stack that creates a user for the CI of this project.
 */
public class CiIsolationApp {
    public static void main(final String[] args) {
        final App app = new App();
        new CiUserStack(app, CiUserStack.CiUserStackProps.builder().projectName("s3-files-vs")
                .addRequiredPermissions(new PolicyReader().readPolicyFromResources("test-permissions.json")).build());
        app.synth();
    }
}
