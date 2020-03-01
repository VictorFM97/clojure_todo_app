(ns todo-app.middleware.logger)

(defn format-date
  ([format date]
   (.format (java.text.SimpleDateFormat. format) date))
  ([format]
   (format-date format (java.util.Date.)))
  ([]
   (format-date "yyyy-MM-dd" (java.util.Date.))))

(defn file-name
  []
  (str
   ;"/logs/" 
   (format-date) "-api.log"))

(defn format-request
  [{method :method body :body url :url}]
  (str (format-date "yyyy-MM-dd hh:mm:ss") ": " method " " url " " body))

(defn wrap-logger
  [handler]
  (fn [request]
    (spit (file-name) (format-request request))
    (handler request)))