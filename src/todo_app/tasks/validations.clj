(ns todo-app.tasks.validations
  (:require [todo-app.tasks.business :refer [get-by-id]]))

(defn is-deleted?
  [id]
  (let [task (get-by-id id)
        deleted (:deleted task)]
    deleted))