class Subject < ActiveRecord::Base
	has_many :secs

	scope :search, -> (search) { where("subject_Name LIKE ?", "%#{search}%") }

	def self.to_csv
  	CSV.generate do |csv|
  		csv << column_names
  		all.each do |subject|
  			csv << subject.attributes.values_at(*column_names)
  		end
  	end
  end

end
