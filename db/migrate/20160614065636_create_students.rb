class CreateStudents < ActiveRecord::Migration
  def change
    create_table :students do |t|
      t.string :studentid
      t.string :uid
      t.references :user, index: true, foreign_key: true
      t.integer :firstyear
      t.references :department, index: true, foreign_key: true

      t.timestamps null: false
    end
  end
end
