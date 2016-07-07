class Department < ActiveRecord::Base
  belongs_to :faculty
  has_many :teacher
  has_many :student

  def self.to_csv
  	CSV.generate do |csv|
  		csv << column_names
  		all.each do |department|
  			csv << department.attributes.values_at(*column_names)
  		end
  	end
  end

end
