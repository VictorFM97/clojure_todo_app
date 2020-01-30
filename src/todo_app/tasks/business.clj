(ns todo-app.tasks.business
  (:require [todo-app.tasks.repository :as repo]
            [todo-app.tasks.validations :refer [deleted? valid?]]))

(defn filter-tasks
  [pred]
  (filter pred (repo/get-tasks)))

(defn base-filter
  [id]
  #(= id (:profile-id %)))

(defn get-all
  [profile-id]
  (filter-tasks (base-filter profile-id)))

(defn get-most-recent
  [profile-id]
  (last (get-all profile-id)))

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
  (if (valid? task)
    (repo/add! task)
    "Invalid task"))

(defn update!
  [id key value]
  (let [ret (execute-action
             (deleted? (get-by-id id))
             #(repo/update! id key value)
             "Task is already deleted")]
    (if (string? ret)
      ret
      (get-by-id id))))