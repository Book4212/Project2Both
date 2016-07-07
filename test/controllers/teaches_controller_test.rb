require 'test_helper'

class TeachesControllerTest < ActionController::TestCase
  setup do
    @teach = teaches(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:teaches)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create teach" do
    assert_difference('Teach.count') do
      post :create, teach: { sec_id: @teach.sec_id, user_id: @teach.user_id }
    end

    assert_redirected_to teach_path(assigns(:teach))
  end

  test "should show teach" do
    get :show, id: @teach
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @teach
    assert_response :success
  end

  test "should update teach" do
    patch :update, id: @teach, teach: { sec_id: @teach.sec_id, user_id: @teach.user_id }
    assert_redirected_to teach_path(assigns(:teach))
  end

  test "should destroy teach" do
    assert_difference('Teach.count', -1) do
      delete :destroy, id: @teach
    end

    assert_redirected_to teaches_path
  end
end
