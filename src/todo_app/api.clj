(ns todo-app.api
  (:require [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :refer [response]]
            [compojure.core :refer [defroutes context GET POST]]
            [compojure.route :refer [not-found]]
            [compojure.coercions :refer [as-int]]
            [todo-app.tasks.business :as tasks]))

(defn format-response
  "Format the response of the request"
  [data]
  (response {:success true :data data}))

(defroutes routes
  (context "/api" []
    (context "/todo" []
      (context "/:id{[0-9]+}" [id :<< as-int]
        (POST "/add" req (tasks/add! (:body req)))
        (GET "/most-recent" [] (tasks/get-most-recent id))
        (GET "/all" [] (tasks/get-all id))))
    (context "/profile" [])
    (not-found "404 not found")))

(def api (-> routes
             (wrap-json-body {:keywords? true})
             (wrap-json-response)))

(defn create-api
  []
  api)