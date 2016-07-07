require 'test_helper'

class CheckstudiesControllerTest < ActionController::TestCase
  setup do
    @checkstudy = checkstudies(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:checkstudies)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create checkstudy" do
    assert_difference('Checkstudy.count') do
      post :create, checkstudy: { round: @checkstudy.round, sec_id: @checkstudy.sec_id, status: @checkstudy.status, time_check: @checkstudy.time_check, user_id: @checkstudy.user_id }
    end

    assert_redirected_to checkstudy_path(assigns(:checkstudy))
  end

  test "should show checkstudy" do
    get :show, id: @checkstudy
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @checkstudy
    assert_response :success
  end

  test "should update checkstudy" do
    patch :update, id: @checkstudy, checkstudy: { round: @checkstudy.round, sec_id: @checkstudy.sec_id, status: @checkstudy.status, time_check: @checkstudy.time_check, user_id: @checkstudy.user_id }
    assert_redirected_to checkstudy_path(assigns(:checkstudy))
  end

  test "should destroy checkstudy" do
    assert_difference('Checkstudy.count', -1) do
      delete :destroy, id: @checkstudy
    end

    assert_redirected_to checkstudies_path
  end
end
