class CreateCheckactivities < ActiveRecord::Migration
  def change
    create_table :checkactivities do |t|
      t.references :user, index: true, foreign_key: true
      t.references :activity, index: true, foreign_key: true
      t.datetime :time_check
      t.timestamps null: false
    end
  end
end
