require 'socket'
require 'google/protobuf'
require './Message_pb'
require './Configuration_pb'



# dist_subs.conf dosyasını okuyup, hata tolerans seviyesini al
class ConfigReader
  attr_reader :fault_tolerance_level

  def initialize(file_path)
    @file_path = file_path
    parse_config
  end

  private

  def parse_config
    File.readlines(@file_path).each do |line|
      if line.start_with?("fault_tolerance_level")
        @fault_tolerance_level = line.split('=').last.strip.to_i
      end
    end
  end
end

# Protobuf Configuration sınıfından bir nesne oluşturun
class ConfigurationMessage
  attr_reader :message

  def initialize(fault_tolerance_level, method)
    @message = Communication::Configuration.new
    @message.fault_tolerance_level = fault_tolerance_level
    @message.method = method
  end
end

# Protobuf Message sınıfından bir nesne oluşturun
class Message
  attr_reader :message

  def initialize(demand, response)
    @message = Communication::Message.new
    @message.demand = demand
    @message.response = response
  end
end

# Sunuculara bağlanıp, "STRT" mesajını gönder
class Admin
  SERVER_PORTS = [5001, 5002, 5003]

  def initialize(config_file)
    config_reader = ConfigReader.new(config_file)
    @fault_tolerance_level = config_reader.fault_tolerance_level
    @config_message = ConfigurationMessage.new(@fault_tolerance_level, "STRT").message
  end

  def send_start_command
    SERVER_PORTS.each do |port|
      begin
        socket = TCPSocket.new('localhost', port)
        socket.write(@config_message.to_proto)
        puts "Başlatma komutu gönderildi: Server #{port}"

        # Sunucudan yanıt almak için Message nesnesini hazırla
        response_message = Message.new("STRT", "YEP").message

        
        socket.write(response_message.to_proto)
        puts "Yanıt gönderildi: #{response_message.demand} -> #{response_message.response}"

        socket.close
      rescue StandardError => e
        puts "Sunucuya bağlanırken hata oluştu: #{e.message}"
      end
    end
  end
end

# dist_subs.conf dosyasını okuma ve serverlara komut gönderme
admin = Admin.new('dist_subs.conf')
admin.send_start_command
