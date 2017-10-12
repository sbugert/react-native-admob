require "json"

json = File.read(File.join(__dir__, "package.json"))
package = JSON.parse(json).deep_symbolize_keys

Pod::Spec.new do |s|
  s.name = package[:name]
  s.version = package[:version]
  s.license = package[:license]
  s.authors = package[:author]
  s.summary = package[:description]
  s.source = { :git => 'https://github.com/sbugert/react-native-admob.git',  :tag => 'v'+s.version.to_s } 
  s.homepage = 'https://github.com/sbugert/react-native-admob' 
  s.source_files   = 'ios/*.{h,m}'
  
  s.platform = :ios, "8.0"

  s.dependency 'Google-Mobile-Ads-SDK'
  s.dependency 'React'

end
