json.array!(@students) do |student|
  json.extract! student, :id, :studentid, :uid, :user_id, :firstyear, :department_id
  json.url student_url(student, format: :json)
end
