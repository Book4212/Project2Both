class CheckactivitiesController < ApplicationController
  before_action :set_checkactivity, only: [:show, :edit, :update, :destroy]

  # GET /checkactivities
  # GET /checkactivities.json
  def index
    @checkactivities = Checkactivity.all
  end

  # GET /checkactivities/1
  # GET /checkactivities/1.json
  def show
  end

  def show_check
    @activity = Activity.find(params[:activity_id])
    @checkactivities = @activity.checkactivity

    if params[:colum] == 'name'
      @checkactivities = @checkactivities.sort_by{|a| a.user.name}
    elsif params[:colum] == 'activityname'
      @checkactivities = @checkactivities.sort_by{|a| a.activity.activityname}
    elsif params[:colum] == 'time'
      @checkactivities = @checkactivities.sort_by{|a| a.time_check}
    end
  end
  
  def download_activity_data_csv
    @activity = Activity.find(params[:activity_id])
    @checkactivities = @activity.checkactivity
    
    csv_file = CSV.generate do |csv|
      csv << ['บันทึกการเช็คชื่อเข้าร่วมกิจกรรม']
      csv << ['ชื่อ', 'ชื่อกิจกรรม', 'เวลา']
      @checkactivities.each do |checkactivity|
        csv << [checkactivity.user.name, checkactivity.activity.activityname, checkactivity.time_check.strftime('วันที่ %d / %m / %Y เวลา %H:%M:%S')]
      end
    end
    csv_file = "\xEF\xBB\xBF" + csv_file
    send_data csv_file, :disposition => "attachment; filename=#{@activity.activityname}.csv"
  end

  # GET /checkactivities/new
  def new
    @checkactivity = Checkactivity.new
    @activities = Activity.all
    @users = User.all
  end

  # GET /checkactivities/1/edit
  def edit
    @activities = Activity.all
    @users = User.all
  end

  # POST /checkactivities
  # POST /checkactivities.json
  def create
    @checkactivity = Checkactivity.new
    @user = User.find_by_id(params[:user])
    @checkactivity.user = @user
    @activity = Activity.find_by_id(params[:activity])
    @checkactivity.activity = @activity
    respond_to do |format|
      if @checkactivity.save
        format.html { redirect_to @checkactivity, notice: 'Checkactivity was successfully created.' }
        format.json { render :show, status: :created, location: @checkactivity }
      else
        format.html { render :new }
        format.json { render json: @checkactivity.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /checkactivities/1
  # PATCH/PUT /checkactivities/1.json
  def update
    @user = User.find_by_id(params[:user])
    @checkactivity.user = @user
    @activity = Activity.find_by_id(params[:activity])
    @checkactivity.activity = @activity
    respond_to do |format|
      if @checkactivity.update(checkactivity_params)
        format.html { redirect_to @checkactivity, notice: 'Checkactivity was successfully updated.' }
        format.json { render :show, status: :ok, location: @checkactivity }
      else
        format.html { render :edit }
        format.json { render json: @checkactivity.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /checkactivities/1
  # DELETE /checkactivities/1.json
  def destroy
    @checkactivity.destroy
    respond_to do |format|
      format.html { redirect_to checkactivities_url, notice: 'Checkactivity was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_checkactivity
      @checkactivity = Checkactivity.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def checkactivity_params
      params.require(:checkactivity).permit(:user_id, :activity_id, :time_check)
    end
end
