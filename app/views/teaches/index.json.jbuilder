json.array!(@teaches) do |teach|
  json.extract! teach, :id, :user_id, :sec_id
  json.url teach_url(teach, format: :json)
end
