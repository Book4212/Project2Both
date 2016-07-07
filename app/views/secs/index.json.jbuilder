json.array!(@secs) do |sec|
  json.extract! sec, :id, :sec, :years, :term, :classroom, :timestudy, :subject_id
  json.url sec_url(sec, format: :json)
end
