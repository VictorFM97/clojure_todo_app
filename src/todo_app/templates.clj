(ns todo-app.templates
  (:require [hiccup.core :refer [html]]))

(defn index []
  (html [:span "hi"]))