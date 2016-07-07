# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20160615094037) do

  create_table "activities", force: :cascade do |t|
    t.string   "activityname"
    t.integer  "counts"
    t.datetime "dateactivity"
    t.string   "place"
    t.integer  "sec_id"
    t.datetime "created_at",   null: false
    t.datetime "updated_at",   null: false
  end

  add_index "activities", ["sec_id"], name: "index_activities_on_sec_id"

  create_table "affectivedomains", force: :cascade do |t|
    t.float    "point"
    t.text     "description"
    t.datetime "active_day"
    t.integer  "user_id"
    t.integer  "sec_id"
    t.datetime "created_at",  null: false
    t.datetime "updated_at",  null: false
  end

  add_index "affectivedomains", ["sec_id"], name: "index_affectivedomains_on_sec_id"
  add_index "affectivedomains", ["user_id"], name: "index_affectivedomains_on_user_id"

  create_table "checkactivities", force: :cascade do |t|
    t.integer  "user_id"
    t.integer  "activity_id"
    t.datetime "time_check"
    t.datetime "created_at",  null: false
    t.datetime "updated_at",  null: false
  end

  add_index "checkactivities", ["activity_id"], name: "index_checkactivities_on_activity_id"
  add_index "checkactivities", ["user_id"], name: "index_checkactivities_on_user_id"

  create_table "checkstudies", force: :cascade do |t|
    t.string   "status"
    t.datetime "time_check"
    t.integer  "round"
    t.integer  "user_id"
    t.integer  "sec_id"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  add_index "checkstudies", ["sec_id"], name: "index_checkstudies_on_sec_id"
  add_index "checkstudies", ["user_id"], name: "index_checkstudies_on_user_id"

  create_table "departments", force: :cascade do |t|
    t.string   "departmentname"
    t.integer  "faculty_id"
    t.datetime "created_at",     null: false
    t.datetime "updated_at",     null: false
  end

  add_index "departments", ["faculty_id"], name: "index_departments_on_faculty_id"

  create_table "faculties", force: :cascade do |t|
    t.string   "facultyname"
    t.datetime "created_at",  null: false
    t.datetime "updated_at",  null: false
  end

  create_table "microposts", force: :cascade do |t|
    t.text     "content"
    t.integer  "user_id"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  add_index "microposts", ["user_id"], name: "index_microposts_on_user_id"

  create_table "secs", force: :cascade do |t|
    t.string   "sec"
    t.string   "years"
    t.string   "term"
    t.string   "classroom"
    t.time     "timestudy"
    t.integer  "subject_id"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  add_index "secs", ["subject_id"], name: "index_secs_on_subject_id"

  create_table "students", force: :cascade do |t|
    t.string   "studentid"
    t.string   "uid"
    t.integer  "user_id"
    t.integer  "firstyear"
    t.integer  "department_id"
    t.datetime "created_at",    null: false
    t.datetime "updated_at",    null: false
  end

  add_index "students", ["department_id"], name: "index_students_on_department_id"
  add_index "students", ["user_id"], name: "index_students_on_user_id"

  create_table "studies", force: :cascade do |t|
    t.integer  "user_id"
    t.integer  "sec_id"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  add_index "studies", ["sec_id"], name: "index_studies_on_sec_id"
  add_index "studies", ["user_id"], name: "index_studies_on_user_id"

  create_table "subjects", force: :cascade do |t|
    t.string   "subject_ID"
    t.string   "subject_Name"
    t.datetime "created_at",   null: false
    t.datetime "updated_at",   null: false
  end

  create_table "teachers", force: :cascade do |t|
    t.string   "teacherid"
    t.integer  "user_id"
    t.integer  "department_id"
    t.datetime "created_at",    null: false
    t.datetime "updated_at",    null: false
  end

  add_index "teachers", ["department_id"], name: "index_teachers_on_department_id"
  add_index "teachers", ["user_id"], name: "index_teachers_on_user_id"

  create_table "teaches", force: :cascade do |t|
    t.integer  "user_id"
    t.integer  "sec_id"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  add_index "teaches", ["sec_id"], name: "index_teaches_on_sec_id"
  add_index "teaches", ["user_id"], name: "index_teaches_on_user_id"

  create_table "users", force: :cascade do |t|
    t.string   "name"
    t.string   "email"
    t.integer  "role"
    t.string   "identification"
    t.datetime "created_at",                      null: false
    t.datetime "updated_at",                      null: false
    t.string   "password_digest"
    t.string   "remember_digest"
    t.boolean  "admin",           default: false
  end

  add_index "users", ["email"], name: "index_users_on_email", unique: true

end
