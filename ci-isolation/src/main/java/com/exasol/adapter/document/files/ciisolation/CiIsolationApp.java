package com.exasol.adapter.document.files.ciisolation;

import com.exasol.ciisolation.aws.PolicyReader;
import com.exasol.ciisolation.aws.ciuser.CiUserStack;

import software.amazon.awscdk.App;

/**
 * This class defines a CloudFormation stack that creates a user for the CI of this project.
 */
public class CiIsolationApp {
    public static void main(final String[] args) {
        final App app = new App();
        final PolicyReader policyReader = new PolicyReader();
        new CiUserStack(app,
                CiUserStack.CiUserStackProps.builder().projectName("s3-files-vs")
                        .addRequiredPermissions(policyReader.readPolicyFromResources("test-permissions.json"),
                                policyReader.readPolicyFromResources("exasol-permissions.json"))
                        .build());
        app.synth();
    }
}
