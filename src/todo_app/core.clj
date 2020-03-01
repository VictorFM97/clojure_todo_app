(ns todo-app.core
  (:require [ring.adapter.jetty :as jetty]
            [todo-app.api :refer [create-api-with-logger]]
            [todo-app.app :refer [create-app]]))

(def api (atom nil))
(def app (atom nil))

(defn start-x [create settings]
  (jetty/run-jetty (create) settings))

(defn stop-x [atom]
  (.stop @atom)
  (reset! atom nil))

(defn -main
  [& args]
  (reset! api (start-x create-api-with-logger {:port 8001 :join? false}))
  (reset! app (start-x create-app {:port 8000 :join? false})))