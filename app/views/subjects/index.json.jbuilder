json.array!(@subjects) do |subject|
  json.extract! subject, :id, :subject_ID, :subject_Name
  json.url subject_url(subject, format: :json)
end
