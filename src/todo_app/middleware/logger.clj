(ns todo-app.middleware.logger
  (:require [clojure.java.io :refer [make-parents]]))

(defn format-date
  ([format date]
   (.format (java.text.SimpleDateFormat. format) date))
  ([format]
   (format-date format (java.util.Date.)))
  ([]
   (format-date "yyyy-MM-dd" (java.util.Date.))))

(defn file-name
  []
  (str "logs/" (format-date) "-api.log"))

(defn format-request
  [{method :request-method body :body uri :uri}]
  (str (format-date "yyyy-MM-dd hh:mm:ss") ": " method " " uri " " (slurp body) "\n"))

(defn wrap-logger
  [handler]
  (fn [request]
    (let [file-name (file-name)]
      (make-parents file-name)
      (spit file-name (format-request request) :append true))
    (handler request)))