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
  (html [:div.todo-list {:style "display: none"}
         [:div.input-group
          [:input#task-title {:type "text"}]]
         [:div.input-group
          [:input#task-description {:type "text"}]]
         [:button.add-task "+"]
         [:ul]]))

(defn todo-list-element [{title :title description :description}]
  (html [:li
         [:p title]
         [:p description]
         [:button "Save"]
         [:button "Delete"]]))

(defn index []
  (render-html (html [:div (profile) (todo-list)])))