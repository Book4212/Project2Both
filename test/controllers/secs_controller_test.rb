require 'test_helper'

class SecsControllerTest < ActionController::TestCase
  setup do
    @sec = secs(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:secs)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create sec" do
    assert_difference('Sec.count') do
      post :create, sec: { classroom: @sec.classroom, sec: @sec.sec, subject_id: @sec.subject_id, term: @sec.term, timestudy: @sec.timestudy, years: @sec.years }
    end

    assert_redirected_to sec_path(assigns(:sec))
  end

  test "should show sec" do
    get :show, id: @sec
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @sec
    assert_response :success
  end

  test "should update sec" do
    patch :update, id: @sec, sec: { classroom: @sec.classroom, sec: @sec.sec, subject_id: @sec.subject_id, term: @sec.term, timestudy: @sec.timestudy, years: @sec.years }
    assert_redirected_to sec_path(assigns(:sec))
  end

  test "should destroy sec" do
    assert_difference('Sec.count', -1) do
      delete :destroy, id: @sec
    end

    assert_redirected_to secs_path
  end
end
