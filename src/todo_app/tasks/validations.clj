(ns todo-app.tasks.validations
  (:require [todo-app.profile.business :refer [get-by-id]]))

(defn deleted?
  [task]
  (:deleted task))

(defn valid?
  [task]
  (let [{title :title
         description :description
         profile-id :profile-id} task]
    (and (string? title)
         (> (count title) 0)
         (string? description)
         (> (count description) 0)
         (number? profile-id)
         (> profile-id 0)
         (not (nil? (get-by-id profile-id))))))