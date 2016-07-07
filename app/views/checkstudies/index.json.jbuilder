json.array!(@checkstudies) do |checkstudy|
  json.extract! checkstudy, :id, :status, :time_check, :round, :user_id, :sec_id
  json.url checkstudy_url(checkstudy, format: :json)
end
