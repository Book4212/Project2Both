json.array!(@faculties) do |faculty|
  json.extract! faculty, :id, :facultyname
  json.url faculty_url(faculty, format: :json)
end
