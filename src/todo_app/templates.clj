(ns todo-app.templates
  (:require [hiccup.core :refer [html]]))

(defn render-html [content]
  (html [:html
         [:head
          [:link {:rel "stylesheet" :href "/css/main.css"}]]
         [:body content]
         [:script {:src "/js/form.js" :type "text/javascript"}]]))

(defn profile []
  (html
   [:h2 "Please insert your name"]
   [:div
    [:input#name-input {:type "text"}]
    [:span#name-text]]
   [:button#create-profile.blue "Submit"]))

(defn todo-list-element []
  (html [:li#task-clone.hidden
         [:input.done {:type "checkbox"}]
         [:span.title]
         [:p.description]
         [:button.save "Save"]
         [:button.delete.red "Delete"]]))

(defn todo-list []
  (html [:div.todo-list {:style "display: none"}
         [:div.input-group
          [:label {:for "task-title"} "Title"]
          [:input#task-title {:type "text"}]]
         [:div.input-group
          [:label {:for "task-description"} "Description"]
          [:input#task-description {:type "text"}]]
         [:button.add-task.blue "Add task"]
         [:ul
          (todo-list-element)]]))

(defn index []
  (render-html (html [:div#container (profile) (todo-list)])))