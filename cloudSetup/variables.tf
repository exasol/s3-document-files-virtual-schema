variable "owner" {}

variable "deputy" {
  default = ""
}

# Use a short project tag. Long tags will cause the exasol cluster creation to fail (see https://github.com/exasol/cloudformation-aws-exasol/issues/3)
variable "project" {
  default = "S3VS"
}

variable "additional_tags" {
  default = {}
  description = "Additional resource tags"
  type = map(string)
}