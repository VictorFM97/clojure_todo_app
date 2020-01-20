(ns todo-app.api
  (:require [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.util.response :refer [response]]
            [compojure.core :refer [defroutes context GET POST DELETE PUT]]
            [compojure.route :refer [not-found]]
            [compojure.coercions :refer [as-int]]
            [todo-app.tasks.business :as tasks]
            [todo-app.profile.business :as profiles]))

(defn format-response
  "Format the response of the request"
  [data]
  (response data))

(defroutes routes
  (context "/api" []
    (context "/profile" []
      (POST "/add" req (format-response (profiles/add! (:body req)))))
    (context "/todo" []
      (POST "/add" req (format-response (tasks/add! (:body req))))
      (DELETE "/delete" req (format-response (tasks/update! (:id (:body req)) :deleted true)))
      (PUT "/done/" req (tasks/update!) (:id req) :done (:done req))
      (context "/:id{[0-9]+}" [id :<< as-int]
        (GET "/most-recent" [] (tasks/get-most-recent id))
        (GET "/all" [] (tasks/get-all id))
        (GET "/" [] (tasks/get-by-id id))))
    (context "/profile" [])
    (not-found "404 not found")))

; find better way to format response

(def api (-> routes
             (wrap-json-body {:keywords? true})
             (wrap-json-response)
             (wrap-cors :access-control-allow-origin [#"http://localhost:8000"]
                        :access-control-allow-methods [:get :put :post :delete])))

(defn create-api
  []
  api)