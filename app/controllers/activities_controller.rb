class ActivitiesController < ApplicationController
  before_action :set_activity, only: [:show, :edit, :update, :destroy]

  # GET /activities
  # GET /activities.json
  def index
    @activities = Activity.all
  end

  # GET /activities/1
  # GET /activities/1.json
  def show
  end

  # GET /activities/show/1
  # GET /activities/show/1.json
  def show_sec
    @sec = Sec.find(params[:sec_id])
    @activities = @sec.activity

    if params[:colum] == 'subject'
      @activities = @activities.sort_by{|a| a.sec.subject.subject_Name}
    elsif params[:colum] == 'sec'
      @activities = @activities.sort_by{|a| a.sec.sec}
    elsif params[:colum] == 'activityname'
      @activities = @activities.sort_by{|a| a.activityname}
    elsif params[:colum] == 'dateactivity'
      @activities = @activities.sort_by{|a| a.dateactivity}
    elsif params[:colum] == 'place'
      @activities = @activities.sort_by{|a| a.place}
    elsif params[:colum] == 'count'
      @activities = @activities.sort_by{|a| a.counts}
    end
  end


  # GET /activities/new
  def new
    @activity = Activity.new
    @subjects = Subject.all
    @secs = Sec.all
  end

  # GET /activities/1/edit
  def edit
    @subjects = Subject.all
    @secs = Sec.all
  end

  # POST /activities
  # POST /activities.json
  def create
    @activity = Activity.new(activity_params)
    @sec = Sec.find_by_id(params[:sec])
    @activity.sec = @sec
    respond_to do |format|
      if @activity.save
        format.html { redirect_to @activity, notice: 'Activity was successfully created.' }
        format.json { render :show, status: :created, location: @activity }
      else
        format.html { render :new }
        format.json { render json: @activity.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /activities/1
  # PATCH/PUT /activities/1.json
  def update
    @sec = Sec.find_by_id(params[:sec])
    @activity.sec = @sec
    respond_to do |format|
      if @activity.update(activity_params)
        format.html { redirect_to @activity, notice: 'Activity was successfully updated.' }
        format.json { render :show, status: :ok, location: @activity }
      else
        format.html { render :edit }
        format.json { render json: @activity.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /activities/1
  # DELETE /activities/1.json
  def destroy
    @activity.destroy
    redirect_to secs_url
    #respond_to do |format|
    #  format.html { redirect_to secs_url, notice: 'Activity was successfully destroyed.' }
    #  format.json { head :no_content }
    #end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_activity
      @activity = Activity.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def activity_params
      params.require(:activity).permit(:activityname, :dateactivity, :place, :sec_id, :counts)
    end
end
