json.array!(@teachers) do |teacher|
  json.extract! teacher, :id, :teacherid, :user_id, :department_id
  json.url teacher_url(teacher, format: :json)
end
