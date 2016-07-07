json.array!(@affectivedomains) do |affectivedomain|
  json.extract! affectivedomain, :id, :point, :description, :active_day, :user_id, :sec_id
  json.url affectivedomain_url(affectivedomain, format: :json)
end
