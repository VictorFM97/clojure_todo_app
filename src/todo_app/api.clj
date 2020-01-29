(ns todo-app.api
  (:require [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.util.response :refer [response]]
            [compojure.core :refer [defroutes context GET POST DELETE PUT]]
            [compojure.route :refer [not-found]]
            [compojure.coercions :refer [as-int]]
            [todo-app.tasks.business :as tasks]
            [todo-app.profile.business :as profiles]))

; TODO: WRITE ACCEPTANCE TESTS FOLLOWING THE FLOW OF THE API
; TODO: WRITE UNIT TESTS FOR THE REST OF THE FILES

(defroutes routes
  (context "/api" []
    (context "/profile" []
      (POST "/add" req (response (profiles/add! (:body req))))
      (context "/:id{[0-9]+}" [id :<< as-int]
        (GET "/" [] (response (profiles/get-by-id id)))))
    (context "/todo" []
      (POST "/add" req (response (tasks/add! (:body req))))
      (DELETE "/delete" req (response (tasks/update! (:id (:body req)) :deleted true)))
      (PUT "/done/" req (tasks/update!) (:id req) :done (:done req))
      (context "/:id{[0-9]+}" [id :<< as-int]
        (GET "/most-recent" [] (tasks/get-most-recent id))
        (GET "/all" [] (response (tasks/get-all id)))
        (GET "/" [] (tasks/get-by-id id))))
    (not-found "404 not found")))

(defn create-api []
  (-> routes
      ; (defaults/wrap-defaults defaults/api-defaults)
             (wrap-json-body {:keywords? true})
             (wrap-json-response)
             (wrap-cors :access-control-allow-origin [#"http://localhost:8000"]
                        :access-control-allow-methods [:get :put :post :delete])))
