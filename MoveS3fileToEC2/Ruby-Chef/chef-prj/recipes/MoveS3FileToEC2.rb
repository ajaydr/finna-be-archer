#
# Cookbook Name:: MoveS3FileToEC2
# Recipe:: MoveS3FileToEC2
#
# Description: Copy file from S3 bucket to EC2 node.
# chef client will execute this recipe from ec2 node.
#
include_recipe "default"
require 'fog'           #fog is Ruby cloud services library.

Chef::Log.info("Starting MoveS3FileToEC2 recipe.")

bucket = node[:s3_to_ec2][:s3_bucket]
destination_fld = node[:s3_to_ec2][:destination_folder]

# Function to download and copy file from s3 bucket to ec2 instance.
def download_files_from_S3_bucket(s3bucket_name, delete_first, dest_folder)
  s3conn = Fog::Storage.new({
                                :provider => 'AWS',
                                :aws_access_key_id => node[:s3_to_ec2][:aws_access_key],
                                :aws_secret_access_key => node[:s3_to_ec2][:aws_secret_key]})

  s3_bucket = s3conn.directories.get(s3bucket_name)
  file_keys = []
  s3_bucket.files.each {|f| file_keys << f.key }
  file_keys.each do |key|
    dest = "#{dest_folder}\\#{key}"
    File.delete(dest) if File.exists?(dest) && delete_first
    if not File.exists?(dest)
      Chef::Log.info("copying S3 file to #{dest}...")
      File.open(dest, 'wb') do |local_file|
        s3_bucket.files.get(key) do |chunk, remaining_bytes, total_bytes|
          local_file << chunk
        end
      end
    end
  end
end

#Create Destination director if it does not exists.
Dir::mkdir "#{destination_fld}" if not File.exist?("#{destination_fld}")

download_files_from_S3_bucket "#{bucket}", true, "#{destination_fld}"

Chef::Log.info("Completed MoveS3FileToEC2 recipe.")