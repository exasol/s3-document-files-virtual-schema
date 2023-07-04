provider "aws" {
  region  = "eu-central-1"
  profile = var.aws_profile
}

module "exasol_setup" {
  source                 = "exasol/exasol-test-setup/aws"
  version                = "1.2.1"
  owner                  = var.owner
  deputy                 = var.deputy
  project                = var.project
  datanode_count         = var.override_node_count
  datanode_instance_type = var.override_instance_type
}
