class CreateAffectivedomains < ActiveRecord::Migration
  def change
    create_table :affectivedomains do |t|
      t.float :point
      t.text :description
      t.datetime :active_day
      t.references :user, index: true, foreign_key: true
      t.references :sec, index: true, foreign_key: true

      t.timestamps null: false
    end
  end
end
