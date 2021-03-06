(ns todo-app.api
  (:require [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.util.response :refer [response]]
            [compojure.core :refer [defroutes context GET POST DELETE PUT]]
            [compojure.route :refer [not-found]]
            [compojure.coercions :refer [as-int]]
            [todo-app.tasks.business :as tasks]
            [todo-app.profile.business :as profiles]
            [todo-app.middleware.logger :refer [wrap-logger]]))

; TODO: WRITE ACCEPTANCE TESTS FOLLOWING THE FLOW OF THE API

(defn invalid-entity [body]
  {:status 422 :body body})

(defroutes routes
  (context "/api" []
    (context "/profile" []
      (POST "/add" req
        (if-let [resp (profiles/add! (:body req))]
          (response resp)
          (invalid-entity "Invalid profile")))
      (context "/:id{[0-9]+}" [id :<< as-int]
        (GET "/" []
          (response (profiles/get-by-id id)))))
    (context "/task" []
      (POST "/add" req
        (if-let [resp (tasks/add! (:body req))]
          (response resp)
          (invalid-entity "Invalid task")))
      (DELETE "/delete" req
        (if-let [resp (tasks/update! (:id (:body req)) :deleted true)]
          (response resp)
          (not-found "Task not found")))
      (PUT "/done" req
        (if-let [resp (tasks/update! (:id (:body req)) :done (:done (:body req)))]
          (response resp)
          (not-found "Task not found")))
      (context "/:profile-id{[0-9]+}" [profile-id :<< as-int]
        (GET "/most-recent" []
          (response (tasks/get-most-recent profile-id)))
        (GET "/all" []
          (response (tasks/get-all profile-id))))))
  (not-found "404 not found"))

(def api
  (-> routes
      (wrap-json-body {:keywords? true})
      (wrap-json-response)
      (wrap-cors :access-control-allow-origin [#"http://localhost:8000"]
                 :access-control-allow-methods [:get :put :post :delete])))

(defn create-api [] api)

(defn create-api-with-logger []
  (wrap-logger api))