#!/usr/bin/python3
from pyspark.sql import SparkSession

# This script converts the JSON files into a parquet file

spark = SparkSession \
    .builder \
    .appName("Converter") \
    .getOrCreate()

json_df = spark.read.json("./book-*.json", multiLine=True)
json_df.printSchema()
json_df.coalesce(1).write.parquet("output")
