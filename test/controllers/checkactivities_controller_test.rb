require 'test_helper'

class CheckactivitiesControllerTest < ActionController::TestCase
  setup do
    @checkactivity = checkactivities(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:checkactivities)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create checkactivity" do
    assert_difference('Checkactivity.count') do
      post :create, checkactivity: { activity_id: @checkactivity.activity_id, user_id: @checkactivity.user_id }
    end

    assert_redirected_to checkactivity_path(assigns(:checkactivity))
  end

  test "should show checkactivity" do
    get :show, id: @checkactivity
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @checkactivity
    assert_response :success
  end

  test "should update checkactivity" do
    patch :update, id: @checkactivity, checkactivity: { activity_id: @checkactivity.activity_id, user_id: @checkactivity.user_id }
    assert_redirected_to checkactivity_path(assigns(:checkactivity))
  end

  test "should destroy checkactivity" do
    assert_difference('Checkactivity.count', -1) do
      delete :destroy, id: @checkactivity
    end

    assert_redirected_to checkactivities_path
  end
end
