Rails.application.routes.draw do
  resources :studies
  get 'studies/show/:sec_id' => 'studies#show_student', as: "study_show_student"
  resources :teaches
  get 'teaches/show/:sec_id' => 'teaches#show_teach', as: "teach_show_sec"
  resources :affectivedomains
  get 'affectivedomains/show/:user_id' => 'affectivedomains#show_eff', as: "eff_show_student"
  resources :checkstudies
  get 'checkstudies/show/:user_id' => 'checkstudies#show_check', as: "check_show_study"
  resources :checkactivities
  get 'checkactivities/show/:activity_id' => 'checkactivities#show_check', as: "check_show_activity"
  resources :activities
  get 'activities/show/:sec_id' => 'activities#show_sec', as: "activity_show_sec"
  resources :students
  resources :teachers
  resources :departments
  resources :faculties
  resources :secs
  resources :subjects
  get 'sessions/new'

  get 'users/new'

  root 'static_pages#home'

  get'help' => 'static_pages#help'

  get'about' => 'static_pages#about'

  get'contact' => 'static_pages#contact'

  get'signup' => 'users#new'

  get 'login' => 'sessions#new'

  post 'login' => 'sessions#create'

  delete 'logout' => 'sessions#destroy'

  resources:users do
    get '/students/new' => 'students#new_first', as: "new_students"
    get '/teachers/new' => 'teachers#new_first', as: "new_teachers"
    # post '/students' => 'students#create_first', as: "create_students"
  end

  get 'finddepartment/:faculty_id' => 'departments#finddepartment'

  get 'findsec/:subject_id' => 'secs#findsec'
  # receive and sent data between android device
  get 'getTeacherPrivateData' => 'teaches#getTeacherPrivateData'

  get 'receiveCheckStudentName' => 'checkstudies#receiveCheckStudentName'

  get 'receiveStudentScore' => 'affectivedomains#receiveStudentScore'
  
  
  get 'downloadCheckNameCSV' => 'checkstudies#download_check_data_csv'
  get 'downloadAffCSV' => 'affectivedomains#download_aff_data_csv'
  get 'downloadActivityCSV' => 'checkactivities#download_activity_data_csv'
  
  # The priority is based upon order of creation: first created -> highest priority.
  # See how all your routes lay out with "rake routes".

  # You can have the root of your site routed with "root"
  # root 'welcome#index'

  # Example of regular route:
  #   get 'products/:id' => 'catalog#view'

  # Example of named route that can be invoked with purchase_url(id: product.id)
  #   get 'products/:id/purchase' => 'catalog#purchase', as: :purchase

  # Example resource route (maps HTTP verbs to controller actions automatically):
  #   resources :products

  # Example resource route with options:
  #   resources :products do
  #     member do
  #       get 'short'
  #       post 'toggle'
  #     end
  #
  #     collection do
  #       get 'sold'
  #     end
  #   end

  # Example resource route with sub-resources:
  #   resources :products do
  #     resources :comments, :sales
  #     resource :seller
  #   end

  # Example resource route with more complex sub-resources:
  #   resources :products do
  #     resources :comments
  #     resources :sales do
  #       get 'recent', on: :collection
  #     end
  #   end

  # Example resource route with concerns:
  #   concern :toggleable do
  #     post 'toggle'
  #   end
  #   resources :posts, concerns: :toggleable
  #   resources :photos, concerns: :toggleable

  # Example resource route within a namespace:
  #   namespace :admin do
  #     # Directs /admin/products/* to Admin::ProductsController
  #     # (app/controllers/admin/products_controller.rb)
  #     resources :products
  #   end
end
