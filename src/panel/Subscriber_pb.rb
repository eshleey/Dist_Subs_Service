# frozen_string_literal: true
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: Subscriber.proto

require 'google/protobuf'


descriptor_data = "\n\x10Subscriber.proto\x12\rcommunication\"\xa9\x01\n\nSubscriber\x12)\n\x06\x64\x65mand\x18\x01 \x01(\x0e\x32\x19.communication.DemandType\x12\n\n\x02ID\x18\x02 \x01(\r\x12\x14\n\x0cname_surname\x18\x03 \x01(\t\x12\x12\n\nstart_date\x18\x04 \x01(\x03\x12\x15\n\rlast_accessed\x18\x05 \x01(\x03\x12\x11\n\tinterests\x18\x06 \x03(\t\x12\x10\n\x08isOnline\x18\x07 \x01(\x08*\x1f\n\nDemandType\x12\x08\n\x04SUBS\x10\x00\x12\x07\n\x03\x44\x45L\x10\x01\x62\x06proto3"

pool = Google::Protobuf::DescriptorPool.generated_pool
pool.add_serialized_file(descriptor_data)

module Communication
  Subscriber = ::Google::Protobuf::DescriptorPool.generated_pool.lookup("communication.Subscriber").msgclass
  DemandType = ::Google::Protobuf::DescriptorPool.generated_pool.lookup("communication.DemandType").enummodule
end
