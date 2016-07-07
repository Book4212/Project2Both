json.array!(@activities) do |activity|
  json.extract! activity, :id, :activityname, :dateactivity, :place, :counts, :sec_id
  json.url activity_url(activity, format: :json)
end
