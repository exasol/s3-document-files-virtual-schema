# Setting up the S3-Bucket Using Terraform

[Terraform](https://www.terraform.io/) is a tool that lets you define infrastructure a code.

To run it, you need the [terraform cli](https://www.terraform.io/downloads.html) client installed on your PC.

Next download the [awsSetup](./awsSetup) folder.
In the `awsSetup.tf` you can see that I assign some Exasol specific tags to the bucket with values from terraform variables.
You can change this part of the script or completely remove the tags if you don't want to tag your resources.

Finally run:
```shell script
terraform init
terraform apply
```  
 