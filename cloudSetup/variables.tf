variable "owner" {}

variable "deputy" {
  default = ""
}

# Use a short project tag. Long tags will cause the exasol cluster creation to fail (see https://github.com/exasol/cloudformation-aws-exasol/issues/3)
variable "project" {
  default = "VSS3"
}

variable "additional_tags" {
  default = {}
  description = "Additional resource tags"
  type = map(string)
}

variable "override_node_count" {
  # Don't change cluster config. It will destroy comparability to the test history.
  default = 4
  description = "Override the data-node count. Warning: Don't override for regression tests!"
}

variable "override_instance_type" {
  # Don't change cluster config. It will destroy comparability to the test history.
  default = "c5.4xlarge"
  description = "Override the data-node instance type. Warning: Don't override for regression tests!"
}

variable "aws_profile" {
  default = ""
}