json.array!(@checkactivities) do |checkactivity|
  json.extract! checkactivity, :id, :user_id, :activity_id, :time_check
  json.url checkactivity_url(checkactivity, format: :json)
end
