class Faculty < ActiveRecord::Base
	has_many :departments

	def self.to_csv
  	CSV.generate do |csv|
  		csv << column_names
  		all.each do |faculty|
  			csv << faculty.attributes.values_at(*column_names)
  		end
  	end
  end

end
