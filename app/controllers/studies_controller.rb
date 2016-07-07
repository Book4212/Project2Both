class StudiesController < ApplicationController
  before_action :set_study, only: [:show, :edit, :update, :destroy]

  # GET /studies
  # GET /studies.json
  def index
    @studies = Study.all

    respond_to do |format|
      format.html
      format.csv {render text: @studies.to_csv}
    end
  end

  def show_student
    @sec = Sec.find(params[:sec_id])
    @studies = @sec.study

    if params[:commit].present?
      @students = Student.search(params[:search])
      @users = []
      @studies = []

      @students.each do |student|
        @users << student.user
      end
      @users.each do |user|
        user.study.each do |study|
          if study.sec_id == params[:sec_id].to_i
            @studies << study
          end
        end
      end
    end


    if params[:colum] == 'user'
      @studies = @studies.sort_by{|a| a.user.student.studentid}
    elsif params[:colum] == 'name'
      @studies = @studies.sort_by{|a| a.user.name}
    elsif params[:colum] == 'department'
      @studies = @studies.sort_by{|a| a.user.student.department.departmentname}
    elsif params[:colum] == 'faculty'
      @studies = @studies.sort_by{|a| a.user.student.department.faculty.facultyname}
    elsif params[:colum] == 'subject_ID'
      @studies = @studies.sort_by{|a| a.sec.subject.subject_Name}
    elsif params[:colum] == 'sec'
      @studies = @studies.sort_by{|a| a.sec.sec}
    end
  end

  # GET /studies/1
  # GET /studies/1.json
  def show
  end

  # GET /studies/new
  def new
    @study = Study.new
    @users = User.all
    @subjects = Subject.all
    @secs = Sec.all
  end

  # GET /studies/1/edit
  def edit
    @users = User.all
    @subjects = Subject.all
    @secs = Sec.all
  end

  # POST /studies
  # POST /studies.json
  def create
    @study = Study.new
    @user = User.find_by_id(params[:user])
    @study.user = @user
    @sec = Sec.find_by_id(params[:sec])
    @study.sec = @sec
    respond_to do |format|
      if @study.save
        format.html { redirect_to @study, notice: 'Study was successfully created.' }
        format.json { render :show, status: :created, location: @study }
      else
        format.html { render :new }
        format.json { render json: @study.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /studies/1
  # PATCH/PUT /studies/1.json
  def update
    @user = User.find_by_id(params[:user])
    @study.user = @user
    @sec = Sec.find_by_id(params[:sec])
    @study.sec = @sec
    respond_to do |format|
      if @study.update(study_params)
        format.html { redirect_to @study, notice: 'Study was successfully updated.' }
        format.json { render :show, status: :ok, location: @study }
      else
        format.html { render :edit }
        format.json { render json: @study.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /studies/1
  # DELETE /studies/1.json
  def destroy
    @study.destroy
    respond_to do |format|
      format.html { redirect_to studies_url, notice: 'Study was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_study
      @study = Study.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def study_params
      params.require(:study).permit(:user_id, :sec_id)
    end
end
