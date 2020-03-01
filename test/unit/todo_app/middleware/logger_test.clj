(ns todo-app.middleware.logger-test
  (:require [midje.sweet :refer [facts fact => against-background after]]
            [todo-app.middleware.logger :as logger]
            [clojure.string :refer [includes?]]
            [clojure.java.io :refer [delete-file]]))

(defn file-name
  []
  (str (logger/format-date) "-api.log"))

(defn simulate-request
  [endpoint method body]
  {:headers []
   :body body
   :url endpoint
   :method method})

(facts "Format date function"
       (fact "Format date with no args should return formated date as yyyy-MM-dd"
             (logger/format-date) => (.format (java.text.SimpleDateFormat. "yyyy-MM-dd") (java.util.Date.)))
       (fact "Format date with 1 arg should return current date with sent format"
             (logger/format-date "yyyy-dd-MM") => (.format (java.text.SimpleDateFormat. "yyyy-dd-MM") (java.util.Date.)))
       (fact "Format date with 2 args should return sent date with sent format"
             (let [date (.parse
                         (java.text.SimpleDateFormat. "yyyy-dd-MM hh:mm:ss")
                         "2020-01-02 10:00:30")]
               (logger/format-date "dd/MM/yyyy hh:mm" date) => "01/02/2020 10:00")))

;; (fact "File name should be current date"
;;       (logger/file-name) => (str "/logs/" (logger/format-date) "-api.log"))

(facts "When receiving a request it should create a file with the datetime, endpoint and request body"
       ((logger/wrap-logger (fn [_] 123))
        (simulate-request "/api/task/add" "POST" "{:title \"123\" :description \"456\" :profile-id 1}"))
       (let [file-content (slurp "logger_unit_test.log")]
         (fact "Should contain date and time"
               (includes? file-content (logger/format-date "yyyy-MM-dd hh:mm:ss")) => true)
         (fact "Should contain body of the request"
               (includes? file-content "{:title \"123\" :description \"456\" :profile-id 1}") => true)
         (fact "Should contain endpoint"
               (includes? file-content "/api/task/add") => true)
         (fact "Should contain method"
               (includes? file-content "POST") => true))
       (against-background (logger/file-name) => "logger_unit_test.log")
       (delete-file "logger_unit_test.log"))