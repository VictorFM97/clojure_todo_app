(ns todo-app.app
  (:require [compojure.core :refer [defroutes context GET]]
            [compojure.route :refer [not-found resources]]
            [todo-app.templates :as templates]))

(defroutes routes
  (GET "/" [] (templates/index))
  (context "/todo" []
    (not-found "404 not found"))
  (resources "/"))

(def app routes)

(defn create-app
  []
  app)