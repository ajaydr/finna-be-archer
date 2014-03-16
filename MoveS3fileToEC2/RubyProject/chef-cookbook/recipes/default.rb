#
# Cookbook Name:: MoveS3FileToEC2
# Recipe:: default
#

fog = gem_package "fog" do
  action :install
  action :nothing
end

fog.run_action(:install)
Gem.clear_paths