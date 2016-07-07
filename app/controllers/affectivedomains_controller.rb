class AffectivedomainsController < ApplicationController
  before_action :set_affectivedomain, only: [:show, :edit, :update, :destroy]

  # GET /affectivedomains
  # GET /affectivedomains.json
  def index
    @affectivedomains = Affectivedomain.all
  end

  def show_eff
    @user = User.find(params[:user_id])
    @affectivedomains = @user.affectivedomain

    @sec = Sec.find(params[:sec_id])

    if student_user?
      @affectivedomains = @affectivedomains.where(:sec_id => (params[:sec_id]))
    end

    @point = @affectivedomains.inject(0) { |sum, hash| sum + hash[:point] }

    if params[:colum] == 'name'
      @affectivedomains = @affectivedomains.sort_by{|a| a.user.name}
    elsif params[:colum] == 'subject_name'
      @affectivedomains = @affectivedomains.sort_by{|a| a.sec.subject.subject_Name}
    elsif params[:colum] == 'sec'
      @affectivedomains = @affectivedomains.sort_by{|a| a.sec.sec}
    elsif params[:colum] == 'point'
      @affectivedomains = @affectivedomains.sort_by{|a| a.point}
    elsif params[:colum] == 'active_day'
      @affectivedomains = @affectivedomains.sort_by{|a| a.active_day}
    end
  end
  
  def download_aff_data_csv
    @user = User.find(params[:user_id])
    @affectivedomains = @user.affectivedomain

    @sec = Sec.find(params[:sec_id])

    if student_user?
      @affectivedomains = @affectivedomains.where(:sec_id => (params[:sec_id]))
    end

    @point = @affectivedomains.inject(0) { |sum, hash| sum + hash[:point] }
    
    csv_file = CSV.generate do |csv|
      csv << ['บันทึกการให้คะแนนจิตพิสัย']
      csv << ['วิชา', @sec.subject.subject_ID + ' ' + @sec.subject.subject_Name]
      csv << ['รหัสนักศึกษา', @user.student.studentid, 'ชื่อนักศึกษา', @user.name, 'นักศึกษาชั้นปีที่', @sec.years.to_i - @user.student.firstyear + 1]
      csv << ['คณะ', @user.student.department.faculty.facultyname, 'สาขาวิชา', @user.student.department.departmentname]
      csv << ['คะแนนจิตพิสัยรวม', @point.to_s + ' คะแนน']
      csv << ['ชื่อ', 'วิชา', 'ตอนเรียน', 'คะแนน', 'วันที่บันทึก']
      @affectivedomains.each do |affectivedomain|
        csv << [affectivedomain.user.name, affectivedomain.sec.subject.subject_ID + ' ' + affectivedomain.sec.subject.subject_Name, affectivedomain.sec.sec, affectivedomain.point, affectivedomain.active_day.strftime('วันที่ %d / %m / %Y เวลา %H:%M:%S')]
      end
    end
    csv_file = "\xEF\xBB\xBF" + csv_file
    send_data csv_file, :disposition => "attachment; filename=#{@user.student.studentid}_score_aff.csv"
  end

  # GET /affectivedomains/1
  # GET /affectivedomains/1.json
  def show
  end

  # GET /affectivedomains/new
  def new
    @affectivedomain = Affectivedomain.new
    @users = User.all
    @subjects = Subject.all
    @secs = Sec.all
  end

  # GET /affectivedomains/1/edit
  def edit
    @users = User.all
    @subjects = Subject.all
    @secs = Sec.all
  end

  # POST /affectivedomains
  # POST /affectivedomains.json
  def create
    @affectivedomain = Affectivedomain.new(affectivedomain_params)
    @user = User.find_by_id(params[:user])
    @affectivedomain.user = @user
    @sec = Sec.find_by_id(params[:sec])
    @affectivedomain.sec = @sec
    respond_to do |format|
      if @affectivedomain.save
        format.html { redirect_to @affectivedomain, notice: 'Affectivedomain was successfully created.' }
        format.json { render :show, status: :created, location: @affectivedomain }
      else
        format.html { render :new }
        format.json { render json: @affectivedomain.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /affectivedomains/1
  # PATCH/PUT /affectivedomains/1.json
  def update
    @user = User.find_by_id(params[:user])
    @affectivedomain.user = @user
    @sec = Sec.find_by_id(params[:sec])
    @affectivedomain.sec = @sec
    respond_to do |format|
      if @affectivedomain.update(affectivedomain_params)
        format.html { redirect_to @affectivedomain, notice: 'Affectivedomain was successfully updated.' }
        format.json { render :show, status: :ok, location: @affectivedomain }
      else
        format.html { render :edit }
        format.json { render json: @affectivedomain.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /affectivedomains/1
  # DELETE /affectivedomains/1.json
  def destroy
    @affectivedomain.destroy
    respond_to do |format|
      format.html { redirect_to affectivedomains_url, notice: 'Affectivedomain was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  def receiveStudentScore
    @affectivedomain = Affectivedomain.new
    @affectivedomain.point = params[:score].to_f
    timesprit = params[:time_check].split('-')
    timesprit = timesprit[2] + '-' + timesprit[1] + '-' + timesprit[0] + ' ' + timesprit[3] + ':' + timesprit[4] + ':' + timesprit[5]

    @affectivedomain.active_day = timesprit

    @student = Student.find_by_studentid(params[:student_id])
    if @student == nil
      render :text => ''
    else
      @user = @student.user
      if @user == nil
        render :text => ''
      else
        @affectivedomain.user = @user
        @subject = Subject.find_by_subject_ID(params[:course_id])
        if @subject == nil
          render :text => ''
        else
          @secs = Sec.where(:subject_id => @subject)
          if @secs.length == 0
            render :text => ''
          else
            @sec = Sec.find_by_sec(params[:section])
            if @sec == nil
              render :text => ''
            else
              @affectivedomain.sec = @sec
              @affectivedomain.save!
              render :text => ''
            end
          end
        end
      end
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_affectivedomain
      @affectivedomain = Affectivedomain.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def affectivedomain_params
      params.require(:affectivedomain).permit(:point, :description, :active_day, :user_id, :sec_id)
    end
end
