(ns todo-app.app
  (:require [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [compojure.core :refer [defroutes context GET]]
            [compojure.route :refer [not-found]]
            [todo-app.templates :as templates]))

(defroutes routes
  (GET "/" [] (templates/index))
  (context "/todo" []
    (not-found "404 not found")))

(def app (-> routes
             (wrap-json-body {:keywords? true})
             (wrap-json-response)))

(defn create-app
  []
  app)