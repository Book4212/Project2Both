class Activity < ActiveRecord::Base
  belongs_to :sec
  has_many :checkactivity
end
