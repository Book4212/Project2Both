class CreateActivities < ActiveRecord::Migration
  def change
    create_table :activities do |t|
      t.string :activityname
      t.integer :counts
      t.datetime :dateactivity
      t.string :place
      t.references :sec, index: true, foreign_key: true

      t.timestamps null: false
    end
  end
end
