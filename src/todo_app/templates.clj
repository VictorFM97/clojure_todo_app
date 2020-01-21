(ns todo-app.templates
  (:require [hiccup.core :refer [html]]))

(defn render-html [content]
  (html [:html
         [:head]
         [:body
          content]
         [:script {:src "/js/form.js" :type "text/javascript"}]]))

(defn profile []
  (html
   [:h2 "Please insert your name"]
   [:div
    [:input#name-input {:type "text"}]
    [:span#name-text]]
   [:button#create-profile "Submit"]))

(defn todo-list []
  (html [:div.todo-list
         [:button.add-task "+"]
         [:ul
          (for [x (range 1 4)]
            [:li x])]]))

(defn index []
  (render-html (html [:div (profile) (todo-list)])))