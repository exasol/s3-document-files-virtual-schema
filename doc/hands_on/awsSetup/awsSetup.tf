provider "aws" {
  region = var.region
  profile = "c1-admin"
}

resource "aws_s3_bucket" "aws_bucket" {
  bucket = "s3-virtual-schema-test-bucket"
  acl = "private"

  tags = {
    "exa:owner" : var.owner,
    "exa:deputy" : var.deputy
    "exa:project" : var.project
    "exa:project.name" : var.project_name
    "exa:stage" : var.stage
    "Name" : "Test bucket for the S3-virtual-schema"
  }
}

resource "aws_s3_bucket_object" "book-1" {
  key = "book-1.json"
  bucket = aws_s3_bucket.aws_bucket.id
  source = "../books/book-1.json"
}

resource "aws_s3_bucket_object" "book-2" {
  key = "book-2.json"
  bucket = aws_s3_bucket.aws_bucket.id
  source = "../books/book-2.json"
}

resource "aws_s3_bucket_object" "book-3" {
  key = "book-3.json"
  bucket = aws_s3_bucket.aws_bucket.id
  source = "../books/book-3.json"
}

resource "aws_s3_bucket_object" "books-parquet" {
  key = "books.parquet"
  bucket = aws_s3_bucket.aws_bucket.id
  source = "../books/books.parquet"
}

