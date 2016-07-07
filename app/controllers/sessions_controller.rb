class SessionsController < ApplicationController

  def new
  end

  def create
  	user = User.find_by(email: params[:session][:email].downcase)
    if user && user.authenticate(params[:session][:password])
      # Log the user in and redirect to the user's show page.
      log_in user
      # remember user for create token 
      params[:session][:remember_me] == '1' ? remember(user) : forget(user)
      #it means "redirect_to user_path(user)" user in rake rounts
      if teacher_user?
        redirect_to secs_path
      elsif student_user?
        redirect_to secs_path
      else
        redirect_back_or user
      end
    else
      flash.now[:danger] = 'Invalid email/password combination' # Not quite right!
      render 'new'
    end
  end

  def destroy
  	log_out if logged_in?
    redirect_to root_url
  end
end
