#Attributes can be overwritten during recipe execution.
default[:s3_to_ec2][:s3_bucket] = "s3-bucket-xxx"
default[:s3_to_ec2][:destination_folder] = "c:\\s3_to_ec2"
default[:s3_to_ec2][:aws_access_key] = "your aws access key"
default[:s3_to_ec2][:aws_secret_key] = "your aws secret key"