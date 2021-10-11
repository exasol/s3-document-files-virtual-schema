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
  datanode_count = var.override_node_count
  datanode_instance_type = var.override_instance_type
}