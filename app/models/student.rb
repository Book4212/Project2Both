class Student < ActiveRecord::Base
  belongs_to :user
  belongs_to :department

  scope :search, -> (search) { where("studentid LIKE ?", "%#{search}%") }

  # VALID_EMAIL_REGEX = /\A[\w+\-.]+@[a-z\d\-]+(\.[a-z\d\-]+)*\.[a-z]+\z/i
	# validates :facebook, presence: true, length:{ maximum: 255 }, format: { with: VALID_EMAIL_REGEX }, uniqueness: { case_sensitive:false }
	
	def self.to_csv
  	CSV.generate do |csv|
  		csv << column_names
  		all.each do |student|
  			csv << student.attributes.values_at(*column_names)
  		end
  	end
  end

end
