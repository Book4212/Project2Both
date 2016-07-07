class Teach < ActiveRecord::Base
  belongs_to :user
  belongs_to :sec

  def self.to_csv
  	CSV.generate do |csv|
  		csv << column_names
  		all.each do |teach|
  			csv << teach.attributes.values_at(*column_names)
  		end
  	end
  end

end
