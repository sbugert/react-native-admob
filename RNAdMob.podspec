require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = 'RNAdMob'
  s.version      = package["version"]
  s.summary      = package["description"]
  s.author       = package["author"]
  s.homepage     = 'https://github.com/sbugert/react-native-admob'

  s.license      = package["license"]
  s.platform     = :ios, "8.0"

  s.source       = { :git => "https://github.com/sbugert/react-native-admob.git", :tag => "#{s.version}" }
  s.source_files = "ios/*.{h,m}"
  s.static_framework = true
  
  s.dependency 'React'
  s.dependency 'Google-Mobile-Ads-SDK'

end
