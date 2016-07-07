# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)



User.create!(name:  "พศิน ทองมณี",
             email: "admin@hotmail.com",
             identification: "1234567890123",
             password:              "12345678",
             password_confirmation: "12345678",
             role: 2 , # admin
             admin: true)

User.create!(name:  "สาธิต ดูแลระบบ",
             email: "a1102001884001",
             identification: "1102001884001",
             password:              "12345678",
             password_confirmation: "12345678",
             role: 2 , # admin
             admin: true)

teacher1 = User.create!(name:  "สำรวย ทรัพท์เยอะ",
             email: "t1102001884002",
             identification: "1102001884002",
             password:              "12345678",
             password_confirmation: "12345678",
             role: 1) #teacher

teacher2 = User.create!(name:  "จำใจ ใจดี",
             email: "t1102001884003",
             identification: "1102001884003",
             password:              "12345678",
             password_confirmation: "12345678",
             role: 1) #teacher

teacher3 = User.create!(name:  "สบาย สบายดี",
             email: "t1102001884004",
             identification: "1102001884004",
             password:              "12345678",
             password_confirmation: "12345678",
             role: 1) #teacher

teacher4 = User.create!(name:  "อนุรักษ์ แจกเกรด",
             email: "t1102001884005",
             identification: "1102001884005",
             password:              "12345678",
             password_confirmation: "12345678",
             role: 1) #teacher

student1 = User.create!(name:  "พศิน ทองมณี",
             email: "s5501012630206",
             identification: "1102001884006",
             password:              "12345678",
             password_confirmation: "12345678",
             role: 0) #student

student2 = User.create!(name:  "ทัชกร คุ้นเคย",
             email: "s5501012630207",
             identification: "1102001884007",
             password:              "12345678",
             password_confirmation: "12345678",
             role: 0) #student

student3 = User.create!(name:  "พงศ์ยานน ยางเอน",
             email: "s5501012630208",
             identification: "1102001884008",
             password:              "12345678",
             password_confirmation: "12345678",
             role: 0) #student

student4 = User.create!(name:  "นำชัย นิมิตรจิตรภัคดี",
             email: "s5501012630209",
             identification: "1102001884009",
             password:              "12345678",
             password_confirmation: "12345678",
             role: 0) #student

faculty1 = Faculty.create!(facultyname: "วิศวกรรมศาสตร์")
faculty2 = Faculty.create!(facultyname: "วิทยาศาสตร์")

department1 = Department.create!(departmentname: "วิศวคอมพิวเตอร์",
                  faculty: faculty1)
department2 = Department.create!(departmentname: "วิศวไฟฟ้า",
                  faculty: faculty1)
department3 = Department.create!(departmentname: "วิศวสื่อสาร",
                  faculty: faculty1)
department4 = Department.create!(departmentname: "วิศวการบิน",
                  faculty: faculty1)
department5 = Department.create!(departmentname: "วิศวการผลิต",
                  faculty: faculty1)
department6 = Department.create!(departmentname: "เคมี",
                  faculty: faculty2)
department7 = Department.create!(departmentname: "ชีวะวิทยา",
                  faculty: faculty2)

Teacher.create!(  teacherid: "1102001884002",
                  user: teacher1 ,
                  department: department1)

Teacher.create!(  teacherid: "1102001884003",
                  user: teacher2 ,
                  department: department7)

Teacher.create!(  teacherid: "1102001884004",
                  user: teacher3 ,
                  department: department1)

Teacher.create!(  teacherid: "1102001884005",
                  user: teacher4 ,
                  department: department1)

Student.create!(  studentid: "5501012630206",
                  uid: "66996103",
                  user: student1 ,
                  firstyear: 2555,
                  department: department1)

Student.create!(  studentid: "5501012630207",
                  uid: "66996104",
                  user: student2 ,
                  firstyear: 2555,
                  department: department1)

Student.create!(  studentid: "5501012630208",
                  uid: "66996105",
                  user: student3 ,
                  firstyear: 2555,
                  department: department1)

Student.create!(  studentid: "5501012630209",
                  uid: "368DB201",
                  user: student4 ,
                  firstyear: 2555,
                  department: department1)

subject1 = Subject.create!(  subject_ID: "010123122",
                  subject_Name: "network")

subject2 = Subject.create!(  subject_ID: "010123123",
                  subject_Name: "projectII")

subject3 = Subject.create!(  subject_ID: "040423143",
                  subject_Name: "chemistryI")

subject4 = Subject.create!(  subject_ID: "040423133",
                  subject_Name: "bioI")

time1 = Time.new(2558,1,1,16,0,0)
time2 = Time.new(2558,1,1,20,0,0)
time3 = Time.new(2558,1,1,0,0,0)

subject1_sec1 = Sec.create!(sec: "1",
            years: "2558",
            term: "2",
            classroom: "81-521",
            timestudy: time1 ,
            subject: subject1)

subject1_sec2 = Sec.create!(sec: "2",
            years: "2558",
            term: "2",
            classroom: "81-522",
            timestudy: time3 ,
            subject: subject1)

subject1_sec3 = Sec.create!(sec: "3",
            years: "2558",
            term: "2",
            classroom: "81-523",
            timestudy: time3 ,
            subject: subject1)

subject2_sec1 = Sec.create!(sec: "1",
            years: "2558",
            term: "2",
            classroom: "81-521",
            timestudy: time2 ,
            subject: subject2)

subject3_sec1 = Sec.create!(sec: "1",
            years: "2558",
            term: "2",
            classroom: "88-18001",
            timestudy: time2 ,
            subject: subject3)

subject4_sec1 = Sec.create!(sec: "1",
            years: "2558",
            term: "2",
            classroom: "88-18003",
            timestudy: time2 ,
            subject: subject4)

time4 = Time.new(2558,6,23,16,0,0)
time5 = Time.new(2558,7,3,16,0,0)

activity1 = Activity.create!(activityname: "meeting 01",
            dateactivity: time4,
            place: "172 M.1 street parame 2 nontabure bangkok",
            counts: 1,
            sec: subject1_sec1)

activity1 = Activity.create!(activityname: "meeting 02",
            dateactivity: time5,
            place: "172 M.1 street parame 2 nontabure bangkok",
            counts: 2,
            sec: subject1_sec1)

Teach.create!(user: teacher1,
            sec: subject1_sec1)

Teach.create!(user: teacher1,
            sec: subject1_sec2)

Teach.create!(user: teacher2,
            sec: subject3_sec1)

Teach.create!(user: teacher2,
            sec: subject4_sec1)

Teach.create!(user: teacher3,
            sec: subject1_sec3)

Study.create!(user: student1,
            sec: subject1_sec1)

Study.create!(user: student2,
            sec: subject1_sec1)

Study.create!(user: student3,
            sec: subject1_sec1)

Study.create!(user: student4,
            sec: subject1_sec1)

Study.create!(user: student1,
            sec: subject2_sec1)

Study.create!(user: student2,
            sec: subject2_sec1)

Study.create!(user: student2,
            sec: subject4_sec1)

Study.create!(user: student2,
            sec: subject3_sec1)

Study.create!(user: student3,
            sec: subject3_sec1)

Study.create!(user: student3,
            sec: subject4_sec1)

Study.create!(user: student4,
            sec: subject4_sec1)

# check study "come class","come late"
Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,6,1,16,0,10),
            round: 1 ,
            user: student1,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,6,1,16,0,15),
            round: 1 ,
            user: student2,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,6,1,16,1,10),
            round: 1 ,
            user: student3,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,6,1,16,2,10),
            round: 1 ,
            user: student4,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,6,8,16,0,15),
            round: 2 ,
            user: student1,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,6,8,16,0,30),
            round: 2 ,
            user: student2,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,6,8,16,3,10),
            round: 2 ,
            user: student3,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,6,8,16,4,10),
            round: 2 ,
            user: student4,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come late",
            time_check: Time.new(2558,6,15,16,30,15),
            round: 3 ,
            user: student1,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,6,15,16,0,30),
            round: 3 ,
            user: student2,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,6,15,16,4,10),
            round: 3 ,
            user: student4,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,6,22,16,1,15),
            round: 4 ,
            user: student1,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,6,22,16,0,30),
            round: 4 ,
            user: student2,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,6,22,16,2,10),
            round: 4 ,
            user: student3,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come late",
            time_check: Time.new(2558,6,22,16,25,10),
            round: 4 ,
            user: student4,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come late",
            time_check: Time.new(2558,6,29,16,15,15),
            round: 5 ,
            user: student1,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,6,29,16,0,30),
            round: 5 ,
            user: student2,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,6,29,16,5,10),
            round: 5 ,
            user: student4,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,7,5,16,1,15),
            round: 6 ,
            user: student1,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,7,5,16,0,30),
            round: 6 ,
            user: student2,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,7,5,16,2,10),
            round: 6 ,
            user: student3,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,7,5,16,5,10),
            round: 6 ,
            user: student4,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come late",
            time_check: Time.new(2558,7,12,16,21,30),
            round: 7 ,
            user: student2,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,7,12,16,2,10),
            round: 7 ,
            user: student3,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,7,12,16,5,10),
            round: 7 ,
            user: student4,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,7,19,16,0,30),
            round: 8 ,
            user: student2,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come class",
            time_check: Time.new(2558,7,19,16,2,10),
            round: 8 ,
            user: student3,
            sec: subject1_sec1 )

Checkstudy.create!(status: "come late",
            time_check: Time.new(2558,7,19,16,15,10),
            round: 8 ,
            user: student4,
            sec: subject1_sec1 )

Affectivedomain.create!(point: 10,
            description: "ตอบคำถามได้ดี",
            active_day: Time.new(2558,6,1,17,30,10),
            user: student1,
            sec: subject1_sec1)

Affectivedomain.create!(point: -5.5,
            description: "ไม่ส่งการบ้าน",
            active_day: Time.new(2558,6,15,16,20,10),
            user: student1,
            sec: subject1_sec1)

Affectivedomain.create!(point: 4,
            description: "ช่วยงานอาจารย์",
            active_day: Time.new(2558,6,29,18,35,10),
            user: student1,
            sec: subject1_sec1)

Affectivedomain.create!(point: 3.5,
            description: "",
            active_day: Time.new(2558,7,12,17,30,10),
            user: student1,
            sec: subject1_sec1)

Affectivedomain.create!(point: 5,
            description: "มีส่วนร่วมกิจกรรมในห้อง",
            active_day: Time.new(2558,7,19,18,30,10),
            user: student1,
            sec: subject1_sec1)