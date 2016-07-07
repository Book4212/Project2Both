require 'test_helper'

class AffectivedomainsControllerTest < ActionController::TestCase
  setup do
    @affectivedomain = affectivedomains(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:affectivedomains)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create affectivedomain" do
    assert_difference('Affectivedomain.count') do
      post :create, affectivedomain: { active_day: @affectivedomain.active_day, description: @affectivedomain.description, point: @affectivedomain.point, sec_id: @affectivedomain.sec_id, user_id: @affectivedomain.user_id }
    end

    assert_redirected_to affectivedomain_path(assigns(:affectivedomain))
  end

  test "should show affectivedomain" do
    get :show, id: @affectivedomain
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @affectivedomain
    assert_response :success
  end

  test "should update affectivedomain" do
    patch :update, id: @affectivedomain, affectivedomain: { active_day: @affectivedomain.active_day, description: @affectivedomain.description, point: @affectivedomain.point, sec_id: @affectivedomain.sec_id, user_id: @affectivedomain.user_id }
    assert_redirected_to affectivedomain_path(assigns(:affectivedomain))
  end

  test "should destroy affectivedomain" do
    assert_difference('Affectivedomain.count', -1) do
      delete :destroy, id: @affectivedomain
    end

    assert_redirected_to affectivedomains_path
  end
end
