class SecsController < ApplicationController
  before_action :set_sec, only: [:show, :edit, :update, :destroy]

  # GET /secs
  # GET /secs.json
  def index
    if current_user.role == "teacher" 
      @secs = current_user.secs
    elsif current_user.role == "student" 
      @secs = current_user.sec
    else
      @secs = Sec.all
    end

    if params[:commit].present?
      @subjects = Subject.search(params[:search])
      @secss = []
      if current_user.role == "teacher" 
        @secs = current_user.secs
      elsif current_user.role == "student" 
        @secs = current_user.sec
      else
        @secs = Sec.all
      end
      @subjects.each do |subject|
        subject.secs.each do |sec|
          @secs.each do |sec2|
            if sec2 == sec
              @secss << sec
            end
          end
        end
      end
      @secs = @secss
    end

    if params[:colum] == 'subject_ID'
      @secs = @secs.sort_by{|a| a.subject.subject_ID}
    elsif params[:colum] == 'sec'
      @secs = @secs.sort_by{|a| a.sec}
    elsif params[:colum] == 'term'
      @secs = @secs.sort_by{|a| a.term}
    elsif params[:colum] == 'classroom'
      @secs = @secs.sort_by{|a| a.classroom}
    elsif params[:colum] == 'timestudy'
      @secs = @secs.sort_by{|a| a.timestudy}
    end

    respond_to do |format|
      format.html
      format.csv {render text: @secs.to_csv}
    end
  end

  # GET /secs/1
  # GET /secs/1.json
  def show
  end

  # GET /secs/new
  def new
    @sec = Sec.new
    @subjects = Subject.all
  end

  # GET /secs/1/edit
  def edit
    @subjects = Subject.all
  end

  # POST /secs
  # POST /secs.json
  def create
    @sec = Sec.new(sec_params)
    @subject = Subject.find_by_id(params[:subject])
    @sec.subject = @subject
    respond_to do |format|
      if @sec.save
        format.html { redirect_to @sec, notice: 'Sec was successfully created.' }
        format.json { render :show, status: :created, location: @sec }
      else
        format.html { render :new }
        format.json { render json: @sec.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /secs/1
  # PATCH/PUT /secs/1.json
  def update
    @subject = Subject.find_by_id(params[:subject])
    @sec.subject = @subject
    respond_to do |format|
      if @sec.update(sec_params)
        format.html { redirect_to @sec, notice: 'Sec was successfully updated.' }
        format.json { render :show, status: :ok, location: @sec }
      else
        format.html { render :edit }
        format.json { render json: @sec.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /secs/1
  # DELETE /secs/1.json
  def destroy
    @sec.destroy
    respond_to do |format|
      format.html { redirect_to secs_url, notice: 'Sec was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  def findsec
    @subject = Subject.find(params[:subject_id])
    @secs = @subject.secs

    respond_to do |format|
      format.html
      format.json{render json: @secs}
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_sec
      @sec = Sec.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def sec_params
      params.require(:sec).permit(:sec, :years, :term, :classroom, :timestudy)
    end
end
