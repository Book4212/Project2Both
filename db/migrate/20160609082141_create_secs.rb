class CreateSecs < ActiveRecord::Migration
  def change
    create_table :secs do |t|
      t.string :sec
      t.string :years
      t.string :term
      t.string :classroom
      t.time :timestudy
      t.references :subject, index: true, foreign_key: true

      t.timestamps null: false
    end
  end
end
