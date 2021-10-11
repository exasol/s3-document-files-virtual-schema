provider "aws" {
  region = "eu-central-1"
}

module "exasol_setup" {
  source = "exasol/exasol-test-setup/aws"
  version = "1.1.0"
  owner = var.owner
  deputy = var.deputy
  project = var.project
  # Don't change cluster config. It will destroy comparability to the test history.
  datanode_count = 4
  datanode_instance_type = "c5.4xlarge"
}