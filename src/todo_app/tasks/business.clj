(ns todo-app.tasks.business
  (:require [todo-app.tasks.repository :as repo]
            [todo-app.tasks.validations :refer [deleted? valid?]]))

(defn filter-tasks
  [pred]
  (filter pred (repo/get-tasks)))

(defn base-filter
  [id]
  #(and (= id (:profile-id %))
        (false? (:deleted %))))

(defn get-all
  [profile-id]
  (filter-tasks (base-filter profile-id)))

(defn get-most-recent
  [profile-id]
  (last (get-all profile-id)))

(defn get-by-id
  [id]
  (first (filter-tasks #(= id (:id %)))))

(defn add!
  [task]
  (if (valid? task)
    (repo/add! task)
    false))

(defn update!
  [id key value]
  (if-let [task  (get-by-id id)]
    (do (repo/update! id key value)
        (merge task {key value}))
    false))