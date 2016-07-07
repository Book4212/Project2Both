class CreateTeaches < ActiveRecord::Migration
  def change
    create_table :teaches do |t|
      t.references :user, index: true, foreign_key: true
      t.references :sec, index: true, foreign_key: true

      t.timestamps null: false
    end
  end
end
