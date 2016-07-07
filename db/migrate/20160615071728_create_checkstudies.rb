class CreateCheckstudies < ActiveRecord::Migration
  def change
    create_table :checkstudies do |t|
      t.string :status
      t.datetime :time_check
      t.integer :round
      t.references :user, index: true, foreign_key: true
      t.references :sec, index: true, foreign_key: true

      t.timestamps null: false
    end
  end
end
