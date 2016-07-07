class CreateUsers < ActiveRecord::Migration
  def change
    create_table :users do |t|
      t.string :name
      t.string :email

      t.integer :role
      t.string :identification

      t.timestamps null: false
    end
  end
end
