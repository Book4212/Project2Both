json.array!(@studies) do |study|
  json.extract! study, :id, :user_id, :sec_id
  json.url study_url(study, format: :json)
end
