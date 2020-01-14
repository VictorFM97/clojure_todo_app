(ns todo-app.tasks.business
  (:require [todo-app.tasks.repository :as repo]
            [todo-app.tasks.validations :refer [is-deleted?]]))

(defn filter-tasks
  [pred]
  (repo/order-by pred))

(defn base-filter
  [id]
  #(= id (:profile-id %)))

(defn get-all
  [id-profile]
  (filter-tasks (base-filter id-profile)))

(defn get-most-recent
  [id-profile]
  (last (filter-tasks (base-filter id-profile))))

(defn get-by-id
  [id]
  (first (filter-tasks #(= id (:id %)))))

(defn execute-action
  [should-execute action message]
  (if (true? should-execute)
    message
    (action)))

(defn add!
  [task]
  (repo/add! task))

(defn update!
  [id key value]
  (let [ret (execute-action
             (is-deleted? id)
             #(repo/update! id key value)
             "Task is already deleted")]
    (if (string? ret)
      ret
      (get-by-id id))))