class TeachesController < ApplicationController
  before_action :set_teach, only: [:show, :edit, :update, :destroy]

  # GET /teaches
  # GET /teaches.json
  def index
    if teacher_user?
      @teaches = Teach.where(:user_id => current_user)
    else
      @teaches = Teach.all
    end

    respond_to do |format|
      format.html
      format.csv {render text: @teaches.to_csv}
    end
  end

  # GET /teaches/1
  # GET /teaches/1.json
  def show
  end

  def show_teach
    @sec = Sec.find(params[:sec_id])
    @teaches = @sec.teach
  end

  # GET /teaches/new
  def new
    @teach = Teach.new
    @users = User.all
    @subjects = Subject.all
    @secs = Sec.all
  end

  # GET /teaches/1/edit
  def edit
    @users = User.all
    @subjects = Subject.all
    @secs = Sec.all
  end

  # POST /teaches
  # POST /teaches.json
  def create
    @teach = Teach.new
    @user = User.find_by_id(params[:user])
    @teach.user = @user
    @sec = Sec.find_by_id(params[:sec])
    @teach.sec = @sec
    respond_to do |format|
      if @teach.save
        format.html { redirect_to @teach, notice: 'Teach was successfully created.' }
        format.json { render :show, status: :created, location: @teach }
      else
        format.html { render :new }
        format.json { render json: @teach.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /teaches/1
  # PATCH/PUT /teaches/1.json
  def update
    @user = User.find_by_id(params[:user])
    @teach.user = @user
    @sec = Sec.find_by_id(params[:sec])
    @teach.sec = @sec
    respond_to do |format|
      if @teach.update(teach_params)
        format.html { redirect_to @teach, notice: 'Teach was successfully updated.' }
        format.json { render :show, status: :ok, location: @teach }
      else
        format.html { render :edit }
        format.json { render json: @teach.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /teaches/1
  # DELETE /teaches/1.json
  def destroy
    @teach.destroy
    respond_to do |format|
      format.html { redirect_to teaches_url, notice: 'Teach was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  # http://localhost:3000/getTeacherPrivateData?teacher_id=1102001884002&command=getCourse
  # http://localhost:3000/getTeacherPrivateData?teacher_id=1102001884002&command=getStudent
  def getTeacherPrivateData
    if params[:command].to_s == 'getCourse'
      teachs = find_subjects_teacher
      if teachs == '1@' || teachs == '2@'
        render :text => teachs
      else
        tea = ''
        teachs.each do |teach| 
          tea += teach.sec.subject.subject_ID + '!' 
          tea += teach.sec.subject.subject_Name + '!' 
          tea += teach.sec.sec + '!'
          tea += teach.user.teacher.teacherid + '!'
          tea += teach.user.name + '@'
        end
        render :text => tea
      end
    elsif params[:command].to_s == 'getStudent'
      teachs = find_subjects_teacher
      if teachs == '1@' || teachs == '2@'
        render :text => teachs
      else
        stu = ''
        teachs.each do |teach|
          studies = Study.where(:sec_id => teach.sec_id)
          studies.each do |study|
            stu += study.user.student.studentid + '!'
            stu += study.user.student.uid + '!'
            stu += study.user.name + '@'
          end
        end
        if stu.length == 0
          render :text => '3@'
        else
          render :text => stu
        end
      end

    else
      render :text => ''
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_teach
      @teach = Teach.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def teach_params
      params.require(:teach).permit(:user_id, :sec_id)
    end

    def find_subjects_teacher
      tea = ''
      teacher = Teacher.find_by_teacherid(params[:teacher_id])
      if teacher != nil
        teachs = Teach.where(:user_id => teacher.user.id)
        if teachs.length == 0
          tea = '2@'
        else
          tea = teachs
        end
      else
        tea = '1@'
      end
      tea
    end

end
